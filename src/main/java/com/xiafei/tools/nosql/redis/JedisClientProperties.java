package com.xiafei.tools.nosql.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <P>Description: . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/1</P>
 * <P>UPDATE DATE: 2017/12/1</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@Data
@ConfigurationProperties(prefix = "jedis.client")
public class JedisClientProperties {

    /**
     * redis服务器地址[host:port]，集群多个地址之间使用逗号分隔.
     */
    private String address;

    /**
     * redis服务密码.
     */
    private String password;

    /**
     * 最大线程数量.
     */
    private Integer maxTotal;

    /**
     * 最大空闲线程数量.
     */
    private Integer maxIdle;

    /**
     * 最大等待返回时间（毫秒）.
     */
    private Integer maxWaitTimeMs;
}
