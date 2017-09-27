package com.xiafei.tools.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>Description: Map工具类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/6/NumberUtils29</P>
 * <P>UPDATE DATE: 2017/6/29</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public final class MapUtils {

    /**
     * 不允许实例化.
     */
    private MapUtils() {
    }

    /**
     * 新建一个hashMap,默认初始cap是16，loadFactor 0.75.
     *
     * @param <K> 键类型
     * @param <V> 值类型
     * @return 新建的HashMap
     */
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * 新建一个hashMap,默认初始cap是根据要放的键值对数量，loadFactor 0.75计算出的最优值.
     *
     * @param dataSize 键值对数量
     * @param <K>      键类型
     * @param <V>      值类型
     * @return 新建的HashMap
     */
    public static <K, V> Map<K, V> newHashMap(final int dataSize) {
        return new HashMap<>(NumberUtils.getHashMapCap(dataSize));
    }

    /**
     * 新建一个hashMap
     * 默认初始cap是根据要放的键值对数量，loadFactor计算出的最优值
     * 默认初始loadFactor是参数指定的.
     *
     * @param dataSize   键值对数量
     * @param loadFactor 负载因子
     * @param <K>        键类型
     * @param <V>        值类型
     * @return 新建的HashMap
     */
    public static <K, V> Map<K, V> newHashMap(final int dataSize, final float loadFactor) {
        return new HashMap<>(NumberUtils.getHashMapCap(dataSize, loadFactor), loadFactor);
    }
}
