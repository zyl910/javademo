package org.zyl910.javademo.io.zipstream;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipReadTest {
    public static void main(String[] args) {
        String srcPath = "resources/test.docx";
        //System.out.println(System.getProperty("user.dir"));
        try(FileInputStream is = new FileInputStream(srcPath)) {
            run(System.out, is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ZipReadTest done.");
    }

    private static void run(PrintStream out, InputStream is) throws IOException {
        try(ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (null==zipEntry) continue;
                String line = String.format("ZipEntry(%s, isDirectory=%d, size=%d, compressedSize=%d, time=%d, crc=%d, method=%d, comment=%s)",
                        zipEntry.getName(), (zipEntry.isDirectory())?1:0, zipEntry.getSize(), zipEntry.getCompressedSize(), zipEntry.getTime(), zipEntry.getCrc(), zipEntry.getMethod(), zipEntry.getComment());
                out.println(line);
            }
        }
    }
}