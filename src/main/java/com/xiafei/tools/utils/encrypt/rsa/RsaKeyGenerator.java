package com.xiafei.tools.utils.encrypt.rsa;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * <P>Description: RsaKey生成器. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/11/22</P>
 * <P>UPDATE DATE: 2017/11/22</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class RsaKeyGenerator {

    public static void main(String[] args) {
        genKeyPair();
    }
    /**
     * 随机生成密钥对
     */
    public static void genKeyPair() {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        try {
            System.out.println("PublicKey:");
            // 得到公钥字符串
            System.out.println(Base64.encodeBase64String(publicKey.getEncoded()));
            System.out.println("PrivateKey: Don't Show Others!!!");
            // 得到私钥字符串
            System.out.println(Base64.encodeBase64String(privateKey.getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
