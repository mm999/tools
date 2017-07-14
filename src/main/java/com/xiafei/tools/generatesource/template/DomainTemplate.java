package com.xiafei.tools.generatesource.template;

import com.xiafei.tools.generatesource.ColumnInfo;
import com.xiafei.tools.generatesource.GenerateSourceParam;
import com.xiafei.tools.generatesource.GenerateSourceParamItem;
import com.xiafei.tools.generatesource.enums.JdbcTypeJavaTypeEnum;
import com.xiafei.tools.utils.DateUtils;
import com.xiafei.tools.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * <P>Description: domain类模板. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/13</P>
 * <P>UPDATE DATE: 2017/7/13</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
public final class DomainTemplate extends SourceTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainTemplate.class);

    /**
     * 不允许实例化.
     */
    private DomainTemplate() {

    }

    /**
     * 增加package那一行的信息.
     *
     * @param domainPackage package
     * @param fileContent   输出文件内容列表
     */
    public static void addPackage(final String domainPackage, final List<String> fileContent) {
        fileContent.add("package " + domainPackage + ";");
    }

    /**
     * 在domain文件内容列表中增加import信息.
     *
     * @param columnInfoList 字段信息
     * @param fileContent    文件内容列表
     */
    public static void addDomainImportInfo(final List<ColumnInfo> columnInfoList, final List<String> fileContent) {
        // 空行
        fileContent.add("");
        fileContent.add("import java.io.Serializable;");
        boolean existDate = false;
        boolean existBigDecimal = false;
        for (ColumnInfo columnInfo : columnInfoList) {
            final JdbcTypeJavaTypeEnum jdbcTypeJavaTypeEnum = JdbcTypeJavaTypeEnum.instance(columnInfo.getType());
            if (jdbcTypeJavaTypeEnum == null) {
                LOGGER.error("有无法识别的jdbcType,字段信息列表:{}", columnInfo);
                throw new RuntimeException("有无法识别的jdbcType" + columnInfo);
            }
            if (!existDate && "Date".equals(jdbcTypeJavaTypeEnum.javaType)) {
                existDate = true;
            }
            if (!existBigDecimal && "BigDecimal".equals(jdbcTypeJavaTypeEnum.javaType)) {
                existBigDecimal = true;
            }
            if (existDate && existBigDecimal) {
                break;
            }
        }
        if (existDate) {
            fileContent.add("import java.util.Date;");
        }

        if (existBigDecimal) {
            fileContent.add("import java.math.BigDecimal;");
        }
        fileContent.add("");
    }

    /**
     * 增加类注释.
     *
     * @param param       参数
     * @param item        参数明细项
     * @param fileContent 文件内容列表
     */
    public static void addClassComments(final GenerateSourceParam param, final GenerateSourceParamItem item, final List<String> fileContent) {
        fileContent.add("/**");
        fileContent.add(" * <P>Description: " + item.getClassName() + "PO. </P>");
        fileContent.add(" * <P>CALLED BY:   " + param.getCommentsUser() + " </P>");
        fileContent.add(" * <P>UPDATE BY:   " + param.getCommentsUser() + " </P>");
        final String dateComment = DateUtils.toString(new Date(), DateUtils.YMD_SEPARATE_WITH_SLASH);
        fileContent.add(" * <P>CREATE DATE: " + dateComment + "</P>");
        fileContent.add(" * <P>UPDATE DATE: " + dateComment + "</P>");
        fileContent.add(" *");
        fileContent.add(" * @author " + param.getCommentsUser());
        fileContent.add(" * @version " + param.getCommentsVersion());
        fileContent.add(" * @since " + param.getCommentsSince());
        fileContent.add(" */");
    }

    /**
     * 增加类声明.
     *
     * @param className   Java类名字
     * @param fileContent 输出文件内容列表.
     */
    public static void addClassDeclara(final String className, final List<String> fileContent) {
        fileContent.add("@SuppressWarnings(\"unused\")");
        fileContent.add("public class " + className + "PO implements Serializable {");
    }

    /**
     * 增加字段声明.
     *
     * @param columnInfoList 字段信息列表.
     * @param fileContent    输出文件内容列表.
     */
    public static void addPropertiesDeclara(final List<ColumnInfo> columnInfoList, final List<String> fileContent) {
        for (ColumnInfo columnInfo : columnInfoList) {
            fileContent.add("");
            // 字段注释
            fileContent.add(getIndent(1) + "/**");
            fileContent.add(getIndent(1) + " * " + columnInfo.getComment() + ".");
            fileContent.add(getIndent(1) + " */");
            // 字段声明
            fileContent.add(getIndent(1) + "private " + JdbcTypeJavaTypeEnum.instance(columnInfo.getType()).javaType
                + " " + StringUtils.underLineToHump(columnInfo.getName(), false) + ";");
        }
    }

    /**
     * 增加字段的setter和getter方法.
     *
     * @param columnInfoList 字段信息列表.
     * @param fileContent    输出文件内容列表.
     */
    public static void addSetterAndGetter(final List<ColumnInfo> columnInfoList, final List<String> fileContent) {
        for (ColumnInfo columnInfo : columnInfoList) {
            // 字段名称
            String pName = StringUtils.underLineToHump(columnInfo.getName(), false);
            // 首字母大写的字段名称
            String pNameFirstUpper = StringUtils.firstCharToUpper(pName);
            // 字段java类型
            String javaType = JdbcTypeJavaTypeEnum.instance(columnInfo.getType()).javaType;

            fileContent.add("");
            // get方法
            fileContent.add(getIndent(1) + "public " + javaType + " get" + pNameFirstUpper + "() {");
            fileContent.add(getIndent(2) + "return " + pName + ";");
            fileContent.add(getIndent(1) + "}");
            // set 方法
            fileContent.add("");
            fileContent.add(getIndent(1) + "public void set" + pNameFirstUpper + "(final " + javaType + " p" + pNameFirstUpper + ") {");
            fileContent.add(getIndent(2) + "this." + pName + " = p" + pNameFirstUpper + ";");
            fileContent.add(getIndent(1) + "}");
        }
    }
}
