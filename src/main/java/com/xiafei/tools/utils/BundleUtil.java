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
package com.xiafei.tools.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

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
public class BundleUtil {
    public static final Logger LOG = Logger.getLogger(BundleUtil.class);
    private static final Map<String, BundleUtil> bundleMap = new HashMap<String, BundleUtil>();
    private ResourceBundle bundle;

    public static BundleUtil newInstance(String name) {
        if (StringUtils.isBlank(name)) {
            return new BundleUtil(null);
        }
        if (!bundleMap.containsKey(name)) {
            synchronized (BundleUtil.class) {
                if (!bundleMap.containsKey(name)) {
                    bundleMap.put(name, new BundleUtil(name));
                }
            }
        }
        return bundleMap.get(name);
    }

    public String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            LOG.warn(String.format("-----Can't find %s' value-----", key));
            return "";
        }
    }

    public String getString(String key, String defaultValue) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            LOG.warn(String.format("-----Can't find %s' value-----", key));
        }
        return defaultValue;
    }

    private BundleUtil(String name) {
        try {
            bundle = ResourceBundle.getBundle(name);
        } catch (Exception e) {
            LOG.warn(String.format("Can't find file: %s.property", name));
        }
    }
}
