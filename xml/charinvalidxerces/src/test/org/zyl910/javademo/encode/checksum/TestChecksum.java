package org.zyl910.javademo.encode.checksum;

import org.zyl910.javademo.xml.charinvalidxerces.Main;

import java.io.*;

public class TestChecksum {
    private PrintStream outs;

    public PrintStream getOuts() {
        return outs;
    }

    public void setOuts(PrintStream outs) {
        this.outs = outs;
    }

    /** 处理.
     *
     * @param args 命令行参数.
     * @throws Exception 异常.
     */
    private void process(String[] args) throws Exception {
        String fileName = null;
        if (args.length>=1) fileName=args[0];
        if (null==fileName || fileName.length()<=0) {
            outs.println("format: TestChecksum <fileName>");
            return;
        }
        processFile(fileName);
    }

    /** 处理文件.
     *
     * @param fileName 文件.
     */
    private void processFile(String fileName) throws IOException {
        File file = new File(fileName);
        byte sum = 0;
        try(FileInputStream fi = new FileInputStream(file)) {
            int n;
            while(-1!=(n=fi.read())) {
                byte by = (byte)n;
                sum ^= by;
            }
        }
        outs.println(String.format("xor checksum: %d # 0x%2X", sum, sum));
    }

    public static void main(String[] args) {
        System.out.println("TestChecksum v1.0");
        try {
            TestChecksum p = new TestChecksum();
            p.setOuts(System.out);
            p.process(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
