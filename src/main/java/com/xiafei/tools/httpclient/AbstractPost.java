package com.xiafei.tools.httpclient;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * <P>Description: 使用post方式发送数据. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/11</P>
 * <P>UPDATE DATE: 2017/8/11</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
public abstract class AbstractPost {

    private static final int FORM = 0;
    private static final int JSON = 1;

    public static void main(String[] args) {
        AbstractPost post = new PostJsonToMetalQuotation();
        System.out.println(post.httpPostJson());
//        System.out.println(post.httpPostForm());

    }

    public String httpPostForm() {
        return httpPost(FORM);
    }

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
        if (type == 0) {
            addForm(post);
        } else {
            addJson(post);
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

    protected abstract void addJson(final HttpPost post);

    protected abstract String getUrl();

    protected abstract void addForm(final HttpPost post);
}
