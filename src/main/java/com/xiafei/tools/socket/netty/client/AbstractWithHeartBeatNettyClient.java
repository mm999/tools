package com.xiafei.tools.socket.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * <P>Description: Netty客户端（包含心跳服务）抽象类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/9/11</P>
 * <P>UPDATE DATE: 2017/9/11</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public abstract class AbstractWithHeartBeatNettyClient {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWithHeartBeatNettyClient.class);

    /**
     * 连接断开是否重连.
     */
    private static final boolean IS_CONNECT = true;

    /**
     * 建立一个包含心跳服务的socket长连接.
     */
    public void connect() {

        // 客户端有一个group就够了,group的构造参数里可以指定线程池线程数量，若不传参会取默认线程数
        // MultithreadEventLoopGroup.DEFAULT_EVENT_LOOP_THREADS ，这个属性是静态变量，在虚拟机初始化
        // NioEventLoopGroup这个类的时候初始化，取值是Runtime.getRuntime().availableProcessors()，
        // 取的是虚拟机可用逻辑cpu数量。
        final EventLoopGroup group = new NioEventLoopGroup();
        // Netty 应用程序通过设置 bootstrap（引导）类的开始，该类提供了一个 用于应用程序网络层配置的容器
        final Bootstrap boot = new Bootstrap();
        // 初始化工作者、channel、日志
        boot.group(group).
                channel(NioSocketChannel.class).
                handler(new LoggingHandler(LogLevel.INFO));

        // 初始化连接检测狗，并实现handlers()方法，方便初始化pipeLine和重连
        final AbstractConnectionWatchdog watchdog = new AbstractConnectionWatchdog(boot, new HashedWheelTimer(),
                getPort(), getHost(), IS_CONNECT) {

            @Override
            public ChannelHandler[] handlers() {
                // 公共Handler
                final ChannelHandler[] pubHandlers = new ChannelHandler[]{
                        // 连接检测狗本身
                        this,
                        // 空闲状态检测handler，用于客户端心跳服务，应设置writerIdlerTime，
                        // 当空闲的时候会繁殖一个userEventTriggered事件，下方handler接收处理
                        new IdleStateHandler(0L, getHeartBeatInterval(), 0L, TimeUnit.SECONDS),
                        // 监听空闲状态事件，捕捉到写空闲超过getHeartBeatInterval()秒后发送心跳报文
                        new ConnectorIdleStateTrigger(getHeartBeatStr(), getServerCharset(), getHost(), getPort()),
                        // 解码，附带解决粘包问题
                        new PacketDecoder(getServerCharset()),
                        // 编码
                        new StringEncoder(getClientCharset()),
                };

                // 业务Handler
                final ChannelHandler[] bizHandlers = getBizHandlers();
                // 所有的Handler
                final ChannelHandler[] handlers = new ChannelHandler[pubHandlers.length + bizHandlers.length];

                // 先copy公共handler
                System.arraycopy(pubHandlers, 0, handlers, 0, pubHandlers.length);
                // 再copy业务handler
                System.arraycopy(bizHandlers, 0, handlers, pubHandlers.length, bizHandlers.length);

                return handlers;
            }

        };

        final ChannelFuture future;
        //进行连接
        synchronized (boot) {

            boot.handler(new ChannelInitializer<Channel>() {

                //初始化channel
                @Override
                protected void initChannel(final Channel ch) throws Exception {
                    ch.pipeline().addLast(watchdog.handlers());
                }
            });
            future = boot.connect(getHost(), getPort());
        }

        // 以下代码在synchronized同步块外面是安全的
        try {
            future.sync();
        } catch (InterruptedException e) {
            LOGGER.error("{}:{}客户端netty连接启动异常", getHost(), getPort(), e);
        }
        LOGGER.info("{}:{}客户端netty连接启动成功", getHost(), getPort());
    }

    /**
     * 获得服务器端口号，由子类去重写.
     *
     * @return 服务器端口号
     */
    protected abstract int getPort();

    /**
     * 获得服务器IP地址，由子类去重写.
     *
     * @return 服务器IP地址字符串
     */
    protected abstract String getHost();

    /**
     * 获得服务器编码类型.
     *
     * @return 服务器的编码类型
     */
    protected abstract Charset getServerCharset();

    /**
     * 获得客户端编码类型.
     *
     * @return 客户端的编码类型
     */
    protected abstract Charset getClientCharset();

    /**
     * 获得业务处理的handler数组.
     *
     * @return 业务处理相关的Handler数组，不允许为空.
     */
    protected abstract ChannelHandler[] getBizHandlers();

    /**
     * 获得心跳报文，由子类决定.
     *
     * @return 心跳报文字符串内容
     */
    protected abstract String getHeartBeatStr();

    /**
     * 获得心跳报文发送间隔，由子类决定.
     *
     * @return 心跳报文发送间隔，单位s
     */
    protected abstract long getHeartBeatInterval();
}
