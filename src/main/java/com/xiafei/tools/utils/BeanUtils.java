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
     * @param keyValueStr key=value类URL传参类字符串
     * @param separator   不同字段之间的分隔符
     * @param beanClass   要转换成的对象类型
     * @param <T>         要转换成的对象泛型
     * @return 将key=value类URL传参类字符串转换成的Java对象.
     * @throws ReflectiveOperationException Java反射类异常
     * @throws IntrospectionException       Java内省类异常
     */
    public static <T> T parseKeyValueToBean(final String keyValueStr, final String separator, final Class<T> beanClass)
        throws ReflectiveOperationException, IntrospectionException {

        if (StringUtils.isBlank(keyValueStr) || StringUtils.isBlank(separator) || beanClass == null) {
            throw new IllegalArgumentException("将key=value类URL传参类字符串转换成指定的Java对象，参数错误");
        }
        // key = value 串数组
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
            map.put(kV[0], kV[1]);
        }

        if (map.isEmpty()) {
            return null;
        }

        // 利用反射实例化bean一个实例
        final T bean = beanClass.newInstance();
        // 利用Java内省机制，获取相应的setter方法
        final BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
        final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        // 循环设置属性值
        for (PropertyDescriptor property : propertyDescriptors) {
            // bean属性名,默认认为水姓名是小写开头
            final String propertyName = property.getName();
            // 所有类都继承Object的getClass方法，这是多出来的属性
            if ("class".equals(propertyName)) {
                continue;
            }
            // bean属性的set方法，默认认为是有且只有一个参数
            final Class<?> paramClass = property.getWriteMethod().getParameterTypes()[0];
            // 属性值
            final Object propertyValue;
            if (map.get(propertyName) != null) {

                propertyValue = parseStringToType(map.get(propertyName), paramClass);

            } else if (map.get(propertyName.toLowerCase()) != null) {

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
        if (clazz == null || source == null) {
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
        return result;
    }
}