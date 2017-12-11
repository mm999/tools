package com.xiafei.tools.file;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.PathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <P>Description: . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/11</P>
 * <P>UPDATE DATE: 2017/12/11</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public class FileReader {

    public static void main(String[] args) throws IOException {
        System.out.println(readFileToBase64("c:/baidu.jpg"));
    }
    public static String readFileToBase64(final String path) throws IOException {
        PathResource pathResource = new PathResource(path);
        InputStream is = pathResource.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8096];
        int remain = 0;
        while ((remain = is.read(buffer, 0, buffer.length)) > 0) {
            bos.write(buffer, 0, remain);
        }

        byte[] content = bos.toByteArray();
        return Base64.encodeBase64String(content);
    }
}
