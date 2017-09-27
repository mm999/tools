package com.xiafei.tools.socket.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * <P>Description: 连接空闲状态触发器，用于发送心跳报文.</P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/7</P>
 * <P>UPDATE DATE: 2017/7/7</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@ChannelHandler.Sharable
public class ConnectorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    /**
     * 日志.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorIdleStateTrigger.class);

    /**
     * 心跳请求序列.
     */
    private final ByteBuf heartbeatSequence;

    /**
     * 日志前缀.
     */
    private final String logPrefix;

    /**
     * 类构造器.
     *
     * @param heartBeatStr  心跳报文字符串内容
     * @param serverCharset 服务器的编码
     * @param host          服务器IP地址
     * @param port          服务器端口号
     */
    ConnectorIdleStateTrigger(final String heartBeatStr, final Charset serverCharset, final String host,
                              final int port) {
        this.heartbeatSequence = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(heartBeatStr, serverCharset));
        this.logPrefix = host.concat(":").concat(String.valueOf(port).concat("，"));

    }

    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        // 如果是空闲时间超时事件，并且是向服务器发送数据空闲时间超时，则发送心跳数据到服务器，否则将事件继续向下繁殖
        if (evt instanceof IdleStateEvent) {
            final IdleState state = ((IdleStateEvent) evt).state();
            // 当停止向服务器发送数据N秒后发送心跳数据到服务器
            if (state == IdleState.WRITER_IDLE) {
                LOGGER.info("{}发送心跳报文", logPrefix);
                // 发送心跳请求到服务器
                ctx.writeAndFlush(heartbeatSequence.duplicate());
            }

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
