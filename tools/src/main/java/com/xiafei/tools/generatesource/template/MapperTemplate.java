package com.xiafei.tools.generatesource.template;

import com.xiafei.tools.generatesource.ColumnInfo;
import com.xiafei.tools.generatesource.GenerateSourceParam;
import com.xiafei.tools.generatesource.GenerateSourceParamItem;
import com.xiafei.tools.generatesource.enums.ColumnKeyEnum;
import com.xiafei.tools.generatesource.enums.DataBaseTypeEnum;
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
            columnInfos.remove(primaryColumn);
        }
        final String resultId = StringUtils.firstCharToLower(item.getClassName()) + "Result";
        final String domainJavaPath = param.getDomainPackage() + item.getClassName() + "PO";
        // 增加resultMap信息.
        addResultMap(param, item, columnInfos, content, primaryColumn, domainJavaPath);
        // 增加get

        // 增加query

        // 增加count

        // 增加updateById

        // 增加update

        // 增加insert

        // 增加batchInsert


        content.add("</mapper>");
    }

    /**
     * 增加mapper的resultMap部分.
     *
     * @param primaryColumn  主键列
     * @param domainJavaPath domain所在包+类地址
     */
    private static void addResultMap(final GenerateSourceParam param, final GenerateSourceParamItem item, final List<ColumnInfo> columnInfos, final List<String> content, final ColumnInfo primaryColumn, final String domainJavaPath) {
        content.add(getIndent(1) + "<resultMap id=\"" + StringUtils.firstCharToLower(item.getClassName())
            + "Result\" type=\"" + domainJavaPath + "\">");
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
     * 增加头部.
     *
     * @param param   参数
     * @param item    参数明细
     * @param content 输出文件内容.
     */
    private static void addHead(final GenerateSourceParam param, final GenerateSourceParamItem item,
                                final List<String> content) {
        content.add("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        content.add("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >");
        content.add("<mapper namespace=\"" + param.getDaoPackage() + "." + item.getClassName() + "Dao\">");
    }


}
