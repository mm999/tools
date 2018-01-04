package com.xiafei.tools.httpclient;

import java.util.Collections;
import java.util.Map;

/**
 * <P>Description: . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/1/2</P>
 * <P>UPDATE DATE: 2018/1/2</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public class PostToSelf extends AbstractPost{

    public static void main(String[] args) {
        PostToSelf post = new PostToSelf();
        post.httpPostJson();
    }

    @Override
    protected Map<String, Object> getContentMap() {
        return Collections.emptyMap();
    }

    @Override
    protected String getUrl() {
        return "http://127.0.0.1:8080/refresh";
    }
}
