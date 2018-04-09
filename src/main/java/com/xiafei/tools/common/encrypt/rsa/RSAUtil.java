package com.xiafei.tools.common.encrypt.rsa;

import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

/**
 * <P>Description: RSA工具类 </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/5/27</P>
 * <P>UPDATE DATE: 2017/6/28</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
@Slf4j
public class RSAUtil {


    public static void main(String[] args) {
//        System.out.println(encryptByPubKey("123456", "pt"));
        System.out.println(decryptByPriKey("Y1yRJsUEaKUQevu5/Mr2vBmbKO+vjYyeloJd8uQiGD39DFLCVc76KyRhiR4tZzdyBrmPrH38ACvN2VirP1aBPkp9FjKhyBZ88PQGFX6CSEg4Ar6jQiYo36JVheN7FcGkHaybWm415qQMM9WwppkdZrCD3j0TXYBAVUFJcE2ZUt0="));
//        Properties sp = System.getProperties();
//        Enumeration e = sp.propertyNames();
//        while (e.hasMoreElements()) {
//            String key = (String) e.nextElement();
//            System.out.println(key + "=" + sp.getProperty(key));
//        }

    }

    /**
     * 用公钥加密.
     *
     * @param src      原内容，明文
     * @param coopCode 合作方编码
     * @return 加密后的Base64字符串
     */
    public static String encryptByPubKey(String src, String coopCode) {

        try {
            byte[] bytes = RSA.encryptByPublicKey(src.getBytes("GBK"), coopCode);
            return new String(Base64.getEncoder().encode(bytes), "utf-8");
        } catch (Exception e) {
            log.error("RSA encrypt failure:src={}", src, e);
        }
        return null;
    }

    /**
     * 使用私钥解密.
     *
     * @param encryptedStr 已经加密的内容Base64字符串
     * @return 明文
     */
    public static String decryptByPriKey(String encryptedStr) {
        try {
            byte[] bytes = RSA.decryptByPrikey(Base64.getDecoder().decode(encryptedStr.getBytes("utf-8")));
            return new String(bytes, "GBK");
        } catch (Exception e) {
            log.error("RSA decrypt failure:encryptedStr={}", encryptedStr, e);
        }
        return null;
    }


    /**
     * 加签.
     *
     * @param plain 加签前字符串
     * @return 加签后字符串
     */
    public static String sign(final String plain) {
        try {
            return RSA.sign(plain);
        } catch (Exception e) {
            log.error("RSA sign failure:plain={}", plain, e);
        }
        return null;
    }

    /**
     * 验签.
     *
     * @param sign     签名字符串
     * @param plain    签名前字符串
     * @param coopCode 合作方编码
     * @return true - 验签通过，false-验签未通过
     */
    public static boolean verify(final String sign, final String plain, final String coopCode) {
        try {
            return RSA.verify(sign, plain, coopCode);
        } catch (Exception e) {
            log.error("RSA verify failure:sign={},plain={},coopCode={}", sign, plain, coopCode, e);
        }
        return false;
    }
}
