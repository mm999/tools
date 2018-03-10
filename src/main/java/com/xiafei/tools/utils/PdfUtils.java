package com.xiafei.tools.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * <P>Description: 生成pdf工具. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/3/1</P>
 * <P>UPDATE DATE: 2018/3/1</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public class PdfUtils {
    public static void main(String[] args) throws Exception {
        File file = new File(".");
        final String[] list = file.list();
        System.out.println(Arrays.toString(list));
        parseHtmlToPdf();
    }

    /**
     * 使用 iText XML Worker实现HTML转PDF
     * itextpdf-5.5.6.jar
     *
     * @param fileName
     * @throws Exception
     */
    public static void parseHtmlToPdf() throws Exception {
        String htmlFile = "test.html";
        String pdfFile = "test.pdf";

        InputStream htmlFileStream = new FileInputStream(htmlFile);

        // 创建一个document对象实例
        Document document = new Document();
        // 为该Document创建一个Writer实例
        PdfWriter pdfwriter = PdfWriter.getInstance(document,
                new FileOutputStream(pdfFile));
        pdfwriter.setViewerPreferences(PdfWriter.HideToolbar);
        // 打开当前的document
        document.open();

        InputStreamReader isr = new InputStreamReader(htmlFileStream, "UTF-8");
        XMLWorkerHelper.getInstance().parseXHtml(pdfwriter, document, isr);
        document.close();
    }
}
