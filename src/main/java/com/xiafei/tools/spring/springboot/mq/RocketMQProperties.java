package com.xiafei.tools.spring.springboot.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rocket.mq")
public class RocketMQProperties {
    private String namesrvAddr;
    private String producerGroup;
    private String consumerGroup;
    /**
     * 通用topic
     */
    private String mercuryTopic;
    /**
     * 所有消费者tag拼接字符串
     */
    private String mercuryTag;
    /**
     * 还款状态回调mq.
     */
    private String repaymentStatusCallbackTag;
    /**
     * 贷款状态回调mq.
     */
    private String loanStatusCallbackTag;
    /**
     * 订单状态回调mq.
     */
    private String billStatusCallbackTag;
    /**
     * 贷款审批回调mq.
     */
    private String loanApproveCallbackTag;
}
