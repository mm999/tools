package com.xiafei.tools.generatesource.template;

import com.xiafei.tools.generatesource.ColumnInfo;
import com.xiafei.tools.generatesource.GenerateSourceParam;
import com.xiafei.tools.generatesource.GenerateSourceParamItem;
import com.xiafei.tools.generatesource.enums.JdbcTypeJavaTypeEnum;
import com.xiafei.tools.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    public static void addContent(final GenerateSourceParam param, final GenerateSourceParamItem item, final List<ColumnInfo> columnInfoList, final List<String> fileContent) {
        // 第一行固定package信息
        addPackage(param.getDomainPackage(), fileContent);
        // 输出内容中增加可能出现的import信息
        addDomainImportInfo(columnInfoList, fileContent);
        // 增加类注释
        addClassComments(param, item, "持久化对象", fileContent);
        // 增加类声明
        addClassDeclara(item.getClassName() + (param.getDomainSuffix() == null ? "" :
            param.getDomainSuffix()), fileContent);
        // 增加字段声明
        addPropertiesDeclara(columnInfoList, fileContent);
        // 增加get、set方法
        addSetterAndGetter(columnInfoList, fileContent);
        fileContent.add("}");
    }

    /**
     * 增加package那一行的信息.
     *
     * @param domainPackage package
     * @param fileContent   输出文件内容列表
     */
    private static void addPackage(final String domainPackage, final List<String> fileContent) {
        fileContent.add("package " + domainPackage + ";");
    }

    /**
     * 在domain文件内容列表中增加import信息.
     *
     * @param columnInfoList 字段信息
     * @param fileContent    文件内容列表
     */
    private static void addDomainImportInfo(final List<ColumnInfo> columnInfoList, final List<String> fileContent) {
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
    }

    /**
     * 增加类声明.
     *
     * @param className   Domain类名字
     * @param fileContent 输出文件内容列表.
     */
    private static void addClassDeclara(final String className, final List<String> fileContent) {
        fileContent.add("@SuppressWarnings(\"unused\")");
        fileContent.add("public class " + className + " implements Serializable {");
    }

    /**
     * 增加字段声明.
     *
     * @param columnInfoList 字段信息列表.
     * @param fileContent    输出文件内容列表.
     */
    private static void addPropertiesDeclara(final List<ColumnInfo> columnInfoList, final List<String> fileContent) {
        for (ColumnInfo columnInfo : columnInfoList) {
            fileContent.add("");
            // 字段注释
            fileContent.add(getIndent(1) + "/**");
            fileContent.add(getIndent(1) + " * " + columnInfo.getComment() + ".");
            fileContent.add(getIndent(1) + " */");
            // 字段声明
            fileContent.add(getIndent(1) + "private " + JdbcTypeJavaTypeEnum.instance(columnInfo.getType()).javaType
                + " " + StringUtils.underLineToHump(columnInfo.getName().toLowerCase(), false) + ";");
        }
    }

    /**
     * 增加字段的setter和getter方法.
     *
     * @param columnInfoList 字段信息列表.
     * @param fileContent    输出文件内容列表.
     */
    private static void addSetterAndGetter(final List<ColumnInfo> columnInfoList, final List<String> fileContent) {
        for (ColumnInfo columnInfo : columnInfoList) {
            // 字段名称
            String pName = StringUtils.underLineToHump(columnInfo.getName().toLowerCase(), false);
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
