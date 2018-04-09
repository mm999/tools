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
        System.out.println(decryptByPriKey("Ht3NzZohSK61B4AhLbb4O43N7fpklz99+P49mTZlKNhG6I56SEHofzEhR3xWmJmPj2Om2h64tR5iBam/wQo3AZpXSOzFHoIt1Eqt5yKZispN+aS4K5zkfWMZ1fk9wXUC1YPZ0o1ISRm4LIzWkhyaQynx15UXPlGu8SVVTTPvSoY="));
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
            byte[] bytes = RSA.encryptByPublicKey(src.getBytes("UTF-8"), coopCode);
            return new String(Base64.getEncoder().encode(bytes), "UTF-8");
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
            byte[] bytes = RSA.decryptByPrikey(Base64.getDecoder().decode(encryptedStr.getBytes("UTF-8")));
            return new String(bytes, "UTF-8");
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
