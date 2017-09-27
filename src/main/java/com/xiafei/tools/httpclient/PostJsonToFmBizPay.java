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
 * <P>Description: 发送Json. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/22</P>
 * <P>UPDATE DATE: 2017/8/22</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class PostJsonToFmBizPay extends AbstractPost {


    public static void main(String[] args) {
        AbstractPost post = new PostJsonToFmBizPay();
        System.out.println(post.httpPostJson());
    }

    @Override
    protected Map<String, Object> getContentMap() {
        final Map<String, Object> content = MapUtils.newHashMap();
        content.put("nofity_type", "成功");
        content.put("PROCESS_STATUS", "PR03");
        content.put("REMARK", "支付成功终态");
        List<Map<String, String>> notifyArray = new ArrayList<>();
        Map<String, String> notifyArrayMap = MapUtils.newHashMap(2);
        content.put("NOFITY_TYPE", "122");
        notifyArrayMap.put("SOURCE_REFERENCE", "NL0000831669335");
        notifyArray.add(notifyArrayMap);
        content.put("body", notifyArrayMap);
        return content;
    }

    @Override
    protected String getUrl() {
        String url;
//        url = "http://123.59.124.139/fm-biz-pay/pay/callBack";
        url = "http://127.0.0.1:8080/pay/callBack";
//        url = "https://pre-jr1.le.com/fm-biz-pay/pay/callBack";
        return url;
    }

}
