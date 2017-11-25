package com.xiafei.tools.image.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * <P>Description: 二维码生成器. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/11/25</P>
 * <P>UPDATE DATE: 2017/11/25</P>
 *
 * @author 齐霞飞
 * @version 1.0
 * @since java 1.7.0
 */
public class QrCodeGenerator {

    private QrCodeGenerator() {

    }

    public static void main(String[] args) {
        generateQRCode("http://www.baidu.com", 300, 300, "c:/baidu.jpg");
    }

    /**
     * 根据内容，生成指定宽高、指定格式的二维码图片
     *
     * @param content  内容
     * @param width    宽
     * @param height   高
     * @param fullPath 图片完整路径
     */
    private static void generateQRCode(final String content, int width, int height, String fullPath) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        File outputFile = new File(fullPath);
        try {
            final String format = fullPath.substring(fullPath.lastIndexOf(".") + 1);
            writeToFile(bitMatrix, format, outputFile);
            System.out.println("图片生成成功，路径:" + fullPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;


    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }


    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }


    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

}
