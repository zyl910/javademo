package org.zyl910.javademo.io.zipstream;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipCopyTest {
    public static void main(String[] args) {
        String srcPath = "resources/test.docx";
        String outPath = "E:\\test\\export\\test_copy.docx";
        try(FileInputStream is = new FileInputStream(srcPath)) {
            try(FileOutputStream os = new FileOutputStream(outPath)) {
                copyZipStream(os, is);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ZipCopyTest done.");
    }

    private static void copyZipStream(OutputStream os, InputStream is) throws IOException {
        try(ZipInputStream zis = new ZipInputStream(is)) {
            try(ZipOutputStream zos = new ZipOutputStream(os)) {
                ZipEntry se;
                while ((se = zis.getNextEntry()) != null) {
                    if (null==se) continue;
                    String line = String.format("ZipEntry(%s, isDirectory=%d, size=%d, compressedSize=%d, time=%d, crc=%d, method=%d, comment=%s)",
                            se.getName(), (se.isDirectory())?1:0, se.getSize(), se.getCompressedSize(), se.getTime(), se.getCrc(), se.getMethod(), se.getComment());
                    System.out.println(line);
                    ZipEntry de = new ZipEntry(se);
                    de.setCompressedSize(-1); // 重新压缩后, csize 可能不一致, 故需要恢复为默认值.
                    zos.putNextEntry(de);
                    copyStream(zos, zis);
                    zos.closeEntry();
                }
            }
        }
    }

    private static void copyStream(OutputStream os, InputStream is) throws IOException {
        copyStream(os, is, 0);
    }

    private static void copyStream(OutputStream os, InputStream is, int bufsize) throws IOException {
        if (bufsize<=0) bufsize= 4096;
        int len;
        byte[] bytes = new byte[bufsize];
        while ((len = is.read(bytes)) != -1) {
            os.write(bytes, 0, len);
        }
    }
}