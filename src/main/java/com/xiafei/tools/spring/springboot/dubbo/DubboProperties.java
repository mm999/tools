package com.xiafei.tools.spring.springboot.dubbo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <P>Description: . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/4</P>
 * <P>UPDATE DATE: 2017/12/4</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@Data
@ConfigurationProperties(prefix = "custom.dubbo")
public class DubboProperties {

    /**
     * 应用名.
     */
    private String applicationName;
    /**
     * dubbo接口版本号.
     */
    private String version;

    /**
     * dubbo接口重试次数
     */
    private Integer retry;

    /**
     * 连接超时.
     */
    private Integer timeOut;
}
