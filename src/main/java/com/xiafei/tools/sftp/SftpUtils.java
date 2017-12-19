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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
     * 文件上传线程池，因文件上传速度主要取决于网络及服务器硬盘写速度，所以使用单线程的线程池.
     */
    private static final Executor THREADS = Executors.newSingleThreadExecutor();

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
     * 同步上传文件，如果文件已存在则覆盖.
     *
     * @param properties sftp配置
     * @param path       文件路径
     * @param bytes      文件字节数组
     * @throws JSchException 连接异常
     * @throws SftpException 操作异常
     */
    public static void uploadSync(final SftpProperties properties, final String path, final byte[] bytes)
            throws JSchException, SftpException {

        try {
            connect(properties.getHost(), properties.getPort(), properties.getUserName(), properties.getPassword());
            final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            final ChannelSftp channelSftp = CHANNEL_SFTP_THREAD_LOCAL.get();
            cycleMkDir(path, channelSftp);

            CHANNEL_SFTP_THREAD_LOCAL.get().put(bis, path);
        } finally {
            closeChannel();
        }
    }

    /**
     * 异步上传文件，如果文件已存在则覆盖.
     *
     * @param properties sftp配置
     * @param path       文件路径
     * @param bytes      文件字节数组
     */
    public static void uploadAsync(final SftpProperties properties, final String path, final byte[] bytes) {
        THREADS.execute(() -> {
            try {
                uploadSync(properties, path, bytes);
            } catch (JSchException e) {
                log.error("uploadAsync with bytes connect exception,path={}", path, e);
            } catch (SftpException e) {
                log.error("uploadAsync with bytes file operate exception,path={}", path, e);
            } catch (Throwable e) {
                log.error("uploadAsync with bytes uncaught exception,path={}", path, e);
            }
            log.info("uploadAsync success,path={}", path);
        });
    }

    /**
     * 同步上传文件，如果文件已存在则覆盖（流）.
     *
     * @param properties sftp配置
     * @param path       文件路径
     * @param is         输入流
     * @throws JSchException 连接异常
     * @throws SftpException 操作异常
     */
    public static void uploadSync(final SftpProperties properties, final String path, final InputStream is) throws JSchException, SftpException {
        try {
            connect(properties.getHost(), properties.getPort(), properties.getUserName(), properties.getPassword());
            final ChannelSftp channelSftp = CHANNEL_SFTP_THREAD_LOCAL.get();
            cycleMkDir(path, channelSftp);
            CHANNEL_SFTP_THREAD_LOCAL.get().put(is, path);
        } finally {
            closeChannel();
        }
    }

    /**
     * 异步上传文件，如果文件已存在则覆盖（流）.
     *
     * @param properties sftp配置
     * @param path       文件路径
     * @param is         输入流
     */
    public static void uploadAsync(final SftpProperties properties, final String path, final InputStream is) {
        THREADS.execute(() -> {
            try {
                uploadSync(properties, path, is);
            } catch (JSchException e) {
                log.error("uploadAsync with inputStream connect exception,path={}", path, e);
            } catch (SftpException e) {
                log.error("uploadAsync with inputStream file operate exception,path={}", path, e);
            } catch (Throwable e) {
                log.error("uploadAsync with inputStream uncaught exception,path={}", path, e);
            }
            log.info("uploadAsync success,path={}", path);
        });
    }

    /**
     * 删除文件（同步）.
     *
     * @param properties sftp配置
     * @param path       文件路径
     */
    public static void removeSync(final SftpProperties properties, final String path) throws JSchException, SftpException {
        try {
            connect(properties.getHost(), properties.getPort(), properties.getUserName(), properties.getPassword());
            final String directory;
            final String fileName;
            if (path.contains(Constants.FILE_SEPARATOR)) {
                final int lastIndex = path.lastIndexOf(Constants.FILE_SEPARATOR);
                directory = path.substring(0, lastIndex);
                fileName = path.substring(lastIndex);
            } else {
                directory = null;
                fileName = path;
            }
            if (directory != null) {
                CHANNEL_SFTP_THREAD_LOCAL.get().cd(directory);
            }
            CHANNEL_SFTP_THREAD_LOCAL.get().rm(fileName);
        } finally {
            closeChannel();
        }
    }

    /**
     * 删除文件（异步）.
     *
     * @param properties sftp配置
     * @param path       文件路径
     */
    public static void removeAsync(final SftpProperties properties, final String path) throws JSchException, SftpException {
        THREADS.execute(() -> {
            try {
                removeSync(properties, path);
            } catch (JSchException e) {
                log.error("removeAsync connect exception,path={}", path, e);
            } catch (SftpException e) {
                log.error("removeAsync file operate exception,path={}", path, e);
            } catch (Throwable e) {
                log.error("removeAsync uncaught exception,path={}", path, e);
            }
            log.info("removeAsync success,path={}", path);
        });
    }

    /**
     * 循环创建目录.
     *
     * @param path        文件全路径
     * @param channelSftp sftp通道
     * @throws SftpException sftp异常
     */
    private static void cycleMkDir(final String path, final ChannelSftp channelSftp) throws SftpException {
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
