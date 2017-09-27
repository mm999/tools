package com.xiafei.tools.httpclient;


import com.google.gson.reflect.TypeToken;
import com.xiafei.tools.utils.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <P>Description: 使用Http-post方式发送数据. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/11</P>
 * <P>UPDATE DATE: 2017/8/11</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public abstract class AbstractPost {

    /**
     * 类型码：form表单提交.
     */
    private static final int FORM = 0;

    /**
     * 类型码：Json字符串提交.
     */
    private static final int JSON = 1;

    /**
     * 发送HttpPost请求，数据放在FormData里.
     *
     * @return 服务器返回Json数据
     */
    public String httpPostForm() {
        return httpPost(FORM);
    }

    /**
     * 发送HttpPost请求，数据是Json串.
     *
     * @return 服务器返回Json数据
     */
    public String httpPostJson() {
        return httpPost(JSON);
    }

    /**
     * 发送post请求.
     *
     * @param type 0-form表单,1-Json串
     * @return 服务器返回请求
     */
    private String httpPost(final int type) {
        HttpPost post = new HttpPost(getUrl());
        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;
        final Map<String, Object> respMap = getContentMap();
        if (type == 0) {

            List<BasicNameValuePair> pairList = new ArrayList<>(respMap.size());
            for (Map.Entry<String, Object> entry : respMap.entrySet()) {
                pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            try {
                post.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            post.addHeader("Content-Type", "application/json;charset=UTF-8");
            StringEntity entity = new StringEntity(
                    JsonUtils.toJson(respMap, new TypeToken<Map<String, Object>>() {
                    }.getType()),
                    "utf-8");
            entity.setContentEncoding("utf-8");
            entity.setContentType("application/json");
            post.setEntity(entity);
        }
        System.out.println("Executing request " + post.getRequestLine());
        try {
            HttpResponse resp = client.execute(post);
            System.out.println("ResponseCode: " + resp.getStatusLine().getStatusCode());

            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity respEntity = resp.getEntity();
                respContent = EntityUtils.toString(respEntity, "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
        return respContent;
    }

    protected abstract Map<String, Object> getContentMap();

    protected abstract String getUrl();

}
