package com.xiafei.tools.common.encrypt.rsa;

import com.xiafei.tools.common.CommonConst;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;

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
    // cipher 算法
    private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

    /**
     * 秘钥位数.
     */
    private static final int KEY_SIZE = 1024;

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
        final RSAPrivateKey prik = RsaKeyUtil.getPrik();
        final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, prik);
        // 分段处理
        return segmentDeal(cipher, MAX_DECRYPT_BLOCK, encryptedData);
    }

    /**
     * 公钥分段加密.
     *
     * @param data     源数据字节流
     * @param coopCode 合作方编码
     */
    static byte[] encryptByPublicKey(byte[] data, String coopCode) throws Exception {
        // 初始化key对象
        final PublicKey publicK = RsaKeyUtil.getPubK(coopCode);
        // 初始化密码操作器
        final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
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
        signature.update(getContentBytes(plain, CommonConst.COMMON_CHARSET));
        byte[] result = signature.sign();
        return Base64.getEncoder().encodeToString(result);

    }

    /**
     * 验证签名字符串
     *
     * @param sign     客户签名结果
     * @param plain    签名前参数
     * @param coopCode 合作方编码
     * @return 验签结果
     */
    static boolean verify(String sign, String plain, String coopCode) throws Exception {
        PublicKey publicK = RsaKeyUtil.getPubK(coopCode);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(getContentBytes(plain, CommonConst.COMMON_CHARSET));
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * 分段处理.
     *
     * @param cipher    密码操作对象
     * @param blockSize 分段大小
     * @param data      数据字节流
     * @return 处理后的字节流
     */
    private static byte[] segmentDeal(final Cipher cipher, final int blockSize, final byte[] data) throws Exception {
        final int dataLength = data.length;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(KEY_SIZE)) {
            // 对数据分段加密或解密
            for (int offSet = 0; offSet < dataLength; offSet += blockSize) {
                final byte[] cache;
                if (offSet + blockSize < dataLength) {
                    cache = cipher.doFinal(data, offSet, blockSize);
                } else {
                    cache = cipher.doFinal(data, offSet, dataLength - offSet);

                }
                out.write(cache);
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
    }

}
