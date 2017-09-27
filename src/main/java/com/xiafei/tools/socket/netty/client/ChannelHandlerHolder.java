package com.xiafei.tools.socket.netty.client;

import io.netty.channel.ChannelHandler;

/**
 * <P>Description: 标识handler持有者类型.  </p>
 * <p>handlers()可以返回客户端手里的ChannelHandler集合,这样做是为了更方便的重新初始化和重连：
 * 可以很方便地获取通过这个接口的子类获取ChannelPipeline中的Handlers，
 * 获取到handlers之后，可以很方便的初始化和重连.  </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/7</P>
 * <P>UPDATE DATE: 2017/7/7</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public interface ChannelHandlerHolder {

    /**
     * 获取channelPipeline中的所有handlers.
     *
     * @return channelHandler数组
     */
    ChannelHandler[] handlers();
}
