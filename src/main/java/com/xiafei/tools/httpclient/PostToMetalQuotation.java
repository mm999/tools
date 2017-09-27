package com.xiafei.tools.httpclient;

import com.xiafei.tools.utils.MapUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <P>Description: 具体业务实现类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/11</P>
 * <P>UPDATE DATE: 2017/8/11</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class PostToMetalQuotation extends AbstractPost {

    public static void main(String[] args){
        AbstractPost post = new PostToMetalQuotation();
        post.httpPostJson();
    }

    @Override
    protected String getUrl() {
        String url;
//        url = "http://pre-jr1.le.com/metal-quotation/goldInfo/qList";
//        url = "http://pre-jr1.le.com/metal-quotation/goldInfo/productState";
//        url = "http://pre-jr1.le.com/metal-quotation/goldK/getK";
//        url = "http://pre-jr1.le.com/metal-quotation/goldTime/getTime";
//        url = "http://pre-jr1.le.com/metal-quotation/goldTime/getTwoDay";
        url = "http://127.0.0.1:8091/goldInfo/qList/v1/appkey";
        return url;
    }

    @Override
    protected Map<String, Object> getContentMap() {
        Map<String, Object> content = MapUtils.newHashMap();
        content.put("clientType", 1);
        return content;
    }

}
