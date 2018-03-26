package com.xiafei.tools.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <P>Description: 日期相关工具类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/12</P>
 * <P>UPDATE DATE: 2017/7/12</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public final class DateUtils {

    /**
     * 年月日无分隔符.
     */
    private static final ThreadLocal<SimpleDateFormat> YMD = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    /**
     * 年月日用横杠分隔.
     */
    private static final ThreadLocal<SimpleDateFormat> YMD_SEPARATE = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 年月日时分秒用横杠和冒号分隔.
     */
    private static final ThreadLocal<SimpleDateFormat> YMDHMS_SEPARATE = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 不允许实例化.
     */
    private DateUtils() {

    }

    /**
     * 获取年月日无分隔符的日期格式化对象.
     *
     * @return 年月日无分隔符的日期格式化对象
     */
    public static SimpleDateFormat getYMD() {
        return YMD.get();
    }

    /**
     * 获取年月日用横杠分隔的日期格式化对象.
     *
     * @return 年月日用横杠分隔的日期格式化对象
     */
    public static SimpleDateFormat getYMDWithSeparate() {
        return YMD_SEPARATE.get();
    }

    /**
     * 获取年月日时分秒用横杠和冒号分隔的日期格式化对象.
     *
     * @return 年月日时分秒用横杠和冒号分隔的日期格式化对象
     */
    public static SimpleDateFormat getYMDHMSWithSeparate() {
        return YMDHMS_SEPARATE.get();
    }

    /**
     * 字符串转换成日期对象，使用默认的年月日时分秒横杠冒号格式化.
     *
     * @param source 字符串对象
     * @return 由字符串转换的日期对象
     */
    public static Date parse(final String source) {
        return parse(source, YMDHMS_SEPARATE.get());
    }

    /**
     * 字符串转换成日期对象，使用默认的年月日时分秒横杠冒号格式化.
     *
     * @param source 字符串对象
     * @param sdf    日期格式化对象
     * @return 由字符串转换的日期对象
     */
    public static Date parse(final String source, final SimpleDateFormat sdf) {
        Date result = null;
        try {

            result = sdf.parse(source);

        } catch (ParseException e) {
            LOGGER.error("字符串转换日期对象异常，字符串：{}", source, e);
        }
        return result;
    }

    /**
     * 整数转换成日期对象，使用默认的年月日时分秒横杠冒号格式化.
     *
     * @param source 整数日期对象
     * @param sdf    日期格式化对象
     * @return 由字符串转换的日期对象
     */
    public static Date parse(final Integer source, final SimpleDateFormat sdf) {
        return parse(source.toString(), sdf);
    }

}
