package com.xiafei.tools.utils;

/**
 * <P>Description: 数字相关工具类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/6/29</P>
 * <P>UPDATE DATE: 2017/6/29</P>
 *
 * @author qixiafei
 * @version 0.0.1-SNAPSHOT
 * @since java 1.7.0
 */
public final class NumberUtils {

    /**
     * hashMap默认的负载因子.
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 不允许实例化.
     */
    private NumberUtils() {

    }

    /**
     * 通过 键值对数量,确定HashMap最优的初始化大小，负载因子采用默认值0.75.
     *
     * @param dataSize 预计键值对的数量
     * @return 最优的初始化大小
     */
    public static int getHashMapCap(final int dataSize) {
        return getHashMapCap(dataSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 通过 键值对数量,预期的负载因子，确定HashMap最优的初始化大小.
     *
     * @param dataSize   预计键值对的数量
     * @param loadFactor 设置负载因子
     * @return 最优的初始化大小
     * @throws IllegalArgumentException 负载因子非法时抛出异常
     */
    public static int getHashMapCap(final int dataSize, final float loadFactor)
            throws IllegalArgumentException {
        if (dataSize <= 0) {
            return 1;
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: "
                    + loadFactor);
        }
        //按照传入dataSize初始化的话Map的实际size
        int mapInitSize = Integer.highestOneBit(dataSize);
        //如果size不是2的幂数，那么Map的实际size是最近接并大于dataSize的2的幂数
        if (Integer.bitCount(dataSize) > 1) {
            mapInitSize <<= 1;
        }
        //按照传入dataSize初始化的话Map当元素的数量大于等于resizeMax后将扩容，我们要避免这种情况
        final int resizeMax = (int) (mapInitSize * loadFactor);
        if (dataSize < resizeMax) {
            //如果键值对数量小于扩若界限，直接返回map初始化size就可以
            return mapInitSize;
        } else {
            //否则扩大map的初始化大小
            return mapInitSize << 1;
        }
    }
}
