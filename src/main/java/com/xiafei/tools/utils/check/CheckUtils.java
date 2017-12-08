package com.xiafei.tools.utils.check;


import com.xiafei.tools.exceptions.BizException;
import com.xiafei.tools.retry.Code;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <P>Description: 检查工具类，与业务耦合. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/11/23</P>
 * <P>UPDATE DATE: 2017/11/23</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@Slf4j
public class CheckUtils {
    private CheckUtils() {

    }

    /**
     * 在需要跳过检查的属性上加这个注解.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PropertySkipCheck {

    }

    /**
     * 检查对象及字段是否为空，最多遍历两层.
     *
     * @param nullCode 如果检查到空的字段了抛出Biz异常中的错误码
     * @param nullDesc 如果检查到空的字段了抛出Biz异常中的错误信息
     * @param objs     要检查的对象，可变.
     * @throws BizException 空异常
     */
    public static void checkNull(int nullCode, String nullDesc, Object... objs) {
        if (objs == null) {
            throw new BizException(nullCode, nullDesc);
        }
        for (Object obj : objs) {
            // 如果对象为空，抛出异常
            if (obj == null) {
                throw new BizException(nullCode, nullDesc);
            }
            final Class clazz = obj.getClass();
            // 字符串判断是否是空串
            if (clazz == String.class) {
                if (obj.toString().trim().equals("")) {
                    throw new BizException(nullCode, nullDesc);
                }
            }
            // 如果是可以遍历的类型，比如list，set，遍历处理参数
            if (Iterable.class.isAssignableFrom(clazz)) {
                for (Object property : (Iterable) obj) {
                    if (property == null) {
                        throw new BizException(nullCode, nullDesc);
                    }
                    Class propertyClass = property.getClass();
                    if (propertyClass == String.class) {
                        if (property.toString().trim().equals("")) {
                            throw new BizException(nullCode, nullDesc);
                        }
                    }
                    if (isPojo(propertyClass)) {
                        checkPropertiesNullFirstFloor(nullCode, nullDesc, property, propertyClass);
                    }
                }
            }

            // 如果是pojo，判断属性是否含有空的
            if (isPojo(clazz)) {
                // 如果不是基本类型，利用内省机制检查字段
                checkPropertiesNullFirstFloor(nullCode, nullDesc, obj, clazz);
            }

        }
    }

    /**
     * 检查对象字段是否为空,最多向下遍历两层..
     *
     * @param nullCode 如果检查到空的字段了抛出Biz异常中的错误码
     * @param nullDesc 如果检查到空的字段了抛出Biz异常中的错误信息
     * @param obj      要检查的对象
     * @param clazz    要检查的对象class
     */
    private static void checkPropertiesNullFirstFloor(final int nullCode, final String nullDesc, final Object obj, final Class clazz) {
        final List<Field> fieldList = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        Class tempClass = clazz;
        while (true) {
            tempClass = tempClass.getSuperclass();
            if (tempClass == null || tempClass == Object.class) {
                break;
            }
            Field[] fields = tempClass.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                fieldList.addAll(new ArrayList<>(Arrays.asList(fields)));
            }
        }
        final Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);

        for (Field field : fields) {
            field.setAccessible(true);
            // 如果有跳过注解的字段放过检查
            if (field.getAnnotation(PropertySkipCheck.class) != null) {
                continue;
            }
            final Object propertyValue;
            try {
                propertyValue = field.get(obj);
            } catch (IllegalAccessException e) {
                log.error("反射获取第一层属性值对象报错", e);
                throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "系统内部错误");
            }
            if (propertyValue == null) {
                throw new BizException(nullCode, nullDesc);
            }
            final Class propertyValueClass = propertyValue.getClass();
            // 字符串判断是否是空串
            if (propertyValueClass == String.class) {
                if (propertyValue.toString().trim().equals("")) {
                    throw new BizException(nullCode, nullDesc);
                }
            }

            // 如果是可以遍历的类型，比如list，set，遍历处理参数
            if (Iterable.class.isAssignableFrom(propertyValueClass)) {
                for (Object propertysProperty : (Iterable) propertyValue) {
                    if (propertysProperty == null) {
                        throw new BizException(nullCode, nullDesc);
                    }
                    Class propertysPropertyClass = propertysProperty.getClass();
                    if (propertysPropertyClass == String.class) {
                        if (propertysProperty.toString().trim().equals("")) {
                            throw new BizException(nullCode, nullDesc);
                        }
                    }

                }
            }
            if (isPojo(propertyValueClass)) {
                checkPropertiesNullSecondeFloor(nullCode, nullDesc, propertyValue, propertyValueClass);
            }
        }
    }

    private static void checkPropertiesNullSecondeFloor(final int nullCode, final String nullDesc,
                                                        final Object obj, final Class clazz) {
        final Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            // 如果有跳过注解的字段放过检查
            if (field.getAnnotation(PropertySkipCheck.class) != null) {
                continue;
            }
            final Object propertyValue;
            try {
                propertyValue = field.get(obj);
            } catch (IllegalAccessException e) {
                log.error("反射获取第二层属性值对象报错", e);
                throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "系统内部错误");
            }
            if (propertyValue == null) {
                throw new BizException(nullCode, nullDesc);
            }
            final Class propertyValueClass = propertyValue.getClass();
            // 字符串判断是否是空串
            if (propertyValueClass == String.class) {
                if (propertyValue.toString().trim().equals("")) {
                    throw new BizException(nullCode, nullDesc);
                }
            }

            // 如果是可以遍历的类型，比如list，set，遍历处理参数
            if (Iterable.class.isAssignableFrom(propertyValueClass)) {
                for (Object propertysProperty : (Iterable) propertyValue) {
                    if (propertysProperty == null) {
                        throw new BizException(nullCode, nullDesc);
                    }
                    Class propertysPropertyClass = propertysProperty.getClass();
                    if (propertysPropertyClass == String.class) {
                        if (propertysProperty.toString().trim().equals("")) {
                            throw new BizException(nullCode, nullDesc);
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
    private static boolean isPojo(final Class clazz) {
        return clazz != Date.class && clazz != Character.class && clazz != Boolean.class
                && !Number.class.isAssignableFrom(clazz) && !Iterable.class.isAssignableFrom(clazz)
                && String.class != clazz;
    }

}
