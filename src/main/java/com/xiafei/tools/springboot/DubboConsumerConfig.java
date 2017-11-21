package com.xiafei.tools.springboot;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import org.springframework.context.annotation.Configuration;

/**
 * <P>Description: Dubbo客户端配置. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/11/6</P>
 * <P>UPDATE DATE: 2017/11/6</P>
 *
 * @author 齐霞飞
 * @version 1.0
 * @since java 1.7.0
 */
@Configuration
public class DubboConsumerConfig {

    private static final String INTERFACE_VERSION = "1.0.0";

    /**
     * 通用产生dubbo引用bean方法，默认不检查，版本为配置文件中配置.
     *
     * @param clazz 引用bean的interface的class
     * @param <T>   引用bean的interface泛型
     * @return dubbo引用bean
     */
    private <T> ReferenceBean<T> getReferenceBean(final Class<T> clazz) {
        return getReferenceBean(clazz, false, INTERFACE_VERSION);
    }

    /**
     * 通用产生dubbo引用bean方法，可指定是否检查，版本默认.
     *
     * @param clazz   引用bean的interface的class
     * @param isCheck 是否进行接口检查
     * @param <T>     引用bean的interface泛型
     * @return dubbo引用bean
     */
    private <T> ReferenceBean<T> getReferenceBean(final Class<T> clazz, final boolean isCheck) {
        return getReferenceBean(clazz, isCheck, INTERFACE_VERSION);
    }

    /**
     * 通用产生dubbo引用bean方法，可指定版本，是否检查默认否.
     *
     * @param clazz   引用bean的interface的class
     * @param version dubbo接口版本
     * @param <T>     引用bean的interface泛型
     * @return dubbo引用bean
     */
    private <T> ReferenceBean<T> getReferenceBean(final Class<T> clazz, final String version) {
        return getReferenceBean(clazz, false, version);
    }

    /**
     * 通用产生dubbo引用bean方法，可指定是否检查、版本.
     *
     * @param clazz   引用bean的interface的class
     * @param isCheck 是否进行接口检查
     * @param version dubbo接口版本
     * @param <T>     引用bean的interface泛型
     * @return dubbo引用bean
     */
    private <T> ReferenceBean<T> getReferenceBean(final Class<T> clazz, final boolean isCheck, final String version) {
        final ReferenceBean<T> referenceBean = new ReferenceBean<>();
        referenceBean.setInterface(clazz);
        referenceBean.setCheck(isCheck);
        referenceBean.setVersion(version);
        return referenceBean;
    }
}
