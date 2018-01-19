package com.xiafei.tools.utils;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * <P>Description: 跑单元测试时候，可以使用这个类加快测试速度. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/1/19</P>
 * <P>UPDATE DATE: 2018/1/19</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public class TestUtils {

    public static <T> T setProperties(T object) throws IllegalAccessException {
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (java.lang.reflect.Modifier.isFinal(f.getModifiers())) {
                System.out.println("final对象字段忽略" + f.getName());
                continue;
            }
            Class valCls = f.getType();
            if (valCls == String.class) {
                f.set(object, "测试");
            } else if (Byte.class == valCls) {
                f.set(object, (byte) 1);
            } else if (Short.class == valCls) {
                f.set(object, (short) 1);
            } else if (Integer.class == valCls) {
                f.set(object, 1);
            } else if (Long.class == valCls) {
                f.set(object, 1L);
            } else if (Float.class == valCls) {
                f.set(object, 1.0F);
            } else if (Double.class == valCls) {
                f.set(object, 1.0);
            } else if (Character.class == valCls) {
                f.set(object, 'A');
            } else if (Boolean.class == valCls) {
                f.set(object, true);
            } else if (Date.class == valCls) {
                f.set(object, new Date());
            } else {
                System.out.println("对象字段忽略" + f.getName());
            }
        }
        return object;
    }

    public static void main(String[] args) throws IllegalAccessException {
    }
}
