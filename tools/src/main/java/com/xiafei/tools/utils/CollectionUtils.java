package com.xiafei.tools.utils;

import java.util.Iterator;

/**
 * <P>Description: 集合工具类. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/13</P>
 * <P>UPDATE DATE: 2017/7/13</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
public final class CollectionUtils {

    /**
     * 工具类不允许实例化.
     */
    private CollectionUtils() {

    }

    /**
     * 将实现了Iterable接口的集合类转换成字符串打印出来.
     *
     * @param pIterable 实现了Iterable接口的集合对象
     * @return 可供打印的字符串
     */
    public static String toString(final Iterable pIterable) {
        if (pIterable == null) {
            return "[]";
        }

        final Iterator iterator = pIterable.iterator();
        final StringBuilder sb = new StringBuilder("[");
        while (iterator.hasNext()) {
            final Object obj = iterator.next();
            sb.append(obj.toString()).append(",");
        }
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }
}
