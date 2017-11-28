package com.xiafei.tools.utils.encrypt.rsa;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.util.UUID;

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
//        System.out.println(encryptByPubKey("liang1314","MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGdOxalt/5zCMXxDueXPeHb42/1M03WCNV2gKyt6TFj6VVr1bFxRYHXz3R6vxne+7ayaA56IuXE8mM5zKlrsUjKSoEORqmvWZ78It2R+yqN1FSE0PjB62fhfbXIcxCUxrDuBvSRG12A7PhPKxR1ekWpVPPhZTo33vgNG2Czg4mgQIDAQAB"));
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
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

}
