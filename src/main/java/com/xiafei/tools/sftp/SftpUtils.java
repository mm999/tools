package com.xiafei.tools.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.xiafei.tools.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    private static final ThreadLocal<Session> SESSION_THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<ChannelSftp> CHANNEL_SFTP_THREAD_LOCAL = new ThreadLocal<>();


    /**
     * 工具类不允许实例化.
     */
    private SftpUtils() {
    }

    /**
     * 从sftp服务器上下载文件并读取成字节流.
     *
     * @param properties sftp配置
     * @param path       文件路径
     * @return 文件并读取成字节流
     * @throws JSchException 各种异常
     */
    public static byte[] getBytes(final SftpProperties properties, final String path) throws JSchException,
            IOException, SftpException {
        try {
            connect(properties.getHost(), properties.getPort(), properties.getUserName(), properties.getPassword());
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final InputStream is = CHANNEL_SFTP_THREAD_LOCAL.get().get(path);
            final byte[] buffer = new byte[Constants.SFTP_BUFFER_SIZE];
            int remain;
            while ((remain = is.read(buffer, 0, Constants.SFTP_BUFFER_SIZE)) > 0) {
                bos.write(buffer, 0, remain);
            }
            return bos.toByteArray();
        } finally {
            closeChannel();
        }

    }

    /**
     * 上传文件，如果文件已存在则覆盖.
     *
     * @param properties sftp配置
     * @param path       文件路径
     * @throws JSchException 各种异常
     */
    public static void upload(final SftpProperties properties, final String path, final byte[] bytes) throws JSchException, SftpException {

        try {
            connect(properties.getHost(), properties.getPort(), properties.getUserName(), properties.getPassword());
            final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            final ChannelSftp channelSftp = CHANNEL_SFTP_THREAD_LOCAL.get();
            if (path.contains(Constants.FILE_SEPARATOR)) {

                final String[] fullItem = path.split(Constants.FILE_SEPARATOR);
                if (path.startsWith(Constants.FILE_SEPARATOR)) {
                    fullItem[0] = Constants.FILE_SEPARATOR.concat(fullItem[0]);
                }
                //pathItem最后一项是文件名，剔除
                final String[] pathItem = new String[fullItem.length - 1];
                System.arraycopy(fullItem, 0, pathItem, 0, fullItem.length - 1);
                // 遍历文件路径，递归创建文件夹
                for (String item : pathItem) {
                    try {
                        channelSftp.cd(item);
                    } catch (SftpException sException) {
                        if (ChannelSftp.SSH_FX_NO_SUCH_FILE == sException.id) {
                            log.info("sftp服务器创建文件路径={}", item);
                            channelSftp.mkdir(item);
                            channelSftp.cd(item);
                        } else {
                            log.error("sftp.cd 报错", sException);
                        }
                    }
                }

            }

            CHANNEL_SFTP_THREAD_LOCAL.get().put(bis, path);
        } finally {
            closeChannel();
        }
    }

    /**
     * 删除文件.
     *
     * @param properties sftp配置
     * @param path       文件路径
     */
    public static void remove(final SftpProperties properties, final String path) throws JSchException, SftpException {
        try {
            connect(properties.getHost(), properties.getPort(), properties.getUserName(), properties.getPassword());
            CHANNEL_SFTP_THREAD_LOCAL.get().rm(path);
        } finally {
            closeChannel();
        }
    }

    /**
     * 连接sftp.
     *
     * @param connectionInfo 连接信息
     * @param timeout        超时时间
     * @return sftp通道
     * @throws JSchException jsch的异常
     */
    private static void connect(final SftpConnectionInfo connectionInfo, final int timeout) throws JSchException {

        final Integer ftpPort = connectionInfo.getPort() == null ? DEFAULT_PORT : connectionInfo.getPort();

        JSch jsch = new JSch(); // 创建JSch对象
        Session session = jsch.getSession(connectionInfo.getUserName(), connectionInfo.getHost(), ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
        SESSION_THREAD_LOCAL.set(session);
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
        CHANNEL_SFTP_THREAD_LOCAL.set((ChannelSftp) channel);
        log.debug("Connected successfully,connectionInfo = {}", JSONUtil.toJson(connectionInfo));
    }


    /**
     * 建立sftp连接.
     *
     * @param host     服务器ip或域名
     * @param port     端口号
     * @param userName 用户名
     * @param password 密码
     * @return sftp通道
     * @throws JSchException 各种异常
     */
    private static void connect(final String host, final Integer port, final String userName,
                                final String password) throws JSchException {
        final SftpUtils.SftpConnectionInfo connectionInfo = new SftpUtils.SftpConnectionInfo();
        connectionInfo.setHost(host);
        connectionInfo.setPort(port);
        connectionInfo.setUserName(userName);
        connectionInfo.setPassword(password);
        connect(connectionInfo, 10000);
    }

    /**
     * 关闭通道.
     */
    private static void closeChannel() {
        try {

            if (CHANNEL_SFTP_THREAD_LOCAL.get() != null) {
                CHANNEL_SFTP_THREAD_LOCAL.get().disconnect();
                CHANNEL_SFTP_THREAD_LOCAL.remove();
            }
            if (SESSION_THREAD_LOCAL.get() != null) {
                SESSION_THREAD_LOCAL.get().disconnect();
                SESSION_THREAD_LOCAL.remove();
            }
        } catch (Throwable e) {
            log.error("关闭sftp连接失败", e);
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
