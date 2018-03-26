package com.xiafei.tools.common.encrypt.rsa;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * <P>Description: RSA签名,加解密处理核心文件，注意：密钥长度1024 </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2013-11-15</P>
 * <P>UPDATE DATE: 2017/6/28</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
@Slf4j
class RSA {

    /**
     * 签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";
    /**
     * 1024密钥RSA加密最大分段大小.
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * 1024密钥RSA解密最大分段大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * 用私钥分段解密.
     *
     * @param encryptedData 已加密字节流
     * @return 明文字节流
     */
    static byte[] decryptByPrikey(final byte[] encryptedData) throws Exception {

        // 初始化密码操作器
        final Cipher cipher = Cipher.getInstance(RsaKeyUtil.getPrik().getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, RsaKeyUtil.getPrik());
        // 分段处理
        return segmentDeal(cipher, MAX_DECRYPT_BLOCK, encryptedData);
    }

    /**
     * 公钥分段加密.
     *
     * @param data    源数据字节流
     * @param propPre 配置文件中前缀
     */
    static byte[] encryptByPublicKey(byte[] data, String propPre) throws Exception {
        // 初始化key对象
        final PublicKey publicK = RsaKeyUtil.getPubK(propPre);
        // 初始化密码操作器
        final Cipher cipher = Cipher.getInstance(publicK.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        // 分段处理
        return segmentDeal(cipher, MAX_ENCRYPT_BLOCK, data);
    }

    /**
     * 签名字符串
     *
     * @param plain 需要签名的字符串
     */
    static String sign(String plain) throws Exception {

        PrivateKey privateK = RsaKeyUtil.getPrik();
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(getContentBytes(plain, "utf-8"));
        byte[] result = signature.sign();
        return Base64.encodeBase64String(result);

    }

    /**
     * 验证签名字符串
     *
     * @param sign    客户签名结果
     * @param plain   签名前参数
     * @param propPre 配置文件中前缀
     * @return 验签结果
     */
    static boolean verify(String sign, String plain, String propPre) throws Exception {
        PublicKey publicK = RsaKeyUtil.getPubK(propPre);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(getContentBytes(plain, "utf-8"));
        return signature.verify(Base64.decodeBase64(sign));


    }

    /**
     * 分段处理.
     *
     * @param cipher       密码操作对象
     * @param segmentLengh 分段大小
     * @param data         数据字节流
     * @return 处理后的字节流
     */
    private static byte[] segmentDeal(final Cipher cipher, final int segmentLengh, final byte[] data) throws Exception {
        final int inputLen = data.length;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // 对数据分段加密
            for (int offSet = 0; offSet < inputLen; offSet += segmentLengh) {
                final byte[] cache;
                if (inputLen >= segmentLengh + offSet) {
                    cache = cipher.doFinal(data, offSet, segmentLengh);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
            }
            return out.toByteArray();
        }
    }

    /**
     * 使用指定字符集将内容转成字节流.
     *
     * @param content 内容
     * @param charset 字符集名字
     * @return 字节流
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    public static void main(String[] args) {
        final String s = Base64.encodeBase64String("测测需要多少字符测测需要多少字符".getBytes());
        System.out.println(s);
        System.out.println(s.length());
    }

}
