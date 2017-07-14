package com.xiafei.tools.utils;

import com.xiafei.tools.collections.Queue;

/**
 * <P>Description: 扩展apache的String工具类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: ${date}</P>
 * <P>UPDATE DATE: ${date}</P>
 *
 * @author 齐霞飞
 * @version 0.0.1-SNAPSHOT
 * @since java 1.7.0
 */
public final class StringUtils extends org.apache.commons.lang.StringUtils {

    /**
     * 下划线.
     */
    private static final String UNDER_LINE = "_";

    /**
     * 不允许实例化.
     */
    private StringUtils() {

    }


    /**
     * 将不足位数的整数左侧填充0直到指定的位数.
     *
     * @param value 十进制值
     * @param digit 位数
     * @return 左侧填充0到指定位数后的字符串
     */
    public static String fillZeroLeftToDigit(final int value, final int digit) {
        // 绝对值的返回结果
        String absResult = null;

        final String absValueStr = String.valueOf(Math.abs(value));
        // value位数
        final int oriLength = absValueStr.length();

        if (oriLength >= digit) {
            absResult = absValueStr;
        } else {
            final StringBuilder absSb = new StringBuilder();
            for (int i = 0, len = digit - oriLength; i < len; i++) {
                absSb.append("0");
            }
            absSb.append(absValueStr);
            absResult = absSb.toString();
        }
        return value < 0 ? "-" + absResult : absResult;

    }

    /**
     * 首字母大写.
     *
     * @param oriStr 原字符串
     * @return 首字母大写后的字符串
     */
    public static String firstCharToUpper(final String oriStr) {
        final StringBuilder sb = new StringBuilder(oriStr);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    /**
     * 首字母小写.
     *
     * @param oriStr 原字符串
     * @return 首字母大写后的字符串
     */
    public static String firstCharToLower(final String oriStr) {
        final StringBuilder sb = new StringBuilder(oriStr);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }

    /**
     * 下划线转驼峰.
     *
     * @param oriStr       原串
     * @param firstToUpper 首字母是否大写
     * @return 将下划线模式转驼峰后的新串
     */
    public static String underLineToHump(final String oriStr, final boolean firstToUpper) {
        if (isBlank(oriStr)) {
            return oriStr;
        }
        final String[] parts = oriStr.split(UNDER_LINE);
        final StringBuilder sb = new StringBuilder();
        for (int i = 0, len = parts.length; i < len; i++) {
            if (i == 0 && !firstToUpper) {
                sb.append(parts[i]);
            } else {
                sb.append(firstCharToUpper(parts[i]));
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰转下划线，原大写字母变小写字母.
     *
     * @param oriStr               原串
     * @param firstCharToUnderLine 首字母若是大写字母，是否需要转下划线
     * @param ignoreStr            忽略大写字母检查的字符串
     * @return 将驼峰转下划线格式后的新串
     */
    public static String humpToUnderLine(final String oriStr, final boolean firstCharToUnderLine,
                                         final String ignoreStr) {
        if (isBlank(oriStr)) {
            return oriStr;
        }
        // 忽略大写字母检查的字符串所在原字符串起始位置
        final Queue<Integer> ignorePositions = new Queue<>(2);
        // 若忽略字符串不为空，计算所忽略字符串在原字符串中出现的所有位置
        if (isNotBlank(ignoreStr)) {
            // 当前循环内忽略字符串所在位置
            int ignorePosition;
            for (int i = 0, len = oriStr.length(); i < len; ) {

                ignorePosition = oriStr.substring(i).indexOf(ignoreStr);

                if (ignorePosition > -1) {

                    ignorePositions.enqueue(i + ignorePosition);
                    i = ignorePosition + ignoreStr.length();

                } else {
                    break;
                }
            }
        }

        final StringBuilder sb = new StringBuilder();
        final char[] charArray = oriStr.toCharArray();
        // 下一个忽略点的位置
        int nextIgnorePosition = -1;
        if (!ignorePositions.isEmpty()) {
            nextIgnorePosition = ignorePositions.dequeue();
        }
        for (int i = 0, len = oriStr.length(); i < len; ) {

            if (nextIgnorePosition == i) {
                i += ignoreStr.length();
                nextIgnorePosition = ignorePositions.isEmpty() ? -1 : ignorePositions.dequeue();
                sb.append(ignoreStr);
                continue;
            }

            final char codePoint = charArray[i];
            if (Character.isUpperCase(codePoint)) {
                sb.append(UNDER_LINE).append(Character.toLowerCase(codePoint));
            } else {
                sb.append(codePoint);
            }
            i++;
        }
        return sb.toString();
    }

    /**
     * 从类似URL参数字符串中获取某一个属性的值.
     *
     * @param source       字符串
     * @param propertyName 属性名
     * @param separator    分隔符
     * @return 属性值
     */
    public static String getPropertyValueFromSimilarURL(final String source, final String propertyName, final String separator) {
        if (isBlank(source) || isBlank(propertyName)) {
            return null;
        }
        // 先拿到属性所在字符串起始位置，拼上=号以防止属性名部分相同的情况发生
        final int propertyIndex = source.indexOf(propertyName.concat("="));
        if (propertyIndex > -1) {
            // 属性的值拼接builder
            final StringBuilder sb = new StringBuilder();
            // 属性值的起始位置是属性名起始位置+属性名长度+1越过等于号
            for (int i = propertyIndex + propertyName.length() + 1, len = source.length();
                 i < len; i++) {

                final char c = source.charAt(i);
                // 如果已经遍历到分隔符，退出循环
                if (source.substring(i).indexOf(separator) == 0) {
                    break;
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();

        } else {
            return null;
        }
    }
}
