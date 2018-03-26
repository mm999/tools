package com.xiafei.tools.spring.springboot.dubbo;

import org.springframework.context.annotation.Configuration;

/**
 * <P>Description: Dubbo服务提供者配置. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/2/1</P>
 * <P>UPDATE DATE: 2018/2/1</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Configuration
public class DubboProviderConfig {

//    @Resource
//    private DubboService dubboService;
//
//    @Resource
//    private DubboProperties dubboProperties;
//
//    public ServiceBean<DubboService> dubboServiceServiceBean() {
//        ServiceBean<DubboService> serviceBean = getWithPubProperties();
//        serviceBean.setInterface(DubboService.class);
//        serviceBean.setRef(dubboService);
//        return serviceBean;
//    }
//
//    /**
//     * 获取serviceBean并设置一些公共属性.
//     */
//    private <T> ServiceBean<T> getWithPubProperties() {
//        ServiceBean<T> serviceBean = new ServiceBean<>();
//        serviceBean.setApplication(new ApplicationConfig(dubboProperties.getApplicationName()));
//        serviceBean.setVersion(dubboProperties.getVersion());
//        serviceBean.setRetries(dubboProperties.getRetry());
//        serviceBean.setTimeout(dubboProperties.getTimeOut());
//        return serviceBean;
//    }
}
