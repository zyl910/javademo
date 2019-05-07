package org.zyl910.javademo.xml.charinvalidxerces;

import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.utils.XMLUtils;
import org.xml.sax.Attributes;
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

    /** 处理.
     */
    private void process() throws Exception {
        //String strxml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root>  <str>汉字ABC&amp;#16;.</str></root>";  // 不会解转义.
            //startElement: str
            //characters: 汉字ABC
            //startEntity: amp
            //characters: &
            //endEntity: amp
            //characters: #16
            //characters: ;.
            //endElement: str
        //String strxml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root>  <str>汉字ABC&amp;#16;&#16;.</str></root>"; // 异常. org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 71; Character reference "&#16" is an invalid XML character.
            //startElement: str
            //characters: 汉字ABC
            //startEntity: amp
            //characters: &
            //endEntity: amp
            //characters: #16
            //characters: ;
            //org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 71; Character reference "&#16" is an invalid XML character.
        //String strxml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root>  <str><![CDATA[汉字ABC&amp;#16;&#16;.]]></str></root>"; // 不会解转义. 会被提取为“C&amp;#16;&#16;”.
            //startElement: str
            //startCDATA
            //characters: 汉字AB
            //characters: C&amp;#16;&#16;.
            //endCDATA
            //endElement: str
        //String strxml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root>  <str>A&#160;B</str></root>";  // 正常转义.
            //characters: A
            //characters:  
            //characters: B
        String strxml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root>  <str>A&#31;B</str></root>";  // 异常. 虽然xml支持转义字符, 但若该字符属于xml非法字符范围内, 则会报异常.
            //startElement: str
            //characters: A
            //org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 58; Character reference "&#31" is an invalid XML character.
        InputSource is;
        is = new InputSource(new StringReader(strxml));
        //testAxis(is);
        testJaxp(is);
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

    @Override
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        outs.println("startDTD: " + name);
    }

    @Override
    public void endDTD() throws SAXException {
        outs.println("endDTD");
    }

    @Override
    public void startEntity(String name) throws SAXException {
        outs.println("startEntity: " + name);
    }

    @Override
    public void endEntity(String name) throws SAXException {
        outs.println("endEntity: " + name);
    }

    @Override
    public void startCDATA() throws SAXException {
        outs.println("startCDATA");
    }

    @Override
    public void endCDATA() throws SAXException {
        outs.println("endCDATA");
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        outs.println("comment: " + str);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        outs.println("startElement: " + qName);
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        outs.println("endElement: " + qName);
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        outs.println(String.format("characters(%d): %s", length, str));
        super.characters(ch, start, length);
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
