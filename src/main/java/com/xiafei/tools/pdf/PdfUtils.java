package com.xiafei.tools.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

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
        parseHtmlToPdf(new FileInputStream(new File("./temp/contract.vm")), new FileOutputStream(new File("./temp/test.pdf")));
    }

    /**
     * 使用 iText XML Worker实现HTML转PDF
     * itextpdf-5.5.6.jar
     *
     * @param htmlIn html输入流
     * @param pdfOut pdf输出流
     */
    public static void parseHtmlToPdf(final InputStream htmlIn, final OutputStream pdfOut) throws Exception {
        // 使用我们的字体提供器，并将其设置为unicode字体样式
        final MyFontsProvider fontProvider = new MyFontsProvider();
        fontProvider.addFontSubstitute("lowagie", "garamond");
        fontProvider.setUseUnicode(true);
        final CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        final HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

        // 声明文档
        final Document document = new Document();
        final PdfWriter pdfwriter = PdfWriter.getInstance(document, pdfOut);
        pdfwriter.setViewerPreferences(PdfWriter.HideToolbar);
        // open一定要在pdfwriter声明之后，否则不生效
        document.open();

        // 输出到文档
        XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        XMLWorkerHelper.getInstance().parseXHtml(pdfwriter, document, htmlIn, null, Charset.forName("UTF-8"), fontProvider);
        // close会将流刷新到文档，一定要有
        document.close();

    }

    /**
     * 重写 字符设置方法，解决中文乱码问题
     */
    private static class MyFontsProvider extends XMLWorkerFontProvider {

        private MyFontsProvider() {
            super(null, null);
        }

        @Override
        public Font getFont(final String fontname, String encoding, float size, final int style) {
            String fntname = fontname;
            if (fntname == null) {
                fntname = "宋体";
            }
            if (size == 0) {
                size = 4;
            }
            return super.getFont(fntname, encoding, size, style);
        }
    }
}
