/* 
 * Copyright (C) 2006-2016 乐视控股（北京）有限公司.
 * 本系统是商用软件,未经授权擅自复制或传播本程序的部分或全部将是非法的.
============================================================
 * @Package:com.leFinanceLoan.PF.common.util
 * @Titel: JSONUtil.java
 * @Created: 2016年8月29日 下午5:23:30   
 * @Version: v1.0
 * @Author：hujian
 * @GitConfig:
============================================================ 
 * ProjectName: leFinanceLoan_PF
 * Description:TODO 
==========================================================*/

package com.xiafei.tools.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DateTypeAdapter;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * <P>Description:  文件操作工具类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/14</P>
 * <P>UPDATE DATE: 2017/7/14</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public final class JsonUtils {

    /**
     * 记录日志工具.
     */
    private static final Logger LOG = Logger.getLogger(JsonUtils.class);

    /**
     * 初始化Gson对象，其中setDateFormat可以执行序列化和反序列化Date的格式，registerTypeAdapter解决了对象中如果包含Date类型
     * 字段并且值为空，在反序列化的时候会报错的问题.
     */
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").
                    registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();

    /**
     * 防止工具类被实例化.
     */
    private JsonUtils() {
    }

    /**
     * 将Json转换成对象.
     *
     * @param jsonStr Json字符串
     * @param clazz   要转换成的对象Class
     * @param <T>     转换成的对象类型
     * @return 转换成的对象实例，若转换发生异常返回null
     */
    public static <T> T fromJson(final String jsonStr, final Class<T> clazz) {
        try {
            return gson.fromJson(jsonStr, clazz);
        } catch (JsonSyntaxException e) {
            LOG.error("fromJson error", e);
        }
        return null;
    }

    /**
     * 将Json转换成带泛型的对象.
     *
     * @param jsonStr Json字符串
     * @param typeOfT 要转换成的对象泛型，使用方法
     *                <pre>
     *                    Type typeOfT = new TypeToken&lt;List&lt;String&gt;&gt;(){}.getType();
     *                </pre>
     * @param <T>     转换成的对象类型
     * @return 转换成的对象实例，若转换发生异常返回null
     */
    public static <T> T fromJson(final String jsonStr, final Type typeOfT) {
        try {
            return gson.fromJson(jsonStr, typeOfT);
        } catch (JsonSyntaxException e) {
            LOG.error("fromJson error", e);
        }
        return null;
    }

    /**
     * 将对象格式化成Json串.
     *
     * @param obj 要格式化的对象
     * @return 格式化的Json串，若发生异常返回null
     */
    public static String toJson(final Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            LOG.error("toJson error", e);
        }
        return null;
    }

    /**
     * 将对象格式化成Json串，指定泛型.
     *
     * @param obj 要格式化的对象
     * @param typeOfT 要转换的对象泛型，使用方法
     *                <pre>
     *                    Type typeOfT = new TypeToken&lt;List&lt;String&gt;&gt;(){}.getType();
     *                </pre>
     * @return 格式化的Json串，若发生异常返回null
     */
    public static String toJson(final Object obj, final Type typeOfT) {
        try {
            return gson.toJson(obj, typeOfT);
        } catch (Exception e) {
            LOG.error("toJson error", e);
        }
        return null;
    }

}
