package com.xiafei.tools.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * <P>Description: properties文件操作工具. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/13</P>
 * <P>UPDATE DATE: 2017/7/13</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@Slf4j
public class BundleUtil {
    private static final Map<String, ResourceBundle> bundleMap = new HashMap<>();

    /**
     * 工具类禁止私有化.
     */
    private BundleUtil() {
    }

    /**
     * 获取一个配置文件操作工具实体.
     *
     * @param name 配置文件名，例：想获取a.properties的操作类 ， 传入参数"a"
     * @return a.properties 的操作类
     */
    public static ResourceBundle instance(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("请输入配置文件名前缀");
        }
        if (!bundleMap.containsKey(name)) {
            synchronized (BundleUtil.class) {
                if (!bundleMap.containsKey(name)) {
                    try {
                        bundleMap.put(name, ResourceBundle.getBundle(name));
                    } catch (RuntimeException e) {
                        return null;
                    }
                }
            }
        }
        return bundleMap.get(name);
    }


}
