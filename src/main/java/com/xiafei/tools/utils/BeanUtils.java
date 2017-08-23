package com.xiafei.tools.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * <P>Description: 和Java类相关的工具类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/11</P>
 * <P>UPDATE DATE: 2017/7/11</P>
 *
 * @author qixiafei
 * @version 0.0.1-SNAPSHOT
 * @since java 1.7.0
 */
public final class BeanUtils {

    /**
     * 不允许实例化.
     */
    private BeanUtils() {

    }

    /**
     * 将key=value类URL传参类字符串转换成指定的Java对象.
     *
     * @param keyValueStr  key=value类URL传参类字符串
     * @param separator    不同字段之间的分隔符
     * @param beanClass    要转换成的对象类型
     * @param strictMatch  是否字段名严格匹配
     * @param specialNames 特殊的属性名，key=value串中含有首字母大写的字段
     * @param <T>          要转换成的对象类型
     * @return 将key=value类URL传参类字符串转换成的Java对象.
     * @throws ReflectiveOperationException Java反射类异常
     * @throws IntrospectionException       Java内省类异常
     */
    public static <T> T parseKeyValueStrToBean(final String keyValueStr, final String separator,
                                               final boolean strictMatch, final Class<T> beanClass,
                                               final String... specialNames)
            throws ReflectiveOperationException, IntrospectionException {

        if (StringUtils.isBlank(keyValueStr) || StringUtils.isBlank(separator) || beanClass == null) {
            throw new IllegalArgumentException("将key=value类URL传参类字符串转换成指定的Java对象参数错误：K-V串为空或分隔符为空");
        }
        // key = value 串解析键值对
        final Map<String, String> keyValueMap = parseKeyValueStrToMap(keyValueStr, separator, false);

        // 没有找到有效k-v对，返回null
        if (keyValueMap.isEmpty()) {
            throw new IllegalArgumentException("将key=value类URL传参类字符串转换成指定的Java对象参数错误：找不到有效K-V串");
        }

        return parseStringMapToBean(keyValueMap, strictMatch, beanClass, specialNames);
    }

    /**
     * Map&lt;String,String&lt;转换成对象.
     *
     * @param map          key代表字段名，value代表字段值的map
     * @param beanClass    要转换成的对象class
     * @param strictMatch  是否字段名严格匹配（大小写）
     * @param <T>          对象泛型
     * @param specialNames 特殊的属性名，不符合内省机制默认的首字母小写
     * @return 封装好的对象
     * @throws ReflectiveOperationException Java反射类异常
     * @throws IntrospectionException       Java内省类异常
     */
    public static <T> T parseStringMapToBean(final Map<String, String> map, final boolean strictMatch,
                                             final Class<T> beanClass, final String... specialNames)
            throws ReflectiveOperationException, IntrospectionException {

        // 利用反射实例化bean一个实例
        final T bean = beanClass.newInstance();
        // 利用Java内省机制，获取相应的setter方法
        final BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
        final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        // 循环设置属性值
        for (PropertyDescriptor property : propertyDescriptors) {
            // bean属性名,默认认为属性名是小写开头
            String propertyName = property.getName();
            // 所有类都继承Object的getClass方法，这是多出来的属性
            if ("class".equals(propertyName)) {
                continue;
            }
            // 如果传入了不符合默认规则的属性名，替换成特殊的属性名
            if (specialNames != null) {
                for (String specialName : specialNames) {
                    if (propertyName.equalsIgnoreCase(specialName)) {
                        propertyName = specialName;
                    }
                }
            }
            // bean属性的set方法，默认认为是有且只有一个参数
            final Class<?> paramClass = property.getWriteMethod().getParameterTypes()[0];

            // 属性值
            final Object propertyValue;
            // 对字段名字的各种可能变型进行尝试，知道找到匹配项
            if (map.get(propertyName) != null) {

                propertyValue = parseStringToType(map.get(propertyName), paramClass);

            } else if (!strictMatch) {

                if (map.get(propertyName.toLowerCase()) != null) {

                    propertyValue = parseStringToType(map.get(propertyName.toLowerCase()), paramClass);

                } else if (map.get(propertyName.toUpperCase()) != null) {

                    propertyValue = parseStringToType(map.get(propertyName.toUpperCase()), paramClass);

                } else if (map.get(StringUtils.firstCharToUpper(propertyName)) != null) {

                    propertyValue = parseStringToType(map.get(StringUtils.firstCharToUpper(propertyName)), paramClass);

                } else if (map.get(StringUtils.underLineToHump(propertyName, false)) != null) {

                    propertyValue = parseStringToType(map.get(StringUtils.underLineToHump(propertyName,
                            false)), paramClass);

                } else if (map.get(StringUtils.underLineToHump(propertyName, true)) != null) {

                    propertyValue = parseStringToType(map.get(StringUtils.underLineToHump(propertyName,
                            true)), paramClass);

                } else if (map.get(StringUtils.humpToUnderLine(propertyName, false,
                        null)) != null) {

                    propertyValue = parseStringToType(map.get(StringUtils.humpToUnderLine(propertyName,
                            false, null)), paramClass);

                } else {
                    propertyValue = null;
                }

            } else {
                propertyValue = null;
            }

            if (propertyValue != null) {

                property.getWriteMethod().invoke(bean, propertyValue);
            }

        }
        return bean;

    }


    /**
     * 将字符串转换成指定格式的对象.
     *
     * @param source 字符串
     * @param clazz  要转换成的目标格式
     * @return 目标格式对象
     */
    public static Object parseStringToType(final String source, final Class<?> clazz) {
        final Object result;
        try{
            if (clazz == null || source == null || source.equals("null")) {
                result = null;
            } else if (clazz == Date.class) {
                result = DateUtils.parse(source);
            } else if (clazz == BigDecimal.class) {
                result = new BigDecimal(source);
            } else if (clazz == Character.class) {
                result = source.charAt(0);
            } else if (clazz == Byte.class) {
                result = Byte.parseByte(source);
            } else if (clazz == Short.class) {
                result = Short.parseShort(source);
            } else if (clazz == Integer.class) {
                result = Integer.parseInt(source);
            } else if (clazz == Long.class) {
                result = Long.parseLong(source);
            } else if (clazz == Float.class) {
                result = Float.parseFloat(source);
            } else if (clazz == Double.class) {
                result = Double.parseDouble(source);
            } else if (clazz == Boolean.class) {
                result = Boolean.parseBoolean(source);
            } else {

                result = source;
            }
        }catch (NumberFormatException e){
            System.out.println("格式化异常，字符="+source);
            throw e;
        }

        return result;
    }

    /**
     * 将key=value字符串解析成map.
     *
     * @param keyValueStr key=value字符串
     * @param separator   字段之间的分隔符
     * @param lowerKey    是否将key转换成小写后放入map
     * @return 解析出的map，可能为null
     */
    private static Map<String, String> parseKeyValueStrToMap(final String keyValueStr, final String separator,
                                                             final boolean lowerKey) {
        final String[] kVPairStrArray = keyValueStr.split(separator);
        final Map<String, String> map = MapUtils.newHashMap(kVPairStrArray.length);

        for (String kVStr : kVPairStrArray) {
            if (StringUtils.isBlank(kVStr)) {
                continue;
            }
            // 如果分隔符分隔的部分不是key=value结构的，忽略
            final String[] kV = kVStr.split("=");
            if (kV.length != 2 || StringUtils.isBlank(kV[0]) || StringUtils.isBlank(kV[1])) {
                continue;
            }
            final String key = lowerKey ? kV[0].toLowerCase() : kV[0];
            map.put(key, kV[1]);
        }
        return map;
    }

}
