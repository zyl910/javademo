package org.zyl910.javademo.io.zipstream;

import java.io.*;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipTxtAppendTest {
    public static void main(String[] args) {
        String srcPath = "resources/text.zip";
        String outPath = "E:\\test\\export\\text_append.zip";
        try(FileInputStream is = new FileInputStream(srcPath)) {
            try(FileOutputStream os = new FileOutputStream(outPath)) {
                ZipStreamUtil.zipEntryCopyStream(os, is, new BiFunction<ZipEntry, ZipInputStream, byte[]>() {
                    @Override
                    public byte[] apply(ZipEntry se, ZipInputStream zis) {
                        byte[] rt = null;
                        String name = se.getName();
                        if (null==name) return rt;
                        String line = String.format("ZipEntry(%s, isDirectory=%d, size=%d, compressedSize=%d, time=%d, crc=%d, method=%d, comment=%s)",
                                se.getName(), (se.isDirectory())?1:0, se.getSize(), se.getCompressedSize(), se.getTime(), se.getCrc(), se.getMethod(), se.getComment());
                        System.out.println(line);
                        if (name.endsWith(".txt")) {
                            String appendString = String.format("\nZipTxtAppendTest %s\n", (new Date()).toString());
                            try {
                                //byte[] oldBytes = ZipStreamUtil.toByteArray(zis);
                                //String str = (new String(oldBytes)) + appendString;
                                //rt = str.getBytes();
                                // 为了避免多余的编码转换, 改成下面的代码更好.
                                try(ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
                                    ZipStreamUtil.copyStream(buf, zis);
                                    buf.write(appendString.getBytes());
                                    rt = buf.toByteArray();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return rt;
                    }
                });
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ZipTxtAppendTest done." + outPath);
    }
}
