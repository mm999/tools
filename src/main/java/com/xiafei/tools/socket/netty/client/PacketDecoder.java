package com.xiafei.tools.socket.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * <P>Description: 客户端解码，解包Decoder.目前只支持服务器编码中汉字是两个字节的情况，因Unicode编码类型汉字的位数无法确认。 </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/24</P>
 * <P>UPDATE DATE: 2017/7/24</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@ChannelHandler.Sharable
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    /**
     * 日志记录.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PacketDecoder.class);

    /**
     * 非英文字节缓存.
     */
    private static final ByteBuffer NON_ENG_BYTE = ByteBuffer.allocate(1);

    /**
     * 服务器端编码.
     */
    private final Charset charset;

    /**
     * 构造器，确定编码.
     *
     * @param charset 编码
     */
    public PacketDecoder(final Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        } else {
            this.charset = charset;
        }
    }

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws Exception {

        if (msg.readableBytes() > 0) {
            // 处理之前的消息字符串，记录日志所用
            final String oriMsg = msg.toString(this.charset);
            // 向下传递的消息字符串
            String fireMsg = "";
            // 如果非英文字节缓存已经写入，和这次缓存中第一个字节拼接成一个完成汉字
            if (!NON_ENG_BYTE.hasRemaining()) {
                fireMsg = assembleChinese(msg, oriMsg);
            }

            // 记录在消息结尾连续非英文字符的数量
            int count = 0;
            // 从最后一个字节开始向前遍历，注：ByteBuf类缓存当明确指定get索引的时候不移动指针位置
            byte aByte = msg.getByte(msg.writerIndex() - 1);
            // 我们知道汉字的编码是小于零的
            while (aByte < 0) {
                count++;
                aByte = msg.getByte(msg.writerIndex() - 1 - count);
            }
            // 如果非英文字节的数量是单数，说明最后一个汉字是被截断的，
            // 本次消息最后一个字节被舍弃，放入缓存等待下一条消息补齐汉字的后半字节
            if (count % 2 == 1) {
                LOGGER.info("本条消息最后一个字节是非英文字符的前半个字节，已缓存。完整消息：{}", oriMsg);

                NON_ENG_BYTE.put(msg.getByte(msg.writerIndex() - 1));
                msg.writerIndex(msg.writerIndex() - 1);

            }
            fireMsg += msg.toString(this.charset);
            out.add(fireMsg);
        }

    }

    /**
     * 组装成汉字.
     *
     * @param msg    服务器发来消息ByteBuf对象
     * @param oriMsg 本次接收完成消息
     * @return 被截断的汉字
     */
    private String assembleChinese(final ByteBuf msg, final String oriMsg) {
        // 转换本地缓存读写状态
        NON_ENG_BYTE.flip();
        // 被截断的汉字字节数组
        final byte[] cutedChinese = new byte[2];
        // 读取上一条消息的最后一个字节
        cutedChinese[0] = NON_ENG_BYTE.get();
        // 清空缓存
        NON_ENG_BYTE.clear();
        // 获取本条消息的第一个字节,这个操作不会让读索引+1，所以紧接着将读索引加1
        cutedChinese[1] = msg.getByte(msg.readerIndex());
        msg.readerIndex(msg.readerIndex() + 1);
        // 将两个字节组成一个汉字
        final String chinese = new String(cutedChinese, this.charset);
        LOGGER.info("上条消息最后一个字符是非英文字符的前半个字节，已和本条消息的第一个字节组成一个完整的汉字：{}，完整消息：{}", chinese, oriMsg);
        return chinese;
    }
}


