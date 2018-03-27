package com.xiafei.tools.httpclient;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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
@Slf4j
public class PostUtil {

    /**
     * 请求返回json传在返回map中的key值.
     */
    public static final String RESP_JSON_KEY = "respJson";

    /**
     * 请求类型键.
     */
    private static final String CONTENT_TYPE_KEY = "Content-Type";

    /**
     * 请求类型为json的值.
     */
    private static final String CONTENT_TYPE_JSON = "application/json;charset=" + "utf-8";

    /**
     * 请求类型为普通form表单的值.
     */
    private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=utf-8";

    /**
     * 工具类.
     */
    private PostUtil() {

    }


    public static void main(String[] args) {
        System.out.println("{\"data\":{\"applyNoList\":[\"2018032110372800275000100009\"],\"batchNo\":\"bcd6db039310440fb58be8afbf88a0c0\",\"status\":\"1\"},\"service\":\"lease_apply_callback\",\"sign\":\"aaJNyxHWMT+O48/FG5eNBOwezxKK7MMz0xOXdVKweYDfEReFaVuruYfl+zDZOrc4IU6qGeU1oX4y\\nYgA4pNR2P4OtZD0VAK1zF4dPsIEjbV22oDUqQubcVCoKD80D9/FVnIRpyFD4RH3EaBtCgLbI2hgI\\nLX1B8QXZEOmH4BHPAOs\\u003d\",\"systemId\":\"FUND_JX\",\"version\":\"1.0\"}");
        System.out.println("服务器返回：" + postJson("http://127.0.0.1:9888/service/toFund",
                "{\"data\":{\"applyNoList\":[\"2018032110372800275000100009\"],\"batchNo\":\"bcd6db039310440fb58be8afbf88a0c0\",\"status\":\"1\"},\"service\":\"lease_apply_callback\",\"sign\":\"aaJNyxHWMT+O48/FG5eNBOwezxKK7MMz0xOXdVKweYDfEReFaVuruYfl+zDZOrc4IU6qGeU1oX4y\\nYgA4pNR2P4OtZD0VAK1zF4dPsIEjbV22oDUqQubcVCoKD80D9/FVnIRpyFD4RH3EaBtCgLbI2hgI\\nLX1B8QXZEOmH4BHPAOs\\u003d\",\"systemId\":\"FUND_JX\",\"version\":\"1.0\"}"));
    }

    /**
     * 发送post请求，json格式，业务无关.
     *
     * @param url     请求url地址
     * @param reqJson 请求json
     * @return 请求响应json串
     */
    public static String postJson(final String url, final String reqJson) {

        final StringEntity entity = new StringEntity(reqJson, "utf-8");
        entity.setContentEncoding("utf-8");
        entity.setContentType(CONTENT_TYPE_JSON);

        return postAndGetResult(url, entity, CONTENT_TYPE_JSON);
    }

    /**
     * 发送post请求，普通form表单提交，业务无关.
     * application/x- www-form-urlencoded
     *
     * @param url    请求url地址
     * @param reqMap 请求数据
     * @return 请求响应json串
     * @throws UnsupportedEncodingException 不支持Url编码异常
     */
    public static String postForm(final String url, final Map<String, Object> reqMap)
            throws UnsupportedEncodingException {

        final List<BasicNameValuePair> pairList = new ArrayList<>(reqMap.size());
        for (Map.Entry<String, Object> entry : reqMap.entrySet()) {
            pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }

        return postAndGetResult(url, new UrlEncodedFormEntity(pairList, "utf-8"),
                CONTENT_TYPE_FORM);
    }


    /**
     * 使用multipart/form-data格式发送Post请求，业务无关.
     *
     * @param url      发送地址
     * @param plainMap 除了文件之外的参数Map
     * @param fileMap  文件参数Map，key是请求map的key，value是multipartFile
     * @return 请求响应json串
     */
    public static String postFormData(final String url, final Map<String, Object> plainMap,
                                      final Map<String, MultipartFile> fileMap) throws IOException {

        //构建multipartEntity对象
        final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setCharset(Charset.forName("utf-8"));
        // 封装一般参数
        for (Map.Entry<String, Object> entry : plainMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            final StringBody body = new StringBody(entry.getValue().toString(), ContentType.TEXT_PLAIN);
            entityBuilder.addPart(entry.getKey(), body);
        }
        // 封装文件参数
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            final InputStreamBody body = new InputStreamBody(entry.getValue().getInputStream(), entry.getValue().getOriginalFilename());
            entityBuilder.addPart(entry.getKey(), body);
        }
        return postAndGetResult(url, entityBuilder.build(), null);
    }

    /**
     * 使用multipart/form-data格式发送Post请求，业务无关.
     *
     * @param url
     * @param plainMap    除了文件之外的参数
     * @param fileMap     文件参数,key就是发往服务方数据中的key，value是输入流
     * @param fileNameMap 文件名参数，对应fileMap中的输入流，使用fileMap的key可以取得文件名
     * @return
     */
    public static String postFormData(final String url, final Map<String, Object> plainMap,
                                      final Map<String, InputStream> fileMap,
                                      final Map<String, String> fileNameMap) throws IOException {

        //构建multipartEntity对象
        final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setCharset(Charset.forName("utf-8"));
        // 封装一般参数
        for (Map.Entry<String, Object> entry : plainMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            entityBuilder.addTextBody(entry.getKey(), entry.getValue().toString(), ContentType.TEXT_PLAIN);
        }
        // 封装文件参数
        for (Map.Entry<String, InputStream> entry : fileMap.entrySet()) {
            entityBuilder.addBinaryBody(entry.getKey(), entry.getValue(), ContentType.DEFAULT_BINARY, fileNameMap.get(entry.getKey()));
        }
        return postAndGetResult(url, entityBuilder.build(), null);
    }

    /**
     * 使用multipart/form-data格式发送Post请求，业务无关.
     *
     * @param url
     * @param plainMap    除了文件之外的参数
     * @param fileMap     文件参数,key就是发往服务方数据中的key，value是字节数组
     * @param fileNameMap 文件名参数，对应fileMap中的输入流，使用fileMap的key可以取得文件名
     * @return
     */
    public static String postFormDataBytes(final String url, final Map<String, Object> plainMap,
                                           final Map<String, byte[]> fileMap,
                                           final Map<String, String> fileNameMap) throws IOException {

        //构建multipartEntity对象
        final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setCharset(Charset.forName("utf-8"));
        // 封装一般参数
        for (Map.Entry<String, Object> entry : plainMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            entityBuilder.addTextBody(entry.getKey(), entry.getValue().toString(), ContentType.TEXT_PLAIN);
        }
        // 封装文件参数
        for (Map.Entry<String, byte[]> entry : fileMap.entrySet()) {
            entityBuilder.addBinaryBody(entry.getKey(), entry.getValue(), ContentType.DEFAULT_BINARY, fileNameMap.get(entry.getKey()));
        }
        return postAndGetResult(url, entityBuilder.build(), null);
    }

    /**
     * 执行发送post请求并拿到结果的方法.
     *
     * @param url       发送请求地址
     * @param reqEntity 发送请求内容
     * @return 响应Json格式字符串
     */
    private static String postAndGetResult(final String url, final HttpEntity reqEntity, final String contentType) {
        final HttpPost post = new HttpPost(url);
        final CloseableHttpClient client = HttpClients.createDefault();
        // multipart/form-data不能指定请求头，因为我们不知道边界怎么设置，由工具去自动计算及设置
        if (contentType != null) {
            post.addHeader(CONTENT_TYPE_KEY, contentType);
        }
        post.setEntity(reqEntity);
        final HttpResponse resp;
        try {
            resp = client.execute(post);
            final int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return EntityUtils.toString(resp.getEntity(), "utf-8");
            } else {
                log.error("postAndGetResult()，发生状态码{}错误,{}", statusCode, Thread.currentThread().getStackTrace());
                throw new RuntimeException();
            }
        } catch (IOException e) {
            log.error("postAndGetResult()，网络不通", e);
            throw new RuntimeException();
        } finally {
            try {
                post.releaseConnection();
            } catch (Exception e) {
                log.warn("postAndGetResult()，释放httpPost资源失败", e);
            }
            post.releaseConnection();
            try {
                client.close();
            } catch (IOException e) {
                log.warn("postAndGetResult()，关闭httpClient报错", e);
            }
        }

    }

}
