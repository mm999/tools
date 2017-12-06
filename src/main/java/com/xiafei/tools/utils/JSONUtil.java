package com.xiafei.tools.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DateTypeAdapter;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;

public class JSONUtil {
    private static final Logger LOG = Logger.getLogger(JSONUtil.class);
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").
                    registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        try {
            return gson.fromJson(jsonStr, clazz);
        } catch (JsonSyntaxException e) {
            LOG.error("gson.fromJson error", e);
        }
        return null;
    }

    public static <T> T fromJson(String jsonStr, Type type) {
        try {
            return gson.fromJson(jsonStr, type);
        } catch (JsonSyntaxException e) {
            LOG.error("gson.fromJson error", e);
        }
        return null;
    }

    public static String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            LOG.error("gson.toJson error", e);
        }
        return null;
    }

    public static String toJson(Object obj, Type type) {
        try {
            return gson.toJson(obj, type);
        } catch (Exception e) {
            LOG.error("gson.toJson error", e);
        }
        return null;
    }

    public static JSONObject appendJson(JSONObject src, JSONObject addiction) {
        Iterator it = addiction.keys();
        while (it.hasNext()) {
            String key = it.next().toString();
            src.put(key, addiction.getString(key));
        }
        return src;
    }

    public static JSONObject append(Object src, Object addiction) {
        JSONObject srcJObj = JSONObject.fromObject(toJson(src));
        JSONObject addictionJObj = JSONObject.fromObject(toJson(addiction));

        Iterator it = addictionJObj.keys();
        while (it.hasNext()) {
            String key = it.next().toString();
            srcJObj.put(key, addictionJObj.getString(key));
        }
        return srcJObj;
    }

    private JSONUtil() {

    }

}
