package com.xiafei.tools.common.encrypt;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * <P>Description: . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/4/17</P>
 * <P>UPDATE DATE: 2018/4/17</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Slf4j
public class Md5Util {

    private static final String ALGORITHM = "MD5";

    /**
     * md5加密.
     *
     * @param plain 明文串
     * @return md5编码后的base64串
     */
    public static String encrypt(final String plain) {
        try {
            return Base64.getEncoder().encodeToString(MessageDigest.getInstance(ALGORITHM).
                    digest(plain.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException e) {
            log.error("md5加密,获取md5算法失败", e);
        } catch (UnsupportedEncodingException e) {
            log.error("md5加密,原串转字节流失败", e);
        }
        return null;
    }

}
