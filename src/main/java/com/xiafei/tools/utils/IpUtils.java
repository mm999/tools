package com.xiafei.tools.utils;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * <P>Description: IP地址工具. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/2/5</P>
 * <P>UPDATE DATE: 2018/2/5</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Slf4j
public class IpUtils {

    private static volatile String REAL_IP = null;
    private static volatile Integer SERVER_PORT = null;
    private static final char FILE_SEPARATOR = File.separatorChar;
    private static volatile String NOTIFY_URL_PREFIX;


    public static void main(String[] args) {
        String ip = "127.0.0.1";
        String[] ipFra = ip.split("\\.");
        StringBuilder temp = new StringBuilder();
        for (String frag : ipFra) {
            System.out.println(frag);
            String hexString = Integer.toHexString(Integer.parseInt(frag));
            hexString = hexString.length() == 1 ? 0 + hexString : hexString;
            temp.append(hexString);

        }
        System.out.println(temp);
    }


    private IpUtils() {
        try {
            NOTIFY_URL_PREFIX = "http://" + IpUtils.getRealIp() + ":" + IpUtils.getServerPort();
        } catch (Throwable e) {
            log.warn("加载本机Ip端口号失败", e);
        }
    }

    public static String getNotifyUrlPrefix() {
        if (NOTIFY_URL_PREFIX == null) {
            synchronized (IpUtils.class) {
                if (NOTIFY_URL_PREFIX == null) {
                    try {
                        NOTIFY_URL_PREFIX = "http://" + IpUtils.getRealIp() + ":" + IpUtils.getServerPort();
                    } catch (Throwable e) {
                        log.warn("加载本机Ip端口号失败", e);
                    }
                }
            }
        }
        return NOTIFY_URL_PREFIX;
    }

    /**
     * 获取本机外网IP.
     *
     * @return
     * @throws SocketException
     */
    private static String getRealIp() throws SocketException {
        if (REAL_IP != null) {
            return REAL_IP;
        }
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP

        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                        && !ip.getHostAddress().contains(":")) {// 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }

        if (netip != null && !"".equals(netip)) {
            REAL_IP = netip;
            return netip;
        } else {
            return localip;
        }
    }

    /**
     * 获取当前程序tomcat容器端口号.
     *
     * @return 端口号
     */
    private static Integer getServerPort() {
        if (SERVER_PORT != null) {
            return SERVER_PORT;
        }
        File serverXml = new File(System.getProperty("catalina.home") + FILE_SEPARATOR + "conf" + FILE_SEPARATOR + "server.xml");
        Integer port;
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(serverXml);
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr = xpath.compile
                    ("/Server/Service[@name='Catalina']/Connector[count(@scheme)=0]/@port[1]");
            String result = (String) expr.evaluate(doc, XPathConstants.STRING);
            port = result != null && result.length() > 0 ? Integer.valueOf(result) : null;
        } catch (Exception e) {
            port = null;
        }
        return port;
    }
}
