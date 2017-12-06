package com.xiafei.tools.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.xiafei.tools.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * <P>Description: 操作sftp的工具类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/10/17</P>
 * <P>UPDATE DATE: 2017/10/17</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@Slf4j
public class SftpUtils {

    /**
     * ssh默认端口号.
     */
    private static final int DEFAULT_PORT = 22;

    /**
     * 工具类不允许实例化.
     */
    private SftpUtils() {
    }

    /**
     * 获取sftp通道.
     *
     * @param host     服务器ip或域名
     * @param port     端口号
     * @param userName 用户名
     * @param password 密码
     * @return sftp通道
     * @throws JSchException 各种异常
     */
    public static ChannelSftp getChannel(final String host, final Integer port, final String userName,
                                         final String password) throws JSchException {
        final SftpUtils.SftpConnectionInfo connectionInfo = new SftpUtils.SftpConnectionInfo();
        connectionInfo.setHost(host);
        connectionInfo.setPort(port);
        connectionInfo.setUserName(userName);
        connectionInfo.setPassword(password);

        return getChannel(connectionInfo, 10000);
    }

    /**
     * 获取sftp通道.
     *
     * @param connectionInfo 连接信息
     * @param timeout        超时时间
     * @return sftp通道
     * @throws JSchException jsch的异常
     */
    private static ChannelSftp getChannel(final SftpConnectionInfo connectionInfo, final int timeout) throws JSchException {

        final Integer ftpPort = connectionInfo.getPort() == null ? DEFAULT_PORT : connectionInfo.getPort();

        JSch jsch = new JSch(); // 创建JSch对象
        Session session = jsch.getSession(connectionInfo.getUserName(), connectionInfo.getHost(), ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
        log.debug("sftp Session created. connectInfo={}", JSONUtil.toJson(connectionInfo));
        if (connectionInfo.getPassword() != null) {
            session.setPassword(connectionInfo.getPassword()); // 设置密码
        }

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        log.debug("Session connected.");

        log.debug("Opening Channel.");
        Channel channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        log.debug("Connected successfully,connectionInfo = {}", JSONUtil.toJson(connectionInfo));
        return (ChannelSftp) channel;
    }

    /**
     * 关闭通道.
     *
     * @param channel 通道
     * @param session 会话
     * @throws Exception
     */
    public void closeChannel(final Channel channel, final Session session) throws Exception {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

    /**
     * 连接信息封装对象.
     */
    public static class SftpConnectionInfo {

        /**
         * 服务器ip.
         */
        private String host;

        /**
         * ssh服务端口号.
         */
        private Integer port;

        /**
         * 用户名.
         */
        private String userName;

        /**
         * 密码.
         */
        private String password;

        public String getHost() {
            return host;
        }

        public void setHost(final String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(final Integer port) {
            this.port = port;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(final String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(final String password) {
            this.password = password;
        }
    }
}
