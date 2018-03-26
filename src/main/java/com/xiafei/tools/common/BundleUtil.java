/* 
 * Copyright (C) 2006-2016 乐视控股（北京）有限公司.
 * 本系统是商用软件,未经授权擅自复制或传播本程序的部分或全部将是非法的.
============================================================
 * FileName: BundleUtil.java 
 * Created: [2016年6月29日 下午14:13:23] by 胡建    (copy by leFinanceLoan)    
 * $Id$
 * $Revision$
 * $Author$
 * $Date$
============================================================ 
 * ProjectName: leFinanceLoan_task 
 * Description: 乐视小贷批次任务持久 properties 文件
==========================================================*/
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
                    bundleMap.put(name, ResourceBundle.getBundle(name));
                }
            }
        }
        return bundleMap.get(name);
    }

}
