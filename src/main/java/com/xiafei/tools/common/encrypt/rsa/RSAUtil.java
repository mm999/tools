package com.xiafei.tools.common.encrypt.rsa;

import com.xiafei.tools.common.CommonConst;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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

    private static AtomicInteger FLAG = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        boolean exception = false;
        Random r = new Random();
        for (int i = 0; i < 100000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (FLAG.get() == 1) {
                        return;
                    }
                    final String ori = String.valueOf(r.nextLong());
                    final byte[] oriBy;
                    try {
                        oriBy = ori.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return;
                    }
                    System.out.println("原串[" + ori + "]");
                    System.out.println("原比特" + Arrays.toString(oriBy));
                    final byte[] encryptBy;
                    try {
                        encryptBy = RSA.encryptByPublicKey(oriBy, "pt");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    System.out.println("加密比特" + Arrays.toString(encryptBy));
                    final String encryBase64 = Base64.getEncoder().encodeToString(encryptBy);
                    final byte[] encryBase64By;
                    try {
                        encryBase64By = encryBase64.getBytes("ISO-8859-1");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return;
                    }
                    System.out.println("加密base64串[" + encryBase64 + "]");
                    System.out.println("加密base64比特" + Arrays.toString(encryBase64By));
                    final byte[] decryBase64By = Base64.getDecoder().decode(encryBase64);
                    System.out.println("解密base64比特" + Arrays.toString(decryBase64By));
                    final byte[] decryBy;
                    try {
                        decryBy = RSA.decryptByPrikey(decryBase64By);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    System.out.println("解密比特" + Arrays.toString(decryBy));
                    final String decry;
                    try {
                        decry = new String(decryBy, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return;
                    }
                    System.out.println("解密串[" + decry + "]");
                    if (oriBy.length != decryBy.length) {
                        FLAG.set(1);
                        throw new RuntimeException();
                    }
                    if (!ori.equals(decry)) {
                        FLAG.set(1);
                        throw new RuntimeException();
                    }
                }
            }).start();

        }


        if (FLAG.get() == 1) {
            return;
        }
        final String ori = String.valueOf(r.nextLong());
        final byte[] oriBy;
        try {
            oriBy = ori.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("原串[" + ori + "]");
        System.out.println("原比特" + Arrays.toString(oriBy));
        final byte[] encryptBy;
        try {
            encryptBy = RSA.encryptByPublicKey(oriBy, "pt");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("加密比特" + Arrays.toString(encryptBy));
        final String encryBase64 = Base64.getEncoder().encodeToString(encryptBy);
        final byte[] encryBase64By;

        try {
            encryBase64By = encryBase64.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("加密base64串[" + encryBase64 + "]");
        System.out.println("加密base64比特" + Arrays.toString(encryBase64By));
        final byte[] decryBase64By = Base64.getDecoder().decode(encryBase64);
        System.out.println("解密base64比特" + Arrays.toString(decryBase64By));
        final byte[] decryBy;
        try {
            decryBy = RSA.decryptByPrikey(decryBase64By);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("解密比特" + Arrays.toString(decryBy));

        final String decry;
        try {
            decry = new String(decryBy, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("解密串[" + decry + "]");
        if (oriBy.length != decryBy.length) {
            FLAG.set(1);
            throw new RuntimeException();
        }
        if (!ori.equals(decry)) {
            FLAG.set(1);
            throw new RuntimeException();
        }
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
            byte[] bytes = RSA.encryptByPublicKey(src.getBytes(CommonConst.ENCRYPT_CHARSET), coopCode);
            return Base64.getEncoder().encodeToString(bytes);
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
            byte[] bytes = RSA.decryptByPrikey(Base64.getDecoder().decode(encryptedStr));
            return new String(bytes, CommonConst.ENCRYPT_CHARSET);
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
