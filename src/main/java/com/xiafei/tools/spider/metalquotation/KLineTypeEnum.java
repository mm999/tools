package com.xiafei.tools.spider.metalquotation;

/**
 * <P>Description: K线周期类型枚举. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/26</P>
 * <P>UPDATE DATE: 2017/7/26</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public enum KLineTypeEnum {

    DAY((byte) 0, "日K"),
    WEEK((byte) 1, "周K"),
    MONTH((byte) 2, "月K"),
    MINUTE_1((byte) 3, "1分钟K"),
    MINUTE_5((byte) 4, "5分钟K"),
    MINUTE_15((byte) 5, "15分钟K"),
    MINUTE_30((byte) 6, "30分钟K"),
    MINUTE_60((byte) 7, "60分钟K"),
    MINUTE_240((byte) 8, "240分钟K");

    /**
     * 周期类型编码.
     */
    public final Byte code;

    /**
     * 周期类型描述.
     */
    public final String desc;

    KLineTypeEnum(final byte code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据枚举code字段确定枚举值.
     *
     * @param code K线类型码
     * @return K线类型码为传入参数的枚举值
     */
    public static KLineTypeEnum instance(final Byte code) {
        if (code == null) return null;
        for (KLineTypeEnum e : values()) {
            if (e.code.equals(code)) return e;
        }
        return null;
    }

}
