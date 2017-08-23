package com.xiafei.tools.httpclient;

import com.xiafei.tools.utils.MapUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <P>Description:  . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/22</P>
 * <P>UPDATE DATE: 2017/8/22</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
public class PostJsonToFmBizPay extends AbstractPost {


    public static void main(String[] args) {
        AbstractPost post =  new PostJsonToFmBizPay();
//        System.out.println(post.httpPostJson());
        System.out.println(post.httpPostForm());
//
    }


    @Override
    protected void addJson(final HttpPost post) {
        post.addHeader("Content-Type", "application/json;charset=UTF-8");
        JSONObject json = new JSONObject();
        json.put("nofity_type", "成功");
//        json.put("PROCESS_STATUS", "PR03");
//        json.put("REMARK", "支付成功终态");
        JSONArray jsonItem = new JSONArray();
        List<Map<String, String>> notifyArray = new ArrayList<>();
        Map<String, String> notifyArrayMap = MapUtils.newHashMap(2);
        notifyArrayMap.put("REF_NO", "");
        notifyArrayMap.put("SOURCE_REFERENCE", "NL0000831669335");
        notifyArray.add(notifyArrayMap);
        jsonItem.addAll(notifyArray);
//        json.put("NOTIFY_ARRAY", jsonItem);
        StringEntity entity = new StringEntity(json.toString(), "utf-8");
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/json");
        post.setEntity(entity);
    }

    @Override
    protected String getUrl() {
        String url;
        url = "http://123.59.124.139/fm-biz-pay/pay/callBack";
//        url = "http://127.0.0.1:8080/pay/callBack";
        return url;
    }

    @Override
    protected void addForm(final HttpPost post) {
        List<BasicNameValuePair> pairList = new ArrayList<>(4);

        pairList.add(new BasicNameValuePair("NOFITY_TYPE", "1"));
        pairList.add(new BasicNameValuePair("PROCESS_STATUS", "PR03"));
        pairList.add(new BasicNameValuePair("REMARK", "支付成功终态"));
        pairList.add(new BasicNameValuePair("NOTIFY_ARRAY[0].REF_NO", ""));
        pairList.add(new BasicNameValuePair("NOTIFY_ARRAY[0].SOURCE_REFERENCE", "NL0000831669335"));

        try {
            post.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
