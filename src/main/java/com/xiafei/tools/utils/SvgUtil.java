package com.xiafei.tools.utils;

import com.itextpdf.text.pdf.codec.Base64;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * <P>Description: . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/3/20</P>
 * <P>UPDATE DATE: 2018/3/20</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public class SvgUtil {


    public static void main(String[] args) throws IOException, TranscoderException {

    }

    /**
     * 将svg字符串转换为png图片作为字节流返回.
     *
     * @throws TranscoderException svg代码异常
     */
    public static byte[] convertToPng(final String svgBase64) throws TranscoderException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] bytes = Base64.decode(svgBase64);
        PNGTranscoder t = new PNGTranscoder();
        TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(bytes));
        TranscoderOutput output = new TranscoderOutput(result);
        t.transcode(input, output);
        return result.toByteArray();
    }
}
