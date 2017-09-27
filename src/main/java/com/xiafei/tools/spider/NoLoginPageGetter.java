package com.xiafei.tools.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <P>Description: 不需要登录的页面内容获取器. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/13</P>
 * <P>UPDATE DATE: 2017/8/13</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class NoLoginPageGetter extends AbstractPageGetter {

    /**
     * Java原生获取网页内容.
     *
     * @param webAddress 网页地址
     * @return 网页内容文本
     */
    public String getPageClassic(final String webAddress) {
        InputStreamReader ir = null;
        BufferedReader br = null;
        final StringBuilder result = new StringBuilder();
        try {
            final URL url = new URL(webAddress);
            final URLConnection connection = url.openConnection();
            connection.setConnectTimeout(2000);
            connection.connect();

            ir = new InputStreamReader(connection.getInputStream());
            br = new BufferedReader(ir);
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (ir != null) {
                try {
                    ir.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result.toString();
    }

    public Document getPageJsoup(final String webAddress) {
        Document result = null;
        try {
            result = Jsoup.connect(webAddress).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
