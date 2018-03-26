package com.xiafei.tools.barcode.qr;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <P>Description: 二维码解析. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/11/25</P>
 * <P>UPDATE DATE: 2017/11/25</P>
 *
 * @author 齐霞飞
 * @version 1.0
 * @since java 1.7.0
 */
public class QrCodeResolver {

    private static final String CHAR_SET = "utf-8";

    private QrCodeResolver() {

    }

    public static void main(String[] args) throws IOException {
        InputStream is = new FileInputStream(new File("c:/new.png"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int bufferLen = 1024 << 3;
        byte[] buffer = new byte[bufferLen];
        int remain;
        while ((remain = is.read(buffer, 0, bufferLen)) > 0) {
            bos.write(buffer, 0, remain);
        }
        resolveFromBytes(bos.toByteArray());
    }

    /**
     * 解析字节流中的二维码图片.
     *
     * @param bytes 字节流
     * @return 二维码中包含的信息
     */
    public static String resolveFromBytes(final byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes, 0, bytes.length);
        return resolveFromStream(bis);
    }

    /**
     * 解析流中的二维码图片.
     *
     * @param inputStream 二维码图片信息流
     * @return 二维码信息
     */
    public static String resolveFromStream(final InputStream inputStream) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            return resolveImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解析电脑上的二维码图片.
     *
     * @param path 二维码文件全路径
     * @return 二维码信息
     */
    public static String resolveFromPath(final String path) {
        try {
            File file = new File(path);
            BufferedImage image = ImageIO.read(file);
            return resolveImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String resolveImage(BufferedImage image) throws NotFoundException {
        MultiFormatReader formatReader = new MultiFormatReader();
        LuminanceSource source = new QrCodeResolveHelper(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        Map<DecodeHintType, String> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, CHAR_SET);
        return formatReader.decode(binaryBitmap, hints).toString();
    }

}
