package com.xiafei.tools.common.encrypt.rsa;

import com.xiafei.tools.common.BundleUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <P>Description: RsaKey工具，因为加载key是比较重量的操作， 所以采用池且懒加载的方式保存安全信息. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/2/1</P>
 * <P>UPDATE DATE: 2018/2/1</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Slf4j
final class RsaKeyUtil {

    // 加密算法
    private static final String ALGORITHM = "RSA";


    // Rsa密钥钥缓存池
    private static final ConcurrentHashMap<String, Key> KEY_MAP = new ConcurrentHashMap<>();
    // rsa配置文件名
    private static final String PROPERTIES_FILE_NAME = "rsa";
    // 平台私钥在配置文件和缓存池中的key
    private static final String PRIVATE_KEY_MAP_KEY = "pt_pri";
    // 平台公钥在配置文件和缓存池中的key后缀
    private static final String PUB_KEY_SUFFIX = "_pub";

    static {
        try {
            loadPrik();
            log.info("平台私钥加载成功");
        } catch (Throwable e) {
            log.error("平台私钥加载失败，请查找原因");
        }
    }

    /**
     * 工具类构造函数私有化.
     */
    private RsaKeyUtil() {
    }

    /**
     * 获取平台私钥.
     *
     * @return 私钥
     * @throws NoSuchAlgorithmException 找不到此算法异常
     * @throws InvalidKeySpecException  私钥格式校验失败
     */
    public static PrivateKey getPrik() throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException {
        if (KEY_MAP.containsKey(PRIVATE_KEY_MAP_KEY)) {
            return (PrivateKey) KEY_MAP.get(PRIVATE_KEY_MAP_KEY);
        } else {
            synchronized (KEY_MAP) {
                if (KEY_MAP.containsKey(PRIVATE_KEY_MAP_KEY)) {
                    return (PrivateKey) KEY_MAP.get(PRIVATE_KEY_MAP_KEY);
                } else {
                    loadPrik();
                    return (PrivateKey) KEY_MAP.get(PRIVATE_KEY_MAP_KEY);
                }
            }
        }
    }

    /**
     * 根据合作方编码拿到对接系统的公钥.
     *
     * @param coopCode 合作方编码
     * @return 公钥
     * @throws NoSuchAlgorithmException 找不到此算法异常
     * @throws InvalidKeySpecException  私钥格式校验失败
     */
    public static PublicKey getPubK(String coopCode) throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException {
        final String key = coopCode.concat(PUB_KEY_SUFFIX);
        if (KEY_MAP.containsKey(key)) {
            return (PublicKey) KEY_MAP.get(key);
        } else {
            synchronized (KEY_MAP) {
                if (KEY_MAP.containsKey(key)) {
                    return (PublicKey) KEY_MAP.get(key);
                } else {
                    loadPubK(key);
                    return (PublicKey) KEY_MAP.get(key);

                }
            }
        }
    }

    /**
     * 加载私钥.
     *
     * @throws NoSuchAlgorithmException 找不到此算法异常
     * @throws InvalidKeySpecException  私钥格式校验失败
     */
    private static void loadPrik() throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        final String priKStr = BundleUtil.instance(PROPERTIES_FILE_NAME).getString(PRIVATE_KEY_MAP_KEY);
        final PrivateKey priKey = KeyFactory.getInstance(ALGORITHM).
                generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(priKStr.getBytes("UTF-8"))));
        KEY_MAP.putIfAbsent(PRIVATE_KEY_MAP_KEY, priKey);
    }

    /**
     * 加载公钥.
     *
     * @throws NoSuchAlgorithmException 找不到此算法异常
     * @throws InvalidKeySpecException  公钥格式校验失败
     */
    private static void loadPubK(final String key) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        final String pubKStr = BundleUtil.instance(PROPERTIES_FILE_NAME).getString(key);
        final PublicKey pubKey = KeyFactory.getInstance(ALGORITHM).
                generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(pubKStr.getBytes("UTF-8"))));
        KEY_MAP.putIfAbsent(key, pubKey);
    }

}
