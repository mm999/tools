package com.xiafei.tools.common.encrypt;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * <P>Description: Aes加密解密工具. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/11/20</P>
 * <P>UPDATE DATE: 2017/11/20</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@Slf4j
public class AesUtils {

    /**
     * 密钥位数.
     */
    private static final int KEY_BITS = 128;
    /**
     * 算法名字.
     */
    private static final String ALGORITHM_NAME = "AES";
    /**
     * 编码格式。
     */
    private static final String CHARSET = "utf-8";
    /**
     * AES私钥，不要给别人看哦.
     */
    public static final String AES_PRI_KEY = "d85975de95974ebda7d34393218904fa";
    /**
     * 产生随机数的种子.
     */
    private static final String SEED = "SHA1PRNG";

    /**
     * 工具类不允许实例化.
     */
    private AesUtils() {

    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(getEncrypted("024334", AES_PRI_KEY));
        String a = "g48jRUhGea9OKCBWeesq0A==";
//        System.out.println(getDecrypted("iZeIsBY2Fc/W/hIKUliRhuWBIpeHrwz4mk+IDcWW4hp+JjXQYot4XpVfblqeRrFV", "d85975de95974ebda7d34393218904fa"));
    }

    /**
     * 获取加密后的字符串.
     *
     * @param source 要加密的内容，明文
     * @param key    密钥，调用者自己保存
     * @return 使用密钥加密的字符串
     */
    public static String getEncrypted(final String source, final String key) {
        try {
            return new String(Base64.getEncoder().encode(encrypt(source, key)), CHARSET);
        } catch (UnsupportedEncodingException e) {
            log.error("Aes加密编码错误,字符串={}", source);
            throw new RuntimeException("Aes加密编码错误");
        }
    }

    /**
     * 通过加密后的字符串和密钥解密成明文.
     *
     * @param source 要解密的使用本工具类加密的密码串
     * @param key    密钥，调用者自己保存
     * @return 密码串加密前的样子
     */
    public static String getDecrypted(final String source, final String key) {
        try {
            final Base64.Decoder decoder = Base64.getDecoder();
            return new String(decrypt(decoder.decode(source.getBytes(CHARSET)), key), CHARSET);
        } catch (UnsupportedEncodingException e) {
            log.error("Aes解密编码错误,字符串={}", source);
            throw new RuntimeException("Aes解密编码错误");
        }
    }


    /**
     * 对内容加密.
     *
     * @param source 需要加密的内容
     * @param key    加密钥匙
     * @return 加密后的内容字节流
     */
    private static byte[] encrypt(final String source, final String key) {
        if (source == null || key == null || source.length() == 0 || key.length() == 0) {
            return null;
        }
        try {
            SecureRandom sr = SecureRandom.getInstance(SEED);
            sr.setSeed(key.getBytes(CHARSET));
            final Key secureKey = getKey(sr);
            final Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);
            return cipher.doFinal(source.getBytes(CHARSET));

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对内容解密.
     *
     * @param source 待解密内容
     * @param key    加密钥匙
     * @return 对内容解密后的字节流
     */
    private static byte[] decrypt(final byte[] source, final String key) {
        if (source == null || key == null || source.length == 0 || key.length() == 0) {
            return null;
        }
        try {
            SecureRandom sr = SecureRandom.getInstance(SEED);
            sr.setSeed(key.getBytes(CHARSET));
            final Key secretKey = getKey(sr);
            final Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);// 初始化
            return cipher.doFinal(source); // 解密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Key getKey(SecureRandom sr) {
        try {

            KeyGenerator _generator = KeyGenerator.getInstance(ALGORITHM_NAME);
            _generator.init(128, sr);
            return _generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(" 初始化密钥出现异常 ");
        }
    }

}
