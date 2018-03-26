package com.xiafei.tools.spring.springboot.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <P>Description: mq消费者接口，所有消费者继承该接口，通知生产者tag要声明成实现类的bean名字. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/11/21</P>
 * <P>UPDATE DATE: 2017/11/21</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public interface MqListener {

    /**
     * 消费mq消息.
     *
     * @param msgs 消息列表
     * @return 消费状态
     * @throws UnsupportedEncodingException 编码格式错误异常
     */
    ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs) throws UnsupportedEncodingException;
}
