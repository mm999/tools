package com.xiafei.tools.httpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <P>Description: . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/20</P>
 * <P>UPDATE DATE: 2017/12/20</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public class PostJsonToGateWay extends AbstractPost {
    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {

        PostJsonToGateWay post = new PostJsonToGateWay();
        long start = System.currentTimeMillis();
        List<Thread> cacheList = new ArrayList<>(1000);
        List<Long> elapseList = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            Thread a = new Thread(() -> {
                long innerStart = System.currentTimeMillis();
                post.httpPostJson();
                elapseList.add(System.currentTimeMillis() - innerStart);
            });
            cacheList.add(a);
        }
        cacheList.forEach(Thread::start);
        cacheList.forEach(n -> {
            try {
                n.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("whole elapse:" + (System.currentTimeMillis() - start));
        elapseList.sort((o1, o2) -> (int) (o2 - o1));
        System.out.println("item elapse:" + elapseList);
    }

    @Override
    protected Map<String, Object> getContentMap() {
        return Collections.emptyMap();
    }

    @Override
    protected String getUrl() {
        return "http://192.168.130.207:8067/loan/listProduct/v1";
//        return "http://127.0.0.1:8080/loan/listProduct/v1";
    }
}
