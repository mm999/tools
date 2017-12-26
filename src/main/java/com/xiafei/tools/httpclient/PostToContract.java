package com.xiafei.tools.httpclient;

import java.util.Collections;
import java.util.Map;

/**
 * <P>Description: . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/25</P>
 * <P>UPDATE DATE: 2017/12/25</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public class PostToContract extends AbstractPost {

    public static void main(String[] args) {
        PostToContract post = new PostToContract();
        post.httpPostJson();
    }

    @Override
    protected Map<String, Object> getContentMap() {
        return Collections.singletonMap("applyID", "xixi");
    }

    @Override
    protected String getUrl() {
        return "http://192.168.1.244:8063/contract/rebuildContractFile";
    }
}
