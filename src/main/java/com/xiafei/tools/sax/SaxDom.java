package com.xiafei.tools.sax;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

/**
 * <P>Description: Sax解析Xml文档例子. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/18</P>
 * <P>UPDATE DATE: 2017/8/18</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class SaxDom {

    public static void main(String[] args) {
        final Document document;

        try {
            document = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "\n" +
                    "<service>\n" +
                    "  <array>\n" +
                    "  <struct/>\n" +
                    "  <struct/>\n" +
                    "  </array>\n" +
                    "</service>\n");
            List<Element> nodes = document.selectNodes("/service/array/struct");
            System.out.println(nodes.size());
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
}
