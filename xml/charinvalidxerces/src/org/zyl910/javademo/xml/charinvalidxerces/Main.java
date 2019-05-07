package org.zyl910.javademo.xml.charinvalidxerces;

import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.utils.XMLUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;

public class Main extends DefaultHandler implements LexicalHandler {
    private PrintStream outs;

    public PrintStream getOuts() {
        return outs;
    }

    public void setOuts(PrintStream outs) {
        this.outs = outs;
    }

    /** 测试axis解析回应xml数据.
     *
     * @param is xml输入流.
     * @throws SAXException SAX异常.
     */
    private void testAxis(InputSource is) throws SAXException {
        DeserializationContext dser = new DeserializationContext(is, null, null);
        //dser.getEnvelope().setOwnerDocument(this);
        dser.parse();
        outs.println(dser);
    }

    /** 测试jxap解析回应xml数据.
     *
     * @param is xml输入流.
     * @throws SAXException SAX异常.
     */
    private void testJaxp(InputSource is) throws SAXException, IOException {
        SAXParser parser = XMLUtils.getSAXParser();
        parser.setProperty("http://xml.org/sax/properties/lexical-handler", this);
        parser.parse(is, this);
        outs.println(parser);
    }

    /** 处理.
     */
    private void process() throws Exception {
        String strxml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root>  <str>汉字ABC&amp;#16;.</str></root>";
        InputSource is;
        is = new InputSource(new StringReader(strxml));
        //testAxis(is);
        testJaxp(is);
    }

    @Override
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        outs.println("startDTD: " + name);
    }

    @Override
    public void endDTD() throws SAXException {
        outs.println("startCDATA");
    }

    @Override
    public void startEntity(String name) throws SAXException {
        outs.println("startDTD: " + name);
    }

    @Override
    public void endEntity(String name) throws SAXException {
        outs.println("startDTD: " + name);
    }

    @Override
    public void startCDATA() throws SAXException {
        outs.println("startCDATA");
    }

    @Override
    public void endCDATA() throws SAXException {
        outs.println("startCDATA");
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        outs.println("startDTD: " + str);
    }

    public static void main(String[] args) {
        System.out.println("Char invalid xerces");
        try {
            Main p = new Main();
            p.setOuts(System.out);
            p.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
