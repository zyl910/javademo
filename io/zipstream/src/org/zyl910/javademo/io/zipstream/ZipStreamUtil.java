package org.zyl910.javademo.io.zipstream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static java.util.zip.ZipEntry.STORED;
import static java.util.zip.ZipOutputStream.DEFLATED;

public class ZipStreamUtil {

    public static byte[] toByteArray(InputStream in) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            copyStream(out, in);
            return out.toByteArray();
        }
    }

    public static void copyStream(OutputStream os, InputStream is) throws IOException {
        copyStream(os, is, 0);
    }

    public static void copyStream(OutputStream os, InputStream is, int bufsize) throws IOException {
        if (bufsize <= 0) bufsize = 4096;
        int len;
        byte[] bytes = new byte[bufsize];
        while ((len = is.read(bytes)) != -1) {
            os.write(bytes, 0, len);
        }
    }

    /**
     * 基于ZIP项目的复制Zip流.
     *
     * @param dst       The output stream of the destination zip.
     * @param src       Source zip.
     * @param transform 转换处理. 可以为null, 不转换. 该回调函数的原型为`byte[] transform(ZipEntry zipEntry, ZipInputStream zis)`, 当返回值为 null时保留原值, 为非null时用返回值替换当前ZipEntry对应的流数据.
     * @return 返回转换次数.
     * @throws IOException
     */
    public static int zipEntryCopyStreamZip(ZipOutputStream zos, ZipInputStream zis, BiFunction<ZipEntry, ZipInputStream, byte[]> transform) throws IOException {
        int rt = 0;
        ZipEntry se;
        while ((se = zis.getNextEntry()) != null) {
            if (null == se) continue;
            //String line = String.format("ZipEntry(%s, isDirectory=%d, size=%d, compressedSize=%d, time=%d, crc=%d, method=%d, comment=%s)",
            //        se.getName(), (se.isDirectory())?1:0, se.getSize(), se.getCompressedSize(), se.getTime(), se.getCrc(), se.getMethod(), se.getComment());
            //System.out.println(line);
            byte[] dstBytes = null;
            if (null != transform) {
                dstBytes = transform.apply(se, zis);
            }
            // choose by dstBytes.
            if (null == dstBytes) {
                ZipEntry de = new ZipEntry(se);
                de.setCompressedSize(-1); // 重新压缩后, csize 可能不一致, 故需要恢复为默认值.
                zos.putNextEntry(de);
                copyStream(zos, zis);
                zos.closeEntry();
            } else {
                ++rt;
                // == java.lang.IllegalArgumentException: invalid entry crc-32 at java.util.zip.ZipEntry.setCrc(ZipEntry.java:381)
                //ZipEntry de = new ZipEntry(se);
                //de.setCompressedSize(-1);
                //de.setCrc(-1);
                // == fix IllegalArgumentException.
                ZipEntry de = new ZipEntry(se.getName());
                //System.out.println(se.getTime());
                //final long timeNone = 312739200000L;
                //if (timeNone!=se.getTime() && null!=se.getLastModifiedTime()) { // 发现会被自动改为当前时间.
                if (null != se.getLastModifiedTime()) {
                    de.setLastModifiedTime(se.getLastModifiedTime());
                }
                if (null != se.getLastAccessTime()) {
                    de.setLastAccessTime(se.getLastAccessTime());
                }
                if (null != se.getCreationTime()) {
                    de.setCreationTime(se.getCreationTime());
                }
                de.setSize(dstBytes.length);
                //de.setCompressedSize(se.getCompressedSize()); // changed.
                //de.setCrc(se.getCrc()); // changed.
                int method = se.getMethod();
                if (method != STORED && method != DEFLATED) {
                    // No setMethod .
                } else {
                    de.setMethod(method);
                }
                de.setExtra(se.getExtra());
                de.setComment(se.getComment());
                zos.putNextEntry(de);
                zos.write(dstBytes);
                zos.closeEntry();
            }
        }
        return rt;
    }

    /**
     * 基于ZIP项目的复制流.
     *
     * @param dst       The output stream of the destination zip.
     * @param src       Source zip.
     * @param transform 转换处理. 可以为null, 不转换. 该回调函数的原型为`byte[] transform(ZipEntry zipEntry, ZipInputStream zis)`, 当返回值为 null时保留原值, 为非null时用返回值替换当前ZipEntry对应的流数据.
     * @return 返回转换次数.
     * @throws IOException
     */
    public static int zipEntryCopyStream(OutputStream os, InputStream is, BiFunction<ZipEntry, ZipInputStream, byte[]> transform) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(is)) {
            try (ZipOutputStream zos = new ZipOutputStream(os)) {
                return zipEntryCopyStreamZip(zos, zis, transform);
            }
        }
    }

}
