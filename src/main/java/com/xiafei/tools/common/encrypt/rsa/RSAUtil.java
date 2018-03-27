package com.xiafei.tools.common.encrypt.rsa;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

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

    private static final String ALGORITHM = "RSA";

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
//        System.out.println(encryptByPubKey("123456", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGdOxalt/5zCMXxDueXPeHb42/1M03WCNV2gKyt6TFj6VVr1bFxRYHXz3R6vxne+7ayaA56IuXE8mM5zKlrsUjKSoEORqmvWZ78It2R+yqN1FSE0PjB62fhfbXIcxCUxrDuBvSRG12A7PhPKxR1ekWpVPPhZTo33vgNG2Czg4mgQIDAQAB"));
//        Properties sp = System.getProperties();
//        Enumeration e = sp.propertyNames();
//        while (e.hasMoreElements()) {
//            String key = (String) e.nextElement();
//            System.out.println(key + "=" + sp.getProperty(key));
//        }

//        System.out.println(sign("{\"applyNoList\":[\"2018032110372800275000100008\"],\"batchNo\":\"2018032110372800275000100009\",\"endDate\":\"20190321\",\"signedFilePath\":\"/files/apply/2018032110372800275000100008/APPLY_CONTRACT\",\"startDate\":\"20180321\",\"status\":\"6\"}"));
        // 初始化密码操作器
        final PublicKey pubKey = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decodeBase64("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5TZyGRaCucf7Ke+S9rqYB1rvUO8qyzZQLkMCpS1pxjvWA3BBr5wYj+LWYVBXoHao0da7Vqu5R9LlWXsjmsHWwZY0CTyIVSKDidPJZJRfitOqsHV1LUQ/jddMAbE4Om6DKc20cJTRnStR0Zo7TKx/LgmgI/DflXthoh6AbYq5umQIDAQAB")));
        final Cipher cipher = Cipher.getInstance(pubKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        // 分段处理
        byte[] data = Base64.decodeBase64("SFsFa60LC+Z9AwnwTgCaLYFHb4z4bXgZsl/I4xL9csm90Q+h8KDm03eGo0LxvT3v7lDygW5cZ8I5fhxIJCFHveuLkbQgNFxEotXwjEkjqrJGWKP3NW/Mz/WZ4H4IV9myPlI3yAFlORnbf0IP9eh6e0jn1YgoTKt8zi7PuLplK4E=");
        final int inputLen = data.length;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // 对数据分段加密
            for (int offSet = 0; offSet < inputLen; offSet += 128) {
                final byte[] cache;
                if (inputLen >= 128 + offSet) {
                    cache = cipher.doFinal(data, offSet, 128);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
            }
            System.out.println(new String(out.toByteArray(), "UTF-8"));
        }

    }

    /**
     * 用公钥加密.
     *
     * @param src     原内容，明文
     * @param propPre 配置文件中前缀
     * @return 加密后的Base64字符串
     */
    public static String encryptTemp(String src, String propPre) {

        try {

            byte[] bytes = RSA.encryptByPublicKey(src.getBytes("GBK"), propPre);
            return Base64.encodeBase64String(bytes);
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
    public static String decryptTemp(String encryptedStr) {
        try {
            byte[] bytes = RSA.decryptByPrikey(Base64.decodeBase64(encryptedStr));
            return new String(bytes, "GBK");
        } catch (Exception e) {
            log.error("RSA decrypt failure:encryptedStr={}", encryptedStr, e);
        }
        return null;
    }

    /**
     * 用公钥加密.
     *
     * @param src    原内容，明文
     * @param pubKey 公钥
     * @return 加密后的Base64字符串
     */
    public static String encryptByPubKey(String src, String pubKey) {

        try {
            byte[] bytes = RSA.encryptByPublicKey(src.getBytes("UTF-8"), pubKey);
            return Base64.encodeBase64String(bytes);
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
            byte[] bytes = RSA.decryptByPrikey(Base64.decodeBase64(encryptedStr));
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
     * @param sign    签名字符串
     * @param plain   签名前字符串
     * @param propPre 配置文件中前缀
     * @return true - 验签通过，false-验签未通过
     */
    public static boolean verify(final String sign, final String plain, final String propPre) {
        try {
            return RSA.verify(sign, plain, propPre);
        } catch (Exception e) {
            log.error("RSA verify failure:sign={},plain={},propPre={}", sign, plain, propPre, e);
        }
        return false;
    }
}
