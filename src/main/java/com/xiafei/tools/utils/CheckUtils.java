package com.xiafei.tools.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * <P>Description: 检查工具类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/11/23</P>
 * <P>UPDATE DATE: 2017/11/23</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class CheckUtils {
    private CheckUtils() {

    }

    /**
     * 检查对象及字段是否为空，最多遍历两层.
     *
     * @param objs 要检查的对象，可变.
     * @throws NullException 空异常
     */
    public void checkNull(Object... objs) throws NullException, IntrospectionException, InvocationTargetException,
            IllegalAccessException {
        if (objs == null) {
            throw new NullException();
        }
        for (Object obj : objs) {
            // 如果对象为空，抛出异常
            if (obj == null) {
                throw new NullException();
            }
            final Class clazz = obj.getClass();
            // 字符串判断是否是空串
            if (clazz == String.class) {
                if (obj.toString().trim().equals("")) {
                    throw new NullException();
                }
            }
            // 如果是可以遍历的类型，比如list，set，遍历处理参数
            if (Iterable.class.isAssignableFrom(clazz)) {
                for (Object property : (Iterable) obj) {
                    if (property == null) {
                        throw new NullException();
                    }
                    Class propertyClass = property.getClass();
                    if (propertyClass == String.class) {
                        if (property.toString().trim().equals("")) {
                            throw new NullException();
                        }
                    }
                    if (isPojo(propertyClass)) {
                        checkPropertiesNullFirstFloor(property, propertyClass);
                    }
                }
            }

            // 如果是pojo，判断属性是否含有空的
            if (isPojo(clazz)) {
                // 如果不是基本类型，利用内省机制检查字段
                checkPropertiesNullFirstFloor(obj, clazz);
            }

        }
    }

    /**
     * 检查对象字段是否为空,最多遍历两层..
     *
     * @param obj   要检查的对象
     * @param clazz 要检查的对象class
     */
    private void checkPropertiesNullFirstFloor(final Object obj, final Class clazz) throws IntrospectionException,
            InvocationTargetException, IllegalAccessException, NullException {
        final BeanInfo bean = Introspector.getBeanInfo(clazz);
        final PropertyDescriptor[] properties = bean.getPropertyDescriptors();
        for (PropertyDescriptor property : properties) {
            // bean属性名,默认认为属性名是小写开头，而报文中所有字段都是大写
            final String propertyName = property.getName();
            // 所有类都继承Object的getClass方法，这是多出来的属性
            if ("class".equals(propertyName)) {
                continue;
            }
            final Object propertyValue = property.getReadMethod().invoke(obj, null);
            if (propertyValue == null) {
                throw new NullException();
            }
            final Class propertyValueClass = propertyValue.getClass();
            // 字符串判断是否是空串
            if (propertyValueClass == String.class) {
                if (propertyValue.toString().trim().equals("")) {
                    throw new NullException();
                }
            }

            // 如果是可以遍历的类型，比如list，set，遍历处理参数
            if (Iterable.class.isAssignableFrom(propertyValueClass)) {
                for (Object propertysProperty : (Iterable) propertyValue) {
                    if (propertysProperty == null) {
                        throw new NullException();
                    }
                    Class propertysPropertyClass = propertysProperty.getClass();
                    if (propertysPropertyClass == String.class) {
                        if (propertysProperty.toString().trim().equals("")) {
                            throw new NullException();
                        }
                    }

                }
            }
            if (isPojo(propertyValueClass)) {
                checkPropertiesNullSecondeFloor(propertyValue, propertyValueClass);
            }
        }
    }

    private void checkPropertiesNullSecondeFloor(final Object obj, final Class clazz)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException, NullException {
        final BeanInfo bean = Introspector.getBeanInfo(clazz);
        final PropertyDescriptor[] properties = bean.getPropertyDescriptors();
        for (PropertyDescriptor property : properties) {
            // bean属性名,默认认为属性名是小写开头，而报文中所有字段都是大写
            final String propertyName = property.getName();
            // 所有类都继承Object的getClass方法，这是多出来的属性
            if ("class".equals(propertyName)) {
                continue;
            }
            final Object propertyValue = property.getReadMethod().invoke(obj, null);
            if (propertyValue == null) {
                throw new NullException();
            }
            final Class propertyValueClass = propertyValue.getClass();
            // 字符串判断是否是空串
            if (propertyValueClass == String.class) {
                if (propertyValue.toString().trim().equals("")) {
                    throw new NullException();
                }
            }

            // 如果是可以遍历的类型，比如list，set，遍历处理参数
            if (Iterable.class.isAssignableFrom(propertyValueClass)) {
                for (Object propertysProperty : (Iterable) propertyValue) {
                    if (propertysProperty == null) {
                        throw new NullException();
                    }
                    Class propertysPropertyClass = propertysProperty.getClass();
                    if (propertysPropertyClass == String.class) {
                        if (propertysProperty.toString().trim().equals("")) {
                            throw new NullException();
                        }
                    }

                }
            }
        }
    }


    /**
     * 判断对象是否是jdk中定义之外的对象.
     *
     * @param clazz 对象的class
     * @return
     */
    private boolean isPojo(final Class clazz) {
        return clazz != Date.class && clazz != Character.class && clazz != Boolean.class
                && !Number.class.isAssignableFrom(clazz) && !Iterable.class.isAssignableFrom(clazz);
    }

    public class NullException extends Exception {
        public NullException() {
            super("存在空参数");
        }
    }
}
