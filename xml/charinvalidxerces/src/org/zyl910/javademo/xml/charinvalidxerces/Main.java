package org.zyl910.javademo.xml.charinvalidxerces;

import org.apache.axis.encoding.DeserializationContext;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.PrintStream;
import java.io.StringReader;

public class Main {

    /** 处理.
     *
     * @param outs 输出流.
     */
    private void process(PrintStream outs) throws SAXException {
        String strxml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root>  <str>汉字ABC&amp;#16;.</str></root>";
        //testJaxp();
        InputSource is;
        is = new InputSource(new StringReader(strxml));
        DeserializationContext dser = new DeserializationContext(is, null, null);
        //dser.getEnvelope().setOwnerDocument(this);
        dser.parse();
        outs.println(dser);
    }

    public static void main(String[] args) {
        System.out.println("Char invalid xerces");
        try {
            (new Main()).process(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
