package com.xiafei.tools.generatesource.template;

import com.xiafei.tools.generatesource.GenerateSourceParam;
import com.xiafei.tools.generatesource.GenerateSourceParamItem;
import com.xiafei.tools.common.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * <P>Description:  模板类超类</P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/14</P>
 * <P>UPDATE DATE: 2017/7/14</P>
 *
 * @author qixiafei
 * @version 1.0
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

    /**
     * 增加类注释.
     *
     * @param param         参数
     * @param item          参数明细项
     * @param descripSuffix 类描述后缀.
     * @param fileContent   输出文件内容列表
     */
    protected static void addClassComments(final GenerateSourceParam param, final GenerateSourceParamItem item, final String descripSuffix, final List<String> fileContent) {
        fileContent.add("");
        fileContent.add("/**");
        fileContent.add(" * <P>Description: " + item.getClassDescription() + descripSuffix + ". </P>");
        fileContent.add(" * <P>CALLED BY:   " + param.getCommentsUser() + " </P>");
        fileContent.add(" * <P>UPDATE BY:   " + param.getCommentsUser() + " </P>");
        final String dateComment = DateUtils.getYMDWithSeparate().format(new Date());
        fileContent.add(" * <P>CREATE DATE: " + dateComment + "</P>");
        fileContent.add(" * <P>UPDATE DATE: " + dateComment + "</P>");
        fileContent.add(" *");
        fileContent.add(" * @author " + param.getCommentsUser());
        fileContent.add(" * @version " + param.getCommentsVersion());
        fileContent.add(" * @since " + param.getCommentsSince());
        fileContent.add(" */");
    }

}
