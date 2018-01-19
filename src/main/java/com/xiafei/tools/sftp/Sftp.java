package com.xiafei.tools.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <P>Description: 操作sftp的工具类（连接池技术）. </P>
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
@ToString
public class Sftp {

    /**
     * ssh默认端口号.
     */
    private static final int DEFAULT_PORT = 22;

    /**
     * 默认sftp下载超时，0为不设限.
     */
    private static final int DEFAULT_SFTP_TIMEOUT = 0;

    /**
     * 连接池.
     */
    private static final LinkedList<ChannelSftp> POOL = new LinkedList<>();

    /**
     * 文件上传线程池，因文件上传速度主要取决于网络及服务器硬盘写速度，所以使用单线程的线程池.
     */
    private static final Executor UPLOAD_THREADS = Executors.newSingleThreadExecutor();
    /**
     * SHELL操作连接池.
     */
    private static Executor SHEEL_THREADS = Executors.newCachedThreadPool();

    /**
     * sftp主机ip.
     */
    private String host;
    /**
     * 端口.
     */
    private Integer port;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码.
     */
    private String password;

    /**
     * 超时时间，秒.
     */
    private Integer timeOut;

    /**
     * 初始和空闲时间连接数.
     */
    private Integer initSize;

    /**
     * 最大连接数.
     */
    private Integer maxSize;

    /**
     * 连接池大小.
     */
    private int poolSize;

    /**
     * 期望连接池中还剩余多少连接.
     */
    private AtomicInteger needSize;

    /**
     * 初始化sftp连接池.
     *
     * @throws JSchException 连接异常
     */
    public Sftp(final String host, final Integer port, final String userName, final String password,
                final Integer timeOut, final Integer initSize, final Integer maxSize) throws JSchException {
        this.host = host;
        this.port = port == null ? DEFAULT_PORT : port;
        this.userName = userName;
        this.password = password;
        this.timeOut = timeOut == null ? DEFAULT_SFTP_TIMEOUT : timeOut;
        this.initSize = initSize;
        this.maxSize = maxSize;
        this.poolSize = initSize;
        this.needSize = new AtomicInteger(0);
        for (int i = 0; i < initSize; i++) {
            POOL.addLast(createChannel());
        }

    }

    /**
     * 从sftp服务器上下载文件并读取成字节流.
     *
     * @param path 文件路径
     * @return 文件并读取成字节流
     */
    public byte[] getBytes(final String path) throws IOException, SftpException {
        ChannelSftp channel = null;
        try {
            channel = fetch();
            final InputStream is = channel.get(path);
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final byte[] buffer = new byte[Constants.SFTP_BUFFER_SIZE];
            int remain;
            while ((remain = is.read(buffer, 0, Constants.SFTP_BUFFER_SIZE)) > 0) {
                bos.write(buffer, 0, remain);
            }
            return bos.toByteArray();
        } finally {
            release(channel);
        }

    }

    /**
     * 下载sftp服务器上文件到本地.
     *
     * @param remotePath 要下载的sftp服务器上文件地址
     * @param localPath  要下载到的本地文件存放地址
     * @throws SftpException sftp各种异常
     */
    public void download(final String remotePath, final String localPath) throws SftpException {
        ChannelSftp channel = null;
        try {
            channel = fetch();
            channel.get(remotePath, localPath);
        } finally {
            release(channel);
        }

    }

    /**
     * 同步上传文件，如果文件已存在则覆盖.
     *
     * @param path  文件路径
     * @param bytes 文件字节数组
     * @throws SftpException 操作异常
     */
    public void uploadSync(final String path, final byte[] bytes) throws SftpException {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = fetch();
            final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            cycleMkDir(path, channelSftp);
            channelSftp.put(bis, path);
        } finally {
            release(channelSftp);
        }
    }

    /**
     * 异步上传文件，如果文件已存在则覆盖.
     *
     * @param path  文件路径
     * @param bytes 文件字节数组
     */
    public void uploadAsync(final String path, final byte[] bytes) {
        UPLOAD_THREADS.execute(() -> {
            try {
                uploadSync(path, bytes);
                log.info("uploadAsync success,path={}", path);
            } catch (SftpException e) {
                log.error("uploadAsync with bytes file operate exception,path={}", path, e);
            } catch (Throwable e) {
                log.error("uploadAsync with bytes uncaught exception,path={}", path, e);
            }
        });
    }


    /**
     * 同步上传文件，如果文件已存在则覆盖（流）.
     *
     * @param path 文件路径
     * @param is   输入流
     */
    public void uploadSync(final String path, final InputStream is) throws SftpException {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = fetch();
            cycleMkDir(path, channelSftp);
            channelSftp.put(is, path);
        } finally {
            release(channelSftp);
        }
    }

    /**
     * 异步上传文件，如果文件已存在则覆盖（流）.
     *
     * @param path 文件路径
     * @param is   输入流
     */
    public void uploadAsync(final String path, final InputStream is) {
        UPLOAD_THREADS.execute(() -> {
            try {
                uploadSync(path, is);
                log.info("uploadAsync success,path={}", path);
            } catch (SftpException e) {
                log.error("uploadAsync with inputStream file operate exception,path={}", path, e);
            } catch (Throwable e) {
                log.error("uploadAsync with inputStream uncaught exception,path={}", path, e);
            }
        });
    }

    /**
     * 同步上传文件，文件来源于本地.
     *
     * @param remotePath 要上传到sftp上的文件路径
     * @param localPath  本地文件路径
     */
    public void uploadSync(final String remotePath, final String localPath) throws SftpException {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = fetch();
            cycleMkDir(remotePath, channelSftp);
            channelSftp.put(localPath, remotePath);
        } finally {
            release(channelSftp);
        }
    }

    /**
     * 异步上传文件，文件来源于本地.
     *
     * @param remotePath 要上传到sftp上的文件路径
     * @param localPath  本地文件路径
     */
    public void uploadAsync(final String remotePath, final String localPath) {
        UPLOAD_THREADS.execute(() -> {
            try {
                uploadSync(remotePath, localPath);
                log.info("uploadAsync success,remotePath={},localPath={}", remotePath, localPath);
            } catch (SftpException e) {
                log.error("uploadAsync with inputStream file operate exception,remotePath={},localPath={}", remotePath,
                        localPath, e);
            } catch (Throwable e) {
                log.error("uploadAsync with inputStream uncaught exception,remotePath={},localPath={}", remotePath,
                        localPath, e);
            }
        });
    }

    /**
     * 删除文件（同步）.
     *
     * @param path 文件路径
     */
    public void removeSync(final String path, final boolean isDirectory) throws SftpException {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = fetch();
            final String directory;
            final String fileName;
            if (path.contains(Constants.FILE_SEPARATOR)) {
                final int lastIndex = path.lastIndexOf(Constants.FILE_SEPARATOR);
                directory = path.substring(0, lastIndex);
                fileName = path.substring(lastIndex + 1);
            } else {
                directory = null;
                fileName = path;
            }
            if (directory != null) {
                channelSftp.cd(directory);
            }
            if (isDirectory) {
                channelSftp.rmdir(fileName);
            } else {
                channelSftp.rm(fileName);
            }
        } finally {
            release(channelSftp);
        }
    }

    /**
     * 删除文件（异步）.
     *
     * @param path 文件路径
     */
    public void removeAsync(final String path, final boolean isDirectory) {
        SHEEL_THREADS.execute(() -> {
            try {
                removeSync(path, isDirectory);
                log.info("removeAsync success,path={}", path);
            } catch (SftpException e) {
                log.error("removeAsync file operate exception,path={}", path, e);
            } catch (Throwable e) {
                log.error("removeAsync uncaught exception,path={}", path, e);
            }

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
                        log.debug("sftp服务器创建文件路径={}", item);
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
     * 从连接池中取出一个连接，无限等待.
     *
     * @return sftp通道
     */
    private ChannelSftp fetch() {
        return fetch(-1);
    }

    /**
     * 从连接池中取出一个连接，等待超时时间后返回null.
     *
     * @param millis 超时时间，毫秒
     * @return sftp通道
     */
    private ChannelSftp fetch(long millis) {
        needSize.incrementAndGet();
        synchronized (POOL) {
            try {

                if (millis <= 0) {
                    // 无限等待
                    while (POOL.isEmpty()) {
                        if (poolSize == maxSize) {
                            try {
                                POOL.wait();
                            } catch (InterruptedException e) {
                                log.warn("从连接池获取sftp通道时线程被通知中断等待,sftp={}", this);
                            }
                        } else {
                            // 如果连接池大小没有达到最大连接数，新增一个连接
                            try {
                                return add();
                            } catch (JSchException e) {
                                log.error("获取更多连接失败");
                            }
                        }

                    }
                    return reconnectIfExpire(POOL.removeFirst());
                } else {
                    // 超时等待
                    long future = System.currentTimeMillis() + millis;
                    long remain = millis;
                    while (POOL.isEmpty() && remain >= 0) {
                        if (poolSize == maxSize) {
                            try {
                                POOL.wait();
                            } catch (InterruptedException e) {
                                log.warn("从连接池获取sftp通道时线程被通知中断等待,sftp={}", this);
                            }
                            remain = future - System.currentTimeMillis();
                        } else {
                            // 如果连接池大小没有达到最大连接数，新增一个连接
                            try {
                                return add();
                            } catch (JSchException e) {
                                log.error("获取更多连接失败");
                            }
                        }
                    }
                    if (!POOL.isEmpty()) {
                        return reconnectIfExpire(POOL.removeFirst());
                    }
                    //超时返回null
                    return null;
                }
            } finally {
                needSize.decrementAndGet();
                log.info("当前连接池数据={}", this);
            }
        }
    }

    /**
     * 将连接释放到连接池.
     *
     * @param channel sftp通道
     */
    private void release(final ChannelSftp channel) {
        if (channel != null) {
            synchronized (POOL) {
                if (!POOL.isEmpty() && poolSize > initSize && needSize.get() < POOL.size()) {
                    remove();
                    closeChannel(channel);
                } else {
                    POOL.addLast(channel);
                }
                POOL.notify();
                log.info("已释放，当前连接池数据={}", this);
            }
        }
    }

    /**
     * 新增一个连接，是否需要放入连接池在释放的时候做判断.
     */
    private ChannelSftp add() throws JSchException {
        poolSize++;
        return createChannel();
    }

    /**
     * 从数据库连接池中删除一个连接.
     */
    private void remove() {
        poolSize--;
    }

    /**
     * 如果通道不可用，重新连接.
     *
     * @param channel 通道
     * @return 可用通道
     */
    private ChannelSftp reconnectIfExpire(ChannelSftp channel) {
        try {
            // 测试连接是否可用
            channel.pwd();
            return channel;
        } catch (SftpException e) {
            log.warn("连接超时被服务器断开，重新连接");
            try {
                closeChannel(channel);
                return createChannel();
            } catch (JSchException e1) {
                log.error("重新连接失败");
                return null;
            }
        }
    }


    /**
     * 创建sftp通道.
     *
     * @return sftp通道
     */
    private ChannelSftp createChannel() throws JSchException {
        final Integer ftpPort = port == null ? DEFAULT_PORT : port;
        JSch jsch = new JSch(); // 创建JSch对象
        Session session = jsch.getSession(userName, host, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
        log.debug("sftp Session created. {}", this);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeOut); // 设置下载超时时间，单位毫秒
        session.setPassword(password); // 设置密码
        session.connect(); // 通过Session建立链接
        log.debug("Session connected.");
        log.debug("Opening Channel.");
        Channel channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        log.debug("Connected successfully.{}", this);
        return (ChannelSftp) channel;
    }

    /**
     * 关闭通道和会话.
     */
    private static void closeChannel(final ChannelSftp channel) {
        try {

            if (channel != null) {
                final Session session = channel.getSession();
                if (session != null) {
                    session.disconnect();
                }
                channel.disconnect();
            }
        } catch (Throwable e) {
            log.error("关闭sftp连接失败,hannel={}", channel, e);
        }
    }

}