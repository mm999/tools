package com.xiafei.tools.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.reflect.TypeToken;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JsonUtil {
    private static final Logger LOG = Logger.getLogger(JsonUtil.class);
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").
                    registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();

    public static void main(String[] args) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("dd", "1");
        data.put("cc", "2");
        data.put("aa", "3");
        List<Map<String, Object>> list1 = new ArrayList<>();
        Map<String, Object> data2 = new HashMap<>();
        data2.put("ccc", "1");
        data2.put("aaa", "2");
        list1.add(data2);
        Map<String, Object> data3 = new HashMap<>();
        data3.put("ddd", "1");
        data3.put("bbb", "2");
        list1.add(data3);
        data.put("bb", list1);
        System.out.println(gson.toJson(data));
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(data));
        String json = gson.toJson(data);
        Map<String, Object> ret = gson.fromJson(json, new TypeToken<TreeMap<String, Object>>() {
        }.getType());

        Map<String, Object> ret2 = mapper.readValue(json, TreeMap.class);

        System.out.println(gson.toJson(ret));
        System.out.println(mapper.writeValueAsString(ret2));

    }

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

    private JsonUtil() {

    }

}
