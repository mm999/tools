package com.xiafei.tools.nosql.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * <P>Description: JedisClient配置. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/11/22</P>
 * <P>UPDATE DATE: 2017/11/22</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@Slf4j
@Configuration
public class JedisConfig {
    @Resource
    private JedisClientProperties properties;

    /**
     * 初始化JedisClient对象.
     *
     * @return 返回什么并不重要.
     * @throws Exception 连接redis失败什么的
     */
    @Bean
    public JedisClient init() throws Exception {
        return new JedisClient(properties.getAddress(), properties.getPassowrd(), properties.getMaxTotal(),
                properties.getMaxIdle(), properties.getMaxWaitTimeMs());
    }

    public static void main(String[] args) throws Exception {
        new JedisClient("192.168.130.203:8501,192.168.130.203:8502,192.168.130.203:8503,192.168.130.203:8504,192.168.130.203:8505,192.168.130.203:8506",
                "Dsjrxd", 1000, 50, 100);

        System.out.println("get不存在的key时返回" + JedisClient.get("mingxianbukenengyou"));
    }
}
