package com.xiafei.tools.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.xiafei.tools.front.CodeVelocityResolver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

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
//        File file = new File(".");
//        final String[] list = file.list();
//        System.out.println(Arrays.toString(list));
//        parseHtmlToPdf();
        test();
    }


    public static void test() throws IOException {

        // 1、将用户签名上传到一个本地临时目录
        final String tempNo = "heihei";
        final String folder = "temp/".concat(tempNo).concat("/");
        final String filePath = folder.concat("sign.jpg");
        final File folderFile = new File(folder);
        folderFile.mkdirs();
        final File tempFile = new File(filePath);
        final String tempPath = tempFile.getAbsolutePath();

        try (FileOutputStream fos = new FileOutputStream(tempFile);
             InputStream is = new FileInputStream(new File("./temp/人脸1.jpg"));
        ) {
            byte[] buffer = new byte[2048];
            int remain;
            while ((remain = is.read(buffer, 0, 2048)) > 0) {
                fos.write(buffer, 0, remain);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2、替换模板文件中的字段、签名图片
        final Map<String, Object> model = new HashMap<>();
        final Map<String, Object> dataMap = new HashMap<>();
        model.put("data", dataMap);
        dataMap.put("applyNo", "applyno");
        dataMap.put("cardImgPath", tempPath);
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(new File("./temp/contract.vm"));
        byte[] buffer = new byte[2048];
        int remain;
        while ((remain = fis.read(buffer, 0, 2048)) > 0) {
            bos1.write(buffer, 0, remain);
        }

        final byte[] tpltBytes = bos1.toByteArray();
        fis.close();
        bos1.close();
        final String contractHtml;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(tpltBytes);
        ) {
            contractHtml = CodeVelocityResolver.parse(bis, model);
        }

        System.out.println("替换后html是：" + contractHtml);
        // 3、将html转换成pdf，上传到本地服务器和金租服务器，然后删除临时文件
        Document document = new Document();
        try (FileOutputStream bos = new FileOutputStream(new File("./temp/xixi.pdf"));
             ByteArrayInputStream bis = new ByteArrayInputStream(contractHtml.getBytes("utf-8"))

        ) {
            final PdfWriter pdfwriter = PdfWriter.getInstance(document, bos);
            pdfwriter.setViewerPreferences(PdfWriter.HideToolbar);
            document.open();
            // 使用我们的字体提供器，并将其设置为unicode字体样式
            MyFontsProvider fontProvider = new MyFontsProvider();
            fontProvider.addFontSubstitute("lowagie", "garamond");
            fontProvider.setUseUnicode(true);
            CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
            XMLWorkerHelper.getInstance().parseXHtml(pdfwriter, document, bis, null, Charset.forName("UTF-8"), fontProvider);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                tempFile.delete();
                folderFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * 重写 字符设置方法，解决中文乱码问题
     */
    public static class MyFontsProvider extends XMLWorkerFontProvider {

        public MyFontsProvider() {
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
