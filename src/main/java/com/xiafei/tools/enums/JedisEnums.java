package com.xiafei.tools.enums;

/**
 * <P>Description: Jedis枚举 . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/31</P>
 * <P>UPDATE DATE: 2017/8/31</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
public class JedisEnums {

    /**
     * 操作类型枚举.
     */
    public enum NxxxEnum {
        // 如果不存在则设置
        NX,
        // 如果存在则设置
        XX
    }

    /**
     * 超时时间单位枚举.
     */
    public enum EXPX {
        // 秒
        EX,
        // 毫秒
        PX
    }


}
