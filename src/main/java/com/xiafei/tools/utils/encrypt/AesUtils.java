package com.xiafei.tools.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

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
     * 产生随机数的种子.
     */
    private static final String SEED = "SHA1PRNG";

    /**
     * 工具类不允许实例化.
     */
    private AesUtils() {

    }

    public static void main(String[] args) {
//        System.out.println(getEncrypted("liang131401234560123456789012345", "d85975de95974ebda7d34393218904fa"));
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
        return Base64.encodeBase64String(encrypt(source, key));
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
            return new String(decrypt(Base64.decodeBase64(source), key), CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
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
            final SecureRandom sr = new SecureRandom();
            final Key secureKey = getKey(key);
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
            final Key secretKey = getKey(key);
            final Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new SecureRandom());// 初始化
            return cipher.doFinal(source); // 解密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static Key getKey(String strKey) {
        try {
            if (strKey == null) {
                strKey = "";
            }
            KeyGenerator _generator = KeyGenerator.getInstance(ALGORITHM_NAME);
            SecureRandom secureRandom = SecureRandom.getInstance(SEED);
            secureRandom.setSeed(strKey.getBytes(CHARSET));
            _generator.init(128, secureRandom);
            return _generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(" 初始化密钥出现异常 ");
        }
    }

}
