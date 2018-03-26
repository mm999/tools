package com.xiafei.tools.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <P>Description: 通过dubbo回调工具类，不需要依赖回调dubbo接口api包，
 * 接收回调的接口要继承DubboCallBackAble接口. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/2/1</P>
 * <P>UPDATE DATE: 2018/2/1</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Slf4j
public class DubboCallBackUtil {

    /**
     * dubbo接口配置池.
     */
    private static final ConcurrentHashMap<String, ReferenceConfig<GenericService>> POOL =
            new ConcurrentHashMap<>();

    /**
     * 连接池的key中的分隔符.
     */
    private static final String KEY_SEPARATOR = "-";

    /**
     * Dubbo的Application配置.
     */
    private static final ApplicationConfig APP_CONFIG = new ApplicationConfig("PAY");

    /**
     * 标识是否已经初始化.
     */
    private static boolean initialized = false;

    /**
     * 执行dubbo回调.
     *
     * @param dto 回调参数
     * @param c   回调配置
     * @return 是否调用成功
     */
    public static boolean callBack(final CallBackDto dto, final DynamicDubboConfigPo c) {
        checkParam(c);
        final ReferenceConfig<GenericService> dubboRef = getDubboRef(c);
        return (boolean) dubboRef.get().$invoke(
                "callBack",
                new String[]{CallBackDto.class.getTypeName()},
                new Object[]{dto}
        );
    }

    /**
     * 获取dubbo连接，如果在连接池中不存在，则初始化到连接池.
     *
     * @param c 配置信息
     */
    private static ReferenceConfig<GenericService> getDubboRef(final DynamicDubboConfigPo c) {

        // 连接池的key，使用接口名称-组-version类唯一标识一个dubbo连接.
        final String key = new StringBuilder(c.getInterfaceName()).append(KEY_SEPARATOR).
                append(c.getGroup()).append(KEY_SEPARATOR).append(c.getVersion()).toString();
        ReferenceConfig<GenericService> rc = POOL.get(key);
        if (rc != null) {
            return rc;
        }
        rc = new ReferenceConfig<>();
        rc.setApplication(APP_CONFIG);
        rc.setInterface(c.getInterfaceName());
        rc.setRegistry(new RegistryConfig(c.getZookeeperAddress()));
        rc.setGroup(c.getGroup());
        rc.setVersion(c.getVersion());
        rc.setGeneric(true);
        POOL.putIfAbsent(key, rc);
        return rc;
    }

    /**
     * 初始化一些zk、dubbo连接.
     *
     * @param configList 需要初始化的配置列表.
     */
    public static void init(final List<DynamicDubboConfigPo> configList) {
        if (initialized) {
            throw new UnsupportedOperationException("class has initialized!");
        }

        if (!CollectionUtils.isEmpty(configList)) {
            for (DynamicDubboConfigPo c : configList) {
                try {
                    final ReferenceConfig<GenericService> dubboRef = getDubboRef(c);
                    // 初始化连接
                    dubboRef.get();
                } catch (Throwable e) {
                    log.error("有初始化失败的dubbo连接,config={}", c, e);
                }
            }
        }
        initialized = true;
    }

    /**
     * 校验配置参数正确性.
     *
     * @param c dubbo配置参数
     */
    private static void checkParam(final DynamicDubboConfigPo c) {
        if (c == null) {
            throw new IllegalArgumentException("param is null");
        }
        if (StringUtils.isBlank(c.getInterfaceName())) {
            throw new IllegalArgumentException("interfaceName must not null");
        }
        if (StringUtils.isBlank(c.getZookeeperAddress())) {
            throw new IllegalArgumentException("zookeeperAddress must not null");
        }
    }


    /**
     * <P>Description: 回调数据格式java对象. </P>
     * <P>CALLED BY:   齐霞飞 </P>
     * <P>UPDATE BY:   齐霞飞 </P>
     * <P>CREATE DATE: 2018/2/1</P>
     * <P>UPDATE DATE: 2018/2/1</P>
     *
     * @author qixiafei
     * @version 1.0
     * @since java 1.8.0
     */
    @Data
    public static class CallBackDto implements Serializable {

        private static final long serialVersionUID = -9153847410795169110L;
        /**
         * 可以唯一标识本次回调对应的调用的标识字段.
         */
        private String unique;

        /**
         * 若unique不能唯一标识，则和这个字段组合唯一标识字段.
         */
        private String unique2;

        /**
         * 本次回调对应的调用是否成功.
         */
        private Boolean success;

        /**
         * 失败代码.
         */
        private String errorCode;
        /**
         * 如果失败，给出失败原因.
         */
        private String errorMsg;

        /**
         * 额外信息.
         */
        private Map<String, String> extraMap;
    }

    /**
     * <P>Description: 动态dubbo接口配置持久化对象. </P>
     * <P>CALLED BY:   齐霞飞 </P>
     * <P>UPDATE BY:   齐霞飞 </P>
     * <P>CREATE DATE: 2018-02-01</P>
     * <P>UPDATE DATE: 2018-02-01</P>
     *
     * @author 齐霞飞
     * @version 1.0.0
     * @since JDK 1.8.0
     */
    @Data
    @NoArgsConstructor
    public static class DynamicDubboConfigPo implements Serializable {

        private static final long serialVersionUID = 6127508277481074783L;
        /**
         * 物理自增主键.
         */
        private Long id;

        /**
         * dubbo接口的全限定名.
         */
        private String interfaceName;

        /**
         * ZK连接地址.
         */
        private String zookeeperAddress;

        /**
         * ZK的组.
         */
        private String group;

        /**
         * dubbo接口版本号.
         */
        private String version;

        /**
         * 创建时间.
         */
        private Date gmtCreate;

        /**
         * 更新时间.
         */
        private Date gmtModified;
    }


    /**
     * <P>Description: 可以用来接收dubbo回调的接口规范. </P>
     * <P>CALLED BY:   齐霞飞 </P>
     * <P>UPDATE BY:   齐霞飞 </P>
     * <P>CREATE DATE: 2018/2/1</P>
     * <P>UPDATE DATE: 2018/2/1</P>
     *
     * @author qixiafei
     * @version 1.0
     * @since java 1.8.0
     */
    public interface DubboCallBackAble {

        /**
         * 商户对接平台需要dubbo类型回调要参照这个方法设计回调接收的dubbo接口.
         *
         * @param dto 回调参数
         * @return 回调成功不重发
         */
        Boolean callBack(CallBackDto dto);
    }
}
