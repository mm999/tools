package com.xiafei.tools.generatesource.template;

import com.xiafei.tools.generatesource.ColumnInfo;
import com.xiafei.tools.generatesource.GenerateSourceParam;
import com.xiafei.tools.generatesource.GenerateSourceParamItem;
import com.xiafei.tools.generatesource.enums.ColumnKeyEnum;
import com.xiafei.tools.generatesource.enums.DataBaseTypeEnum;
import com.xiafei.tools.generatesource.enums.JdbcTypeJavaTypeEnum;
import com.xiafei.tools.generatesource.enums.MyBatisJdbcTypeEnum;
import com.xiafei.tools.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <P>Description:  mybatisMapper文件生成模板. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/14</P>
 * <P>UPDATE DATE: 2017/7/14</P>
 *
 * @author qixiafei
 * @version 0.0.3-SNAPSHOT
 * @since java 1.7.0
 */
public final class MapperTemplate extends SourceTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapperTemplate.class);

    /**
     * 行最大长度，超过就换行.
     */
    private static final int LINE_LENGTH = 120;

    /**
     * 不允许实例化.
     */
    private MapperTemplate() {

    }


    /**
     * 增加输出文件内容.
     *
     * @param param       参数
     * @param item        参数明细
     * @param columnInfos 数据库表中查出的字段信息列表
     * @param content     输出文件内容.
     */
    public static void addContent(final GenerateSourceParam param, final GenerateSourceParamItem item,
                                  final List<ColumnInfo> columnInfos, final List<String> content) {
        // 增加头部信息
        addHead(param, item, content);
        ColumnInfo primaryColumn = null;
        for (ColumnInfo info : columnInfos) {
            if (item.getDataBaseType() == DataBaseTypeEnum.MYSQL
                && ColumnKeyEnum.instance(info.getKey()) == ColumnKeyEnum.MYSQL_PRIMARY) {
                primaryColumn = info;
            }
        }
        if (primaryColumn != null) {
            // ===============若主键不为空，columnInfos里面已经不包含主键信息了===========================
            columnInfos.remove(primaryColumn);
        }
        // resultMap的id.
        final String resultId = StringUtils.firstCharToLower(item.getClassName()) + "Result";
        // domain的包+类路径
        final String domainJavaPath = param.getDomainPackage() + item.getClassName() + (
            param.getDomainSuffix() == null ? "" : param.getDomainSuffix());
        // 增加resultMap信息.
        addResultMap(resultId, domainJavaPath, primaryColumn, columnInfos, content);
        // 增加字段<sql>模板
        addSqlTemplate(primaryColumn, columnInfos, content);
        if (primaryColumn != null) {
            // 增加get
            addGet(resultId, primaryColumn, item.getTableName(), content);
            // 增加updateById
            addUpdateById();
        }
        // 增加query

        // 增加count


        // 增加update

        // 增加insert

        // 增加batchInsert


        content.add("</mapper>");
    }

    /**
     * 增加头部.
     *
     * @param param   生成资源文件参数
     * @param item    参数明细
     * @param content 输出文件内容列表
     */
    private static void addHead(final GenerateSourceParam param, final GenerateSourceParamItem item,
                                final List<String> content) {
        content.add("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        content.add("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >");
        content.add("<mapper namespace=\"" + param.getDaoPackage() + "." + item.getClassName() + "Dao\">");
    }

    /**
     * 增加mapper的resultMap部分.
     *
     * @param resultId       拼好的resultMap的id
     * @param domainJavaPath domian类的包+类路径
     * @param primaryColumn  主键列信息
     * @param columnInfos    数据库字段列表信息
     * @param content        输出文件内容列表
     */
    private static void addResultMap(final String resultId, final String domainJavaPath, final ColumnInfo primaryColumn,
                                     final List<ColumnInfo> columnInfos, final List<String> content) {

        // resultMap第一行
        content.add(getIndent(1) + "<resultMap id=\"" + resultId + "\" type=\"" + domainJavaPath + "\">");
        // 判断是否存在主键列
        if (primaryColumn != null) {
            MyBatisJdbcTypeEnum myBatisJdbcTypeEnum = MyBatisJdbcTypeEnum.instance(primaryColumn.getType());
            if (myBatisJdbcTypeEnum == null) {
                throw new RuntimeException("无法将jdbc类型转换成mybatis的jdbcType");
            }
            content.add(getIndent(2) + "<id column=\"" + primaryColumn.getName() + "\" property=\""
                + StringUtils.underLineToHump(primaryColumn.getName(), false) + "\" jdbcType=\""
                + myBatisJdbcTypeEnum.mybatisType + "\"/>");
        }

        for (ColumnInfo columnInfo : columnInfos) {
            MyBatisJdbcTypeEnum myBatisJdbcTypeEnum = MyBatisJdbcTypeEnum.instance(columnInfo.getType());
            if (myBatisJdbcTypeEnum == null) {
                throw new RuntimeException("无法将jdbc类型转换成mybatis的jdbcType");
            }
            content.add(getIndent(2) + "<result column=\"" + columnInfo.getName() + "\" property=\""
                + StringUtils.underLineToHump(columnInfo.getName(), false) + "\" jdbcType=\""
                + myBatisJdbcTypeEnum.mybatisType + "\"/>");
        }
        content.add(getIndent(1) + "</resultMap>");
    }

    /**
     * 增加mapper的Sql模板
     *
     * @param primaryColumn 主键字段信息
     * @param columnInfos   数据库字段信息
     * @param content       输出文件内容列表
     */
    private static void addSqlTemplate(ColumnInfo primaryColumn, List<ColumnInfo> columnInfos, List<String> content) {
        // 先增加全字段模板
        content.add("");
        content.add(getIndent(1) + "<!-- 基础字段 -->");
        content.add(getIndent(1) + "<sql id=\"Base_Columns\">");
        content.add(getIndent(2) + primaryColumn.getName() + ",");
        content.add(getIndent(2) + "<include refid=\"Columns_For_Insert\"/>");
        content.add(getIndent(1) + "</sql>");

        // 增加insert模板，不包含主键
        content.add("");
        content.add(getIndent(1) + "<!-- Insert使用字段 -->");
        content.add(getIndent(1) + "<sql id=\"Columns_For_Insert\">");
        // 增加字段信息，不包含主键
        cycleAddColumnName(getIndent(2), columnInfos, content);
        content.add(getIndent(1) + "</sql>");
    }

    /**
     * 循环增加字段信息
     *
     * @param indent      缩进
     * @param columnInfos 数据库字段信息列表
     * @param content     输出文件内容
     */
    private static void cycleAddColumnName(final String indent, final List<ColumnInfo> columnInfos, final List<String> content) {
        // 拼接逗号分隔字段信息
        final StringBuilder sb = new StringBuilder();
        for (ColumnInfo columnInfo : columnInfos) {
            final String columnName = columnInfo.getName();
            if ((sb.length() + columnName.length()) / (LINE_LENGTH - indent.length())
                > sb.length() / (LINE_LENGTH - indent.length())) {
                // 如果新增该列描述后超长了，那么这行存好，接下来另起一行
                content.add(getIndent(2) + sb.toString());
                sb.delete(0, sb.length());
            }
            sb.append(columnInfo.getName()).append(",");
        }
        // 删掉最后一个逗号
        sb.deleteCharAt(sb.length() - 1);
        content.add(getIndent(2) + sb.toString());
    }

    /**
     * 增加get方法.
     *
     * @param resultId      resultMap的id
     * @param primaryColumn 主键字段信息
     * @param tableName     数据库表名
     * @param content       输出文件内容
     */
    private static void addGet(final String resultId, final ColumnInfo primaryColumn, final String tableName,
                               final List<String> content) {
        content.add("");
        content.add(getIndent(1) + "<!-- 根据主键查询一行数据 -->");
        content.add(getIndent(1) + "<select id=\"get\" parameterType=\""
            + JdbcTypeJavaTypeEnum.instance(primaryColumn.getType()).javaType
            + "\" resultMap=\"" + resultId + "\">");
        content.add(getIndent(2) + "SELECT");
        content.add(getIndent(2) + "<include refid=\"Base_Columns\"/>");
        content.add(getIndent(2) + "FROM " + tableName);
        content.add(getIndent(2) + "WHERE " + primaryColumn.getName() + " = #{"
            + StringUtils.underLineToHump(primaryColumn.getName(), false)
            + ",jdbcType=" + MyBatisJdbcTypeEnum.instance(primaryColumn.getType()).mybatisType + "}");
        content.add(getIndent(1) + "</select>");
    }


}
