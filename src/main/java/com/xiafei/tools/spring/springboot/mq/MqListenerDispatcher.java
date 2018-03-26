package com.xiafei.tools.spring.springboot.mq;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.xiafei.tools.exceptions.BizException;
import com.xiafei.tools.common.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * <P>Description: mq消费者分发器，基于同一个项目一个topic一个group多tag的机制，消费者继承MqListener接口，
 * tag需要和消费者实现类的bean名字保持一致. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/11/21</P>
 * <P>UPDATE DATE: 2017/11/21</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@Slf4j
@Service
public class MqListenerDispatcher implements MessageListenerConcurrently, ApplicationContextAware {

    /**
     * 所有的mq消费者beanMap.
     */
    private static Map<String, ? extends MqListener> listenerMap;

    /**
     * 日志前缀.
     */
    private static final String LOG_PREFIX = "consumeMessage(): "; // 日志前缀

    /**
     * It is not recommend to throw exception,rather than returning ConsumeConcurrentlyStatus.RECONSUME_LATER
     * if consumption failure
     *
     * @param msgs    msgs.size() >= 1<br>
     *                DefaultMQPushConsumer.consumeMessageBatchMaxSize=1，you can modify here
     * @param context
     * @return
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

        for (MessageExt msg : msgs) {
            log.info("{},msg={}", LOG_PREFIX, JsonUtil.toJson(msg));
            final ConsumeConcurrentlyStatus consumeConcurrentlyStatus;
            try {
                final MqListener listener = listenerMap.get(msg.getTags());
                if (listener != null) {
                    consumeConcurrentlyStatus = listener.consumeMessage(Collections.singletonList(msg));
                    if (ConsumeConcurrentlyStatus.RECONSUME_LATER.equals(consumeConcurrentlyStatus)) {
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                } else {
                    log.error("{},can not find listener to consumer tags={} ", LOG_PREFIX, msg.getTags());
                    continue;
                }
            } catch (BizException e) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (Exception e) {
                log.error("mqConsumer,uncaught exception", e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        listenerMap = applicationContext.getBeansOfType(MqListener.class);
    }
}
