package com.xiafei.tools.common.encrypt.rsa;

import com.virgo.finance.lease.core.common.CommonConst;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Base64;

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


    public static void main(String[] args) throws Exception {
//        final byte[] ori = "123456".getBytes("ISO-8859-1");
//        System.out.println(Arrays.toString(ori));
//        final byte[] pts = RSA.encryptByPublicKey(ori, "pt");
//
//        System.out.println(Base64.getEncoder().encodeToString(pts));
        final byte[] pts = Base64.getDecoder().decode("NZqVJBdx6H/Z3CPPBnEoar9liLqJKgcfrOCyItuY3JPcp+ka1thL7+H0PYwyOFljlq6e33B/AocS4pyeDO1ey7pAxaP9u/Zs3rtcokRGTaaBS+vd1IUIkxbGplYkIfE0i28Hp3ycZn9YbQrGcUroxia7VOyOTwy3i7hX5bFEjNQ\u003d");
                System.out.println(Arrays.toString(pts));
        System.out.println(Arrays.toString(RSA.decryptByPrikey(pts)));
//        System.out.println(AesUtils.getEncrypted(decryptByPriKey("DUJcnwNHd8eD6Y/vjQeChl3rR9u/iL1Pz9MT2qWquQRmsVvC43mpKsOpvDqTb+DWJRZr+81b2Iyjl0IrrEM3z3VO4EDRAbGvOL/4OW5qnd3GRqpCNANtJsGppPotAv/LF2mD16HaCV2XL5O4OkyvfMeK5hTcRFGW0nE46qMcGrQ="), AesUtils.RANDOM_SEED));
//        System.out.println("结束");
        //        Properties sp = System.getProperties();
//        Enumeration e = sp.propertyNames();
//        while (e.hasMoreElements()) {
//            String key = (String) e.nextElement();
//            System.out.println(key + "=" + sp.getProperty(key));
//        }

//        System.out.println(sign("{\"infoList\":[{\"acctType\":\"0\",\"applyNo\":\"2018041114214100797C0A882DF00017\",\"bankNo\":\"jRS+7pejNHqpJKEtaUc8LH/vc9fwsRC69NJDrzNrRpVQDLiR5cIIFwGqxZDyhjXPRmoC9Yu3yeQnxKU5FqmEFmUE2qV7hHwxdGgALZWRHv0tjbYesEhguM7ZYTSek1duzrIE9syT6iYIx/9yAB5wzntFmFO2siEV7FJBDjLQiFw\\u003d\",\"deductNo\":\"201804121308001900000001\",\"deductPeriodList\":[{\"period\":\"2\",\"subjectList\":[{\"amount\":\"40000\",\"subject\":\"0\"}]}],\"period\":\"12\",\"planList\":[{\"dueDate\":\"20180411\",\"endDate\":\"20180411\",\"normalAmount\":\"120142\",\"period\":\"1\",\"repayDate\":\"20180411\",\"startDate\":\"20180411\",\"status\":\"1\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20180511\",\"endDate\":\"20180511\",\"normalAmount\":\"240284\",\"period\":\"2\",\"repayDate\":\"20180511\",\"startDate\":\"20180511\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20180611\",\"endDate\":\"20180611\",\"normalAmount\":\"360426\",\"period\":\"3\",\"repayDate\":\"20180611\",\"startDate\":\"20180611\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20180711\",\"endDate\":\"20180711\",\"normalAmount\":\"480568\",\"period\":\"4\",\"repayDate\":\"20180711\",\"startDate\":\"20180711\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20180811\",\"endDate\":\"20180811\",\"normalAmount\":\"600710\",\"period\":\"5\",\"repayDate\":\"20180811\",\"startDate\":\"20180811\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20180911\",\"endDate\":\"20180911\",\"normalAmount\":\"720852\",\"period\":\"6\",\"repayDate\":\"20180911\",\"startDate\":\"20180911\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20181011\",\"endDate\":\"20181011\",\"normalAmount\":\"840994\",\"period\":\"7\",\"repayDate\":\"20181011\",\"startDate\":\"20181011\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20181111\",\"endDate\":\"20181111\",\"normalAmount\":\"961136\",\"period\":\"8\",\"repayDate\":\"20181111\",\"startDate\":\"20181111\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20181211\",\"endDate\":\"20181211\",\"normalAmount\":\"1081278\",\"period\":\"9\",\"repayDate\":\"20181211\",\"startDate\":\"20181211\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20190111\",\"endDate\":\"20190111\",\"normalAmount\":\"1201420\",\"period\":\"10\",\"repayDate\":\"20190111\",\"startDate\":\"20190111\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20190211\",\"endDate\":\"20190211\",\"normalAmount\":\"1321562\",\"period\":\"11\",\"repayDate\":\"20190211\",\"startDate\":\"20190211\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20190311\",\"endDate\":\"20190311\",\"normalAmount\":\"1441700\",\"period\":\"12\",\"repayDate\":\"20190311\",\"startDate\":\"20190311\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120138\",\"status\":\"1\",\"subject\":\"0\"}]}],\"repayDate\":\"20180412\",\"serialNo\":\"2018041100000000006\"}]}"));
//        System.out.printf(String.valueOf(verify("BDUEra2qv6gYEj9VAJLw1PwayeU3b8/LphobLsRcyLIf4UNxuPXq5PfallH5QYmyWT0fzIPydzxDGaCAjpiJJlV5u8P3jEimWgNcg1mr01GC+O2nE4fOxFaCzO1QsSxg8z8GGuK11z6PkidV0bgEMrDvUZ+FtOPJBGORqkDdw3g=",
//                "{\"infoList\":[{\"acctType\":\"0\",\"applyNo\":\"2018041114214100797C0A882DF00017\",\"bankNo\":\"jRS+7pejNHqpJKEtaUc8LH/vc9fwsRC69NJDrzNrRpVQDLiR5cIIFwGqxZDyhjXPRmoC9Yu3yeQnxKU5FqmEFmUE2qV7hHwxdGgALZWRHv0tjbYesEhguM7ZYTSek1duzrIE9syT6iYIx/9yAB5wzntFmFO2siEV7FJBDjLQiFw\\u003d\",\"deductNo\":\"201804121308001900000001\",\"deductPeriodList\":[{\"period\":\"2\",\"subjectList\":[{\"amount\":\"40000\",\"subject\":\"0\"}]}],\"period\":\"12\",\"planList\":[{\"dueDate\":\"20180411\",\"endDate\":\"20180411\",\"normalAmount\":\"120142\",\"period\":\"1\",\"repayDate\":\"20180411\",\"startDate\":\"20180411\",\"status\":\"1\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20180511\",\"endDate\":\"20180511\",\"normalAmount\":\"240284\",\"period\":\"2\",\"repayDate\":\"20180511\",\"startDate\":\"20180511\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20180611\",\"endDate\":\"20180611\",\"normalAmount\":\"360426\",\"period\":\"3\",\"repayDate\":\"20180611\",\"startDate\":\"20180611\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20180711\",\"endDate\":\"20180711\",\"normalAmount\":\"480568\",\"period\":\"4\",\"repayDate\":\"20180711\",\"startDate\":\"20180711\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20180811\",\"endDate\":\"20180811\",\"normalAmount\":\"600710\",\"period\":\"5\",\"repayDate\":\"20180811\",\"startDate\":\"20180811\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20180911\",\"endDate\":\"20180911\",\"normalAmount\":\"720852\",\"period\":\"6\",\"repayDate\":\"20180911\",\"startDate\":\"20180911\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20181011\",\"endDate\":\"20181011\",\"normalAmount\":\"840994\",\"period\":\"7\",\"repayDate\":\"20181011\",\"startDate\":\"20181011\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20181111\",\"endDate\":\"20181111\",\"normalAmount\":\"961136\",\"period\":\"8\",\"repayDate\":\"20181111\",\"startDate\":\"20181111\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20181211\",\"endDate\":\"20181211\",\"normalAmount\":\"1081278\",\"period\":\"9\",\"repayDate\":\"20181211\",\"startDate\":\"20181211\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20190111\",\"endDate\":\"20190111\",\"normalAmount\":\"1201420\",\"period\":\"10\",\"repayDate\":\"20190111\",\"startDate\":\"20190111\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20190211\",\"endDate\":\"20190211\",\"normalAmount\":\"1321562\",\"period\":\"11\",\"repayDate\":\"20190211\",\"startDate\":\"20190211\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120142\",\"status\":\"1\",\"subject\":\"0\"}]},{\"dueDate\":\"20190311\",\"endDate\":\"20190311\",\"normalAmount\":\"1441700\",\"period\":\"12\",\"repayDate\":\"20190311\",\"startDate\":\"20190311\",\"status\":\"0\",\"subjectList\":[{\"amount\":\"120138\",\"status\":\"1\",\"subject\":\"0\"}]}],\"repayDate\":\"20180412\",\"serialNo\":\"2018041100000000006\"}]}",
//                "FUND_JX")));
    }

    /**
     * 用公钥加密.
     *
     * @param src      原内容，明文
     * @param coopCode 合作方编码
     * @return 加密后的Base64字符串
     */
    public static String encryptByPubKey(String src, String coopCode) {

        try {
            byte[] bytes = RSA.encryptByPublicKey(src.getBytes(CommonConst.ENCRYPT_CHARSET), coopCode);
            return Base64.getEncoder().encodeToString(bytes);
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
            byte[] bytes = RSA.decryptByPrikey(Base64.getDecoder().decode(encryptedStr));
            return new String(bytes, CommonConst.ENCRYPT_CHARSET);
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
     * @param sign     签名字符串
     * @param plain    签名前字符串
     * @param coopCode 合作方编码
     * @return true - 验签通过，false-验签未通过
     */
    public static boolean verify(final String sign, final String plain, final String coopCode) {
        try {
            return RSA.verify(sign, plain, coopCode);
        } catch (Exception e) {
            log.error("RSA verify failure:sign={},plain={},coopCode={}", sign, plain, coopCode, e);
        }
        return false;
    }
}
