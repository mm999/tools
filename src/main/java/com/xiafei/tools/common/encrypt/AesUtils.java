package com.xiafei.tools.common.encrypt;

import com.xiafei.tools.common.CommonConst;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
     * key算法名字.
     */
    private static final String KEY_ALGORITHM_NAME = "AES";
    /**
     * cipher算法.
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    /**
     * 编码格式。
     */
    private static final String CHARSET = "utf-8";
    /**
     * 随机数种子，相当于私钥，不要给别人看哦.
     */
    public static byte[] RANDOM_SEED;

    static {
        try {
            RANDOM_SEED = "d85975de95974ebda7d34393218904fa".getBytes(CommonConst.COMMON_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机算法.
     */
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";

    /**
     * 工具类不允许实例化.
     */
    private AesUtils() {

    }

    public static void main(String[] args) {
        final String encrypted = getEncrypted("024334", RANDOM_SEED);
        System.out.println("加密后：" + encrypted);
        final String decry = getDecrypted(encrypted, RANDOM_SEED);
        System.out.println("解密后：" + decry);
        System.out.println(getEncrypted("024334", RANDOM_SEED));
        String a = "g48jRUhGea9OKCBWeesq0A==";
//        System.out.println(getDecrypted("iZeIsBY2Fc/W/hIKUliRhuWBIpeHrwz4mk+IDcWW4hp+JjXQYot4XpVfblqeRrFV", "d85975de95974ebda7d34393218904fa"));
    }

    /**
     * 获取加密后的字符串.
     *
     * @param source 要加密的内容，明文
     * @param seed   密钥，调用者自己保存
     * @return 使用密钥加密的字符串
     */
    public static String getEncrypted(final String source, final byte[] seed) {
        return Base64.getEncoder().encodeToString(encrypt(source, seed));
    }

    /**
     * 通过加密后的字符串和密钥解密成明文.
     *
     * @param source 要解密的使用本工具类加密的密码串
     * @param seed   密钥，调用者自己保存
     * @return 密码串加密前的样子
     */
    public static String getDecrypted(final String source, final byte[] seed) {
        try {
            final Base64.Decoder decoder = Base64.getDecoder();
            return new String(decrypt(decoder.decode(source), seed), CHARSET);
        } catch (UnsupportedEncodingException e) {
            log.error("Aes解密编码错误,字符串={}", source);
            throw new RuntimeException("Aes解密编码错误");
        }
    }


    /**
     * 对内容加密.
     *
     * @param source 需要加密的内容
     * @param seed   加密钥匙
     * @return 加密后的内容字节流
     */
    private static byte[] encrypt(final String source, final byte[] seed) {
        if (source == null || seed == null || source.length() == 0 || seed.length == 0) {
            return null;
        }
        try {
            final Key secureKey = getKey(getSr(seed));
            final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, getSr(seed));
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
     * @param seed   加密钥匙
     * @return 对内容解密后的字节流
     */
    private static byte[] decrypt(final byte[] source, final byte[] seed) {
        if (source == null || seed == null || source.length == 0 || seed.length == 0) {
            return null;
        }
        try {
            final Key secretKey = getKey(getSr(seed));
            final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, secretKey, getSr(seed));// 初始化
            return cipher.doFinal(source); // 解密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Key getKey(SecureRandom sr) {
        try {

            final KeyGenerator _generator = KeyGenerator.getInstance(KEY_ALGORITHM_NAME);
            _generator.init(128, sr);
            final SecretKey origin_key = _generator.generateKey();
            final SecretKeySpec key = new SecretKeySpec(origin_key.getEncoded(), KEY_ALGORITHM_NAME);
            return key;
        } catch (Exception e) {
            throw new RuntimeException(" 初始化密钥出现异常 ");
        }
    }

    private static SecureRandom getSr(final byte[] seed) throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance(RANDOM_ALGORITHM);
        sr.setSeed(seed);
        return sr;
    }

}
