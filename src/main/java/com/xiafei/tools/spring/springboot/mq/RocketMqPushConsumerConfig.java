package com.xiafei.tools.spring.springboot.mq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Slf4j
@Configuration
public class RocketMqPushConsumerConfig {


    @Resource
    private MqListenerDispatcher mqListenerDispatcher;

//    @Bean(name = "consumerBeanName", initMethod = "start", destroyMethod = "shutdown")
//    public DefaultMQPushConsumer defaultMqConsumer( String topic, String tag表达式)
//            throws MQClientException {
//
//        return registeConsumer(topic, tag, mqListenerDispatcher);
//    }

    /**
     * 注册消费者.
     *
     * @param topic       topic
     * @param tag         tag
     * @param consumerObj 消费者对象实例
     * @param <T>         实现MessageListenerConcurrently 接口的泛型
     * @return Consumer对象
     * @throws MQClientException Mq封装异常
     */
    private <T extends MessageListenerConcurrently> DefaultMQPushConsumer registeConsumer(final String topic,
                                                                                          final String tag,
                                                                                          final T consumerObj)
            throws MQClientException {

        DefaultMQPushConsumer defaultMQPushConsumer = this.defaultInstance();
        defaultMQPushConsumer.subscribe(topic, tag);
        defaultMQPushConsumer.registerMessageListener(consumerObj);
        return defaultMQPushConsumer;
    }

    private DefaultMQPushConsumer defaultInstance() {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("group地址");
        defaultMQPushConsumer.setNamesrvAddr("mqNameServer地址");
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1024);
        defaultMQPushConsumer.setInstanceName(String.valueOf(System.currentTimeMillis()));
        return defaultMQPushConsumer;
    }
}
