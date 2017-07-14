package com.xiafei.tools.generatesource.template;

/**
 * <P>Description:  模板类超类</P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/14</P>
 * <P>UPDATE DATE: 2017/7/14</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
public class SourceTemplate {

    /**
     * 一个tab占用的空格字符串.
     */
    private static final String ONE_TAB = "    ";

    /**
     * 获取缩进.
     *
     * @param tabNum 制表符的数量
     * @return 缩进长度的空格字符串
     */
    protected static String getIndent(int tabNum) {
        if (tabNum <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(ONE_TAB);
        for (int i = 1; i < tabNum; i++) {
            sb.append(ONE_TAB);
        }
        return sb.toString();
    }
}
