package com.xiafei.tools.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DateTypeAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Gson工具.
 */
@Slf4j
public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").
                    registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        try {
            return gson.fromJson(jsonStr, clazz);
        } catch (JsonSyntaxException e) {
            log.error("gson.fromJson error", e);
        }
        return null;
    }

    public static <T> T fromJson(String jsonStr, Type type) {
        try {
            return gson.fromJson(jsonStr, type);
        } catch (JsonSyntaxException e) {
            log.error("gson.fromJson error", e);
        }
        return null;
    }

    public static String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            log.error("gson.toJson error", e);
        }
        return null;
    }

    public static String toJson(Object obj, Type type) {
        try {
            return gson.toJson(obj, type);
        } catch (Exception e) {
            log.error("gson.toJson error", e);
        }
        return null;
    }

    private JsonUtil() {

    }

}
