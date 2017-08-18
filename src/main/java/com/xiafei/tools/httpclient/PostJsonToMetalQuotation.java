package com.xiafei.tools.httpclient;

import net.sf.json.JSONObject;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * <P>Description:  </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/11</P>
 * <P>UPDATE DATE: 2017/8/11</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
public class PostJsonToMetalQuotation extends AbstractPost {


    @Override
    protected String getUrl() {
        String url;
//        url = "http://pre-jr1.le.com/metal-quotation/goldInfo/qList";
//        url = "http://pre-jr1.le.com/metal-quotation/goldInfo/productState";
//        url = "http://pre-jr1.le.com/metal-quotation/goldK/getK";
//        url = "http://pre-jr1.le.com/metal-quotation/goldTime/getTime";
//        url = "http://pre-jr1.le.com/metal-quotation/goldTime/getTwoDay";
        url = "http://127.0.0.1:8091/goldInfo/qList/v1/appkey";
        return url ;
    }

    @Override
    protected void addJson(final HttpPost post) {
        post.addHeader("Content-Type", "application/json;charset=UTF-8");
        JSONObject json = new JSONObject();
//        json.put("version", "v1");
//        json.put("appKey", "rsa");
        json.put("clientType", 1);
//        json.put("productId", "Au(T+D)");
//        json.put("kType", 3);
//        json.put("fromDay", 20170811);
//        json.put("toDay", 20170811);
        StringEntity entity = new StringEntity(json.toString(), "utf-8");
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/json");
        post.setEntity(entity);
    }


    @Override
    protected void addForm(final HttpPost post) {
//        post.addHeader("Content-Type", "application/json;charset=UTF-8");
        List<BasicNameValuePair> pairList = new ArrayList<>(4);
//        BasicNameValuePair pair1 = new BasicNameValuePair("productId", "Au(T+D)");
//        pairList.add(pair1);

//        BasicNameValuePair pair2 = new BasicNameValuePair("kType", "0");
//        pairList.add(pair2);
//
//        BasicNameValuePair pair3 = new BasicNameValuePair("fromDay", "20170811");
//        pairList.add(pair3);
//
//        BasicNameValuePair pair4 = new BasicNameValuePair("toDay", "20170811");
//        pairList.add(pair4);

//        BasicNameValuePair pair5 = new BasicNameValuePair("version", "v1");
//        pairList.add(pair5);
//
//        pairList.add(new BasicNameValuePair("appKey", "rsa"));
        pairList.add(new BasicNameValuePair("clientType", "1"));
        try {
            post.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
