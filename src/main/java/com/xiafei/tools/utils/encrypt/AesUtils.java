package com.xiafei.tools.utils.encrypt;

import com.xiafei.tools.utils.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
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
     * 工具类不允许实例化.
     */
    private AesUtils() {

    }

    /**
     * 获取加密后的字符串.
     *
     * @param source 要加密的内容，明文
     * @param key    密钥，调用者自己保存
     * @return 使用密钥加密的字符串
     */
    public static String getEncrypted(final String source, final String key) {
        return StringUtils.bytes2HexStr(encrypt(source, key));
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
            return new String(decrypt(StringUtils.hexStr2Bytes(source), key), CHARSET);
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
            // 创建AES的Key生产者
            final KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM_NAME);
            // 利用用户密码作为随机数初始化出key生产者,SecureRandom是生成安全随机数序列，
            // password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
            kgen.init(KEY_BITS, new SecureRandom(key.getBytes(CHARSET)));
            // 跟据用户密码，生成一个密钥
            final SecretKey secretKey = kgen.generateKey();
            // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回null
            final byte[] enCodeFormat = secretKey.getEncoded();
            // 转换为AES专用密钥
            final SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, ALGORITHM_NAME);
            // 创建密码器
            final Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            // 加密并返回结果
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
            final KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM_NAME);
            kgen.init(KEY_BITS, new SecureRandom(key.getBytes(CHARSET)));
            final SecretKey secretKey = kgen.generateKey();
            final byte[] enCodeFormat = secretKey.getEncoded();
            final SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, ALGORITHM_NAME);
            final Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);// 初始化
            return cipher.doFinal(source); // 解密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
