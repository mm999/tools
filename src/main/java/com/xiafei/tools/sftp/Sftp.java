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
import java.util.concurrent.atomic.AtomicBoolean;
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
     * sftp缓冲区的大小（8K）.
     */
    private static final int SFTP_BUFFER_SIZE = 1024 << 3;

    /**
     * 文件分隔符，正斜杠.
     */
    private static final String FILE_SEPARATOR_FORWARD = "/";

    /**
     * 文件分隔符，反斜杠.
     */
    private static final String FILE_SEPARATOR_BACK = "\\";

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
    private final LinkedList<ChannelSftp> pool = new LinkedList<>();

    /**
     * 文件上传线程池，因文件上传速度主要取决于网络及服务器硬盘写速度，所以使用单线程的线程池.
     */
    private final Executor uploadExecutor = Executors.newSingleThreadExecutor();
    /**
     * SHELL操作连接池.
     */
    private Executor sheelExecutor = Executors.newCachedThreadPool();

    private String logPrefix;

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
     * 是否初始化成功.
     */
    private AtomicBoolean inited = new AtomicBoolean(false);

    /**
     * 初始化sftp连接池.
     *
     * @param host     sftp服务器ip地址
     * @param port     sftp服务器端口号
     * @param userName sftp用户名
     * @param password sftp密码
     * @param timeOut  建立连接超时
     * @param initSize 初始化连接池连接数量
     * @param maxSize  连接池最大连接数量
     */
    public Sftp(final String host, final Integer port, final String userName, final String password,
                final Integer timeOut, final Integer initSize, final Integer maxSize) {
        this.host = host;
        this.port = port == null ? DEFAULT_PORT : port;
        this.userName = userName;
        this.password = password;
        this.timeOut = timeOut == null ? DEFAULT_SFTP_TIMEOUT : timeOut;
        this.initSize = initSize;
        this.maxSize = maxSize;
        this.poolSize = initSize;
        this.needSize = new AtomicInteger(0);
        logPrefix = userName + "@" + host + ":" + port + ",";
        initPool();
    }

    /**
     * 从sftp服务器上下载文件并读取成字节流.
     *
     * @param path     文件路径
     * @param serialNo 业务流水号，日志用
     * @return 文件并读取成字节流
     */
    public byte[] getBytes(final String path, final String serialNo) {
        ChannelSftp channel = null;
        try {
            channel = fetch();
            try (InputStream is = channel.get(path);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                final byte[] buffer = new byte[SFTP_BUFFER_SIZE];
                int remain;
                while ((remain = is.read(buffer, 0, SFTP_BUFFER_SIZE)) > 0) {
                    bos.write(buffer, 0, remain);
                }
                return bos.toByteArray();
            } catch (SftpException e) {
                log.error("[{}]{}sftp操作异常，获取path={}文件的输出流失败", serialNo, logPrefix, path, e);
                throw new RuntimeException(e);
            } catch (IOException e) {
                log.error("[{}]{}输出流关闭失败", serialNo, logPrefix, e);
                throw new RuntimeException(e);
            }

        } finally {
            release(channel);
        }

    }

    /**
     * 从sftp服务器上获取文件的输入流.
     *
     * @param path     文件路径
     * @param serialNo 业务流水号，日志用
     * @return 文件的输入流
     */
    public InputStream getStream(final String path, final String serialNo) {
        ChannelSftp channel = null;
        try {
            channel = fetch();
            return channel.get(path);
        } catch (SftpException e) {
            log.error("[{}]{}sftp操作异常，获取path={}文件的输出流失败", serialNo, logPrefix, path, e);
            throw new RuntimeException(e);
        } finally {
            release(channel);
        }
    }

    /**
     * 下载sftp服务器上文件到本地.
     *
     * @param remotePath 要下载的sftp服务器上文件地址
     * @param localPath  要下载到的本地文件存放地址
     * @param serialNo   业务流水号，日志用
     */
    public void download(final String remotePath, final String localPath, final String serialNo) {
        ChannelSftp channel = null;
        try {
            channel = fetch();
            channel.get(remotePath, localPath);
        } catch (SftpException e) {
            log.error("[{}]{}sftp操作异常，将目标sftp上path={}的文件下载到本地={}路径下失败", serialNo, logPrefix, remotePath, localPath, e);
            throw new RuntimeException(e);
        } finally {
            release(channel);
        }

    }

    /**
     * 同步上传文件，如果文件已存在则覆盖.
     *
     * @param path     文件路径
     * @param bytes    文件字节数组
     * @param serialNo 业务流水号，记日志用，可以为空
     */
    public void uploadSync(final String path, final byte[] bytes, final String serialNo) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = fetch();
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
                cycleMkDir(path, channelSftp);
                channelSftp.put(bis, path);
                log.info("[{}]{}上传成功,path={}", serialNo, logPrefix, path);
            } catch (IOException e) {
                log.error("[{}]{}输出流关闭失败", serialNo, logPrefix, e);
                throw new RuntimeException(e);
            } catch (SftpException e) {
                log.error("[{}]{}sftp操作异常，上传文件到path={}的路径下失败", serialNo, logPrefix, path, e);
                throw new RuntimeException(e);
            }

        } finally {
            release(channelSftp);
        }
    }

    /**
     * 异步上传文件，如果文件已存在则覆盖.
     *
     * @param path     文件路径
     * @param bytes    文件字节数组
     * @param serialNo 业务流水号，记日志用，可以为空
     */
    public void uploadAsync(final String path, final byte[] bytes, final String serialNo) {
        uploadExecutor.execute(() -> {
            try {
                uploadSync(path, bytes, serialNo);
            } catch (Throwable e) {
                log.error("[{}]{}uploadAsync with bytes uncaught exception,path={}", serialNo, logPrefix, path, e);
            }
        });
    }


    /**
     * 同步上传文件，如果文件已存在则覆盖（流）.
     *
     * @param path     文件路径
     * @param is       输入流
     * @param serialNo 业务流水号，记日志用，可以为空
     */
    public void uploadSync(final String path, final InputStream is, final String serialNo) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = fetch();
            cycleMkDir(path, channelSftp);
            channelSftp.put(is, path);
            log.info("[{}]{}[upload by inputStream success],path={}", serialNo, logPrefix, path);
        } catch (SftpException e) {
            log.error("[{}]{}sftp操作异常，上传文件到path={}的路径下失败", serialNo, logPrefix, path, e);
            throw new RuntimeException(e);
        } finally {
            release(channelSftp);
        }
    }

    /**
     * 异步上传文件，如果文件已存在则覆盖（流）.
     *
     * @param path     文件路径
     * @param is       输入流
     * @param serialNo 业务流水号，记日志用，可以为空
     */
    public void uploadAsync(final String path, final InputStream is, final String serialNo) {
        uploadExecutor.execute(() -> {
            try {
                uploadSync(path, is, serialNo);
            } catch (Throwable e) {
                log.error("[{}]{}uploadAsync with inputStream uncaught exception,path={}", serialNo, logPrefix, path, e);
            }
        });
    }

    /**
     * 同步上传文件，文件来源于本地.
     *
     * @param remotePath 要上传到sftp上的文件路径
     * @param localPath  本地文件路径
     * @param serialNo   业务流水号，记日志用，可以为空
     */
    public void uploadSync(final String remotePath, final String localPath, final String serialNo) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = fetch();
            cycleMkDir(remotePath, channelSftp);
            channelSftp.put(localPath, remotePath);
            log.info("[{}]{}[upload by localPath success],path={}", serialNo, logPrefix, remotePath);
        } catch (SftpException e) {
            log.error("[{}]{}sftp操作异常，将本地文件={}上传到sftp={}的路径下失败", serialNo, logPrefix, localPath, remotePath, e);
            throw new RuntimeException(e);
        } finally {
            release(channelSftp);
        }
    }

    /**
     * 异步上传文件，文件来源于本地.
     *
     * @param remotePath 要上传到sftp上的文件路径
     * @param localPath  本地文件路径
     * @param serialNo   业务流水号，记日志用，可以为空
     */
    public void uploadAsync(final String remotePath, final String localPath, final String serialNo) {
        uploadExecutor.execute(() -> {
            try {
                uploadSync(remotePath, localPath, serialNo);
            } catch (Throwable e) {
                log.error("[{}]{}uploadAsync with inputStream uncaught exception,remotePath={},localPath={}", serialNo, logPrefix, remotePath,
                        localPath, e);
            }
        });
    }

    /**
     * 删除文件（同步）.
     *
     * @param path        文件路径
     * @param isDirectory 是否是目录
     * @param serialNo    业务流水号，记日志用，可以为空
     */
    public void removeSync(final String path, final boolean isDirectory, final String serialNo) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = fetch();
            final String directory;
            final String fileName;
            if (path.contains(FILE_SEPARATOR_FORWARD)) {
                final int lastIndex = path.lastIndexOf(FILE_SEPARATOR_FORWARD);
                directory = path.substring(0, lastIndex);
                fileName = path.substring(lastIndex + 1);
            } else if (path.contains(FILE_SEPARATOR_BACK)) {
                final int lastIndex = path.lastIndexOf(FILE_SEPARATOR_BACK);
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
            log.info("[{}]{}[remove success],path={}", serialNo, logPrefix, path);

        } catch (SftpException e) {
            log.error("[{}]{}sftp操作异常，删除sftp上path={}的文件失败", serialNo, logPrefix, path, e);
            throw new RuntimeException(e);
        } finally {
            release(channelSftp);
        }
    }

    /**
     * 删除文件（异步）.
     *
     * @param path        文件路径
     * @param isDirectory 是否是目录
     * @param serialNo    业务流水号，记日志用，可以为空
     */
    public void removeAsync(final String path, final boolean isDirectory, final String serialNo) {
        sheelExecutor.execute(() -> {
            try {
                removeSync(path, isDirectory, serialNo);
            } catch (Throwable e) {
                log.error("[{}]{}removeAsync uncaught exception,path={}", serialNo, logPrefix, path, e);
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
    private void cycleMkDir(final String path, final ChannelSftp channelSftp) {
        if (path.contains(FILE_SEPARATOR_FORWARD)) {
            cycleMkDir(path, channelSftp, FILE_SEPARATOR_FORWARD);
        } else if (path.contains(FILE_SEPARATOR_BACK)) {
            cycleMkDir(path, channelSftp, FILE_SEPARATOR_BACK);
        }

    }

    /**
     * 循环创建目录.
     *
     * @param path        文件全路径
     * @param channelSftp sftp通道
     * @param separator   分隔符
     */
    private void cycleMkDir(final String path, final ChannelSftp channelSftp, final String separator) {
        final String[] fullItem = path.split(separator);
        if (path.startsWith(separator)) {
            fullItem[0] = separator.concat(fullItem[0]);
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
                    log.debug("{}sftp服务器创建文件路径={}", logPrefix, item);
                    try {
                        channelSftp.mkdir(item);
                        channelSftp.cd(item);
                    } catch (SftpException e) {
                        log.error("{}sftp服务器创建文件路径={}失败", logPrefix, item);
                        throw new RuntimeException(e);
                    }

                } else {
                    log.error("{}sftp.cd 报错", logPrefix, sException);
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
        if (!inited.get() && inited.compareAndSet(false, true)) {
            if (!initPool()) {
                throw new RuntimeException("重新初始化sftp连接池报错" + logPrefix);
            }
        }
        needSize.incrementAndGet();
        synchronized (pool) {
            try {

                if (millis <= 0) {
                    // 无限等待
                    while (pool.isEmpty()) {
                        if (poolSize == maxSize) {
                            try {
                                pool.wait();
                            } catch (InterruptedException e) {
                                log.warn("从连接池获取sftp通道时线程被通知中断等待,sftp={}", this);
                            }
                        } else {
                            // 如果连接池大小没有达到最大连接数，新增一个连接
                            try {
                                return add();
                            } catch (JSchException e) {
                                log.error("{}获取更多连接失败", logPrefix);
                            }
                        }

                    }
                    return reconnectIfExpire(pool.removeFirst());
                } else {
                    // 超时等待
                    long future = System.currentTimeMillis() + millis;
                    long remain = millis;
                    while (pool.isEmpty() && remain >= 0) {
                        if (poolSize == maxSize) {
                            try {
                                pool.wait();
                            } catch (InterruptedException e) {
                                log.warn("从连接池获取sftp通道时线程被通知中断等待,sftp={}", this);
                            }
                            remain = future - System.currentTimeMillis();
                        } else {
                            // 如果连接池大小没有达到最大连接数，新增一个连接
                            try {
                                return add();
                            } catch (JSchException e) {
                                log.error("{}获取更多连接失败", logPrefix);
                            }
                        }
                    }
                    if (!pool.isEmpty()) {
                        return reconnectIfExpire(pool.removeFirst());
                    }
                    //超时返回null
                    return null;
                }
            } finally {
                needSize.decrementAndGet();
                log.debug("当前连接池数据={}", this);
            }
        }
    }

    /**
     * 释放sftp通道，根据连接池中当前线程数量情况决定关闭通道还是放回到连接池.
     * 如果连接池的当前连接数大于空闲线程并且实际需要的线程数量小于当前线程数量，则从连接池中移除这个连接。
     * 否则将连接放回连接池。
     * 这里“实际需要的线程数量”由一个AtomicInteger保存，在有线程企图获取连接（不论是否已经拿到）时自增，当线程使用完毕
     *
     * @param channel sftp通道
     */
    private void release(final ChannelSftp channel) {
        if (channel != null) {
            synchronized (pool) {
                if (!pool.isEmpty() && poolSize > initSize && needSize.get() < pool.size()) {
                    remove();
                    closeChannel(channel);
                } else {
                    pool.addLast(channel);
                }
                pool.notify();
                log.debug("已释放，当前连接池数据={}", this);
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
     * @return 可用通道，如果重连失败返回null
     */
    private ChannelSftp reconnectIfExpire(ChannelSftp channel) {
        try {
            // 测试连接是否可用
            channel.pwd();
            return channel;
        } catch (SftpException e) {
            log.warn("{}连接超时被服务器断开，重新连接", logPrefix);
            try {
                closeChannel(channel);
                return createChannel();
            } catch (JSchException e1) {
                log.error("{}重新连接失败", logPrefix);
                return null;
            }
        }
    }

    /**
     * 创建sftp通道.
     *
     * @return sftp通道
     * @throws JSchException sftp通道建立失败
     */
    private ChannelSftp createChannel() throws JSchException {
        final Integer ftpPort = port == null ? DEFAULT_PORT : port;
        final JSch jsch = new JSch(); // 创建JSch对象
        final Session session = jsch.getSession(userName, host, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
        log.debug("sftp Session created. {}", this);
        final Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeOut); // 设置下载超时时间，单位毫秒
        session.setPassword(password); // 设置密码
        session.connect(); // 通过Session建立链接
        log.debug("Session connected.");
        log.debug("Opening Channel.");
        final Channel channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        log.debug("Connected successfully.{}", this);
        return (ChannelSftp) channel;
    }

    /**
     * 关闭通道和会话.
     *
     * @param channel sftp通道
     */
    private void closeChannel(final ChannelSftp channel) {
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

    /**
     * 初始化sftp连接池.
     *
     * @return 是否初始化成功
     */
    private boolean initPool() {
        try {
            for (int i = 0; i < initSize; i++) {

                pool.addLast(createChannel());

            }
            log.info("{}初始化sftp成功", logPrefix);
            inited.set(true);
            return true;
        } catch (JSchException e) {
            inited.set(false);
            log.error("{}初始化sftp报错", logPrefix, e);
            return false;

        }
    }

}
