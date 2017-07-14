package com.xiafei.tools.utils;

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
 * @version 0.0.1-SNAPSHOT
 * @since java 1.7.0
 */
public final class DateUtils {

    /**
     * 年月日无分隔符.
     */
    public static final SimpleDateFormat YMD = new SimpleDateFormat("yyyyMMdd");

    /**
     * 年月日用横岗分隔.
     */
    public static final SimpleDateFormat YMD_SEPARATE_WITH_BAR = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 年月日用斜杠分隔.
     */
    public static final SimpleDateFormat YMD_SEPARATE_WITH_SLASH = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 年月日时分秒用横杠和冒号分隔.
     */
    public static final SimpleDateFormat YMDHMS_SEPARATE_WITH_BAR_COLON = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 年月日时分秒用斜杠和冒号分隔.
     */
    public static final SimpleDateFormat YMDHMS_SEPARATE_WITH_SLASH_COLON = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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
     * 字符串转换成日期对象，使用默认的年月日时分秒横杠冒号格式化.
     *
     * @param source 字符串对象
     * @return 由字符串转换的日期对象
     */
    public static Date parse(final String source) {
        return parse(source, YMDHMS_SEPARATE_WITH_BAR_COLON);
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
     * 将日期按年月日时分秒格式化成字符串.
     *
     * @param date 日期对象
     * @return 字符串
     */
    public static String toString(final Date date) {
        return toString(date, YMDHMS_SEPARATE_WITH_BAR_COLON);
    }

    /**
     * 将日期按给定的格式格式化成字符串.
     *
     * @param date 日期对象
     * @param sdf  日期格式化对象
     * @return 字符串
     */
    public static String toString(final Date date, final SimpleDateFormat sdf) {
        if (date == null) {
            return "";
        }
        return sdf.format(date);
    }
}
