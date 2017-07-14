package com.xiafei.tools.generatesource;

import com.xiafei.tools.generatesource.enums.DataBaseTypeEnum;
import com.xiafei.tools.generatesource.template.DomainTemplate;
import com.xiafei.tools.generatesource.template.MapperTemplate;
import com.xiafei.tools.utils.DBUtils;
import com.xiafei.tools.utils.FileUtils;
import com.xiafei.tools.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <P>Description: 资源文件生成器. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017年7月13日</P>
 * <P>UPDATE DATE: 2017年7月13日</P>
 *
 * @author qixiafei
 * @version 0.0.1-SNAPSHOT
 * @since java 1.7.0
 */
public final class SourceGenerator {

    /**
     * log4j.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SourceGenerator.class);

    private static final String MYSQL_SQL_TEMPLATE = "SELECT lower(t.COLUMN_NAME),upper(t.DATA_TYPE),t.COLUMN_COMMENT,upper(t.COLUMN_KEY) FROM information_schema.COLUMNS t WHERE t.TABLE_NAME = '$tableName' AND t.TABLE_SCHEMA = '$tableSchema' ORDER BY t.ORDINAL_POSITION";

    /**
     * 工具类不允许实例化.
     */
    private SourceGenerator() {

    }

    public static void main(String[] args) {
        GenerateSourceParam param = new GenerateSourceParam();
        GenerateSourceParamItem item = new GenerateSourceParamItem(DataBaseTypeEnum.MYSQL, "jdbc:mysql://10.11.147.81:3306",
            "order_t_w", "NTk3NjIyYmJmNDY", "trade_product_feepay", "trade_feepay",
            "ProductFeepay");
        param.setItems(Collections.singletonList(item));
        param.setCommentsUser("齐霞飞");
        param.setCommentsSince("JDK 1.7.0");
        param.setCommentsVersion("1.0");
        param.setDomainPath("C:/Users/qixiafei/Desktop/");
        param.setDomainPackage("com.le.jr.qixiafei.domain");
        param.setMapperPath("C:/Users/qixiafei/Desktop/");
        exec(param);
    }

    /**
     * 执行资源文件生成.
     *
     * @param param 生成参数
     */
    public static void exec(final GenerateSourceParam param) {

        if (!validParam(param)) {
            return;
        }

        // 循环要生成资源的项目
        for (GenerateSourceParamItem item : param.getItems()) {
            // 从数据库解析出的字段信息列表
            final List<ColumnInfo> columnInfoList = new ArrayList<>(16);
            // 访问数据库，查询表结构
            if (DataBaseTypeEnum.MYSQL == item.getDataBaseType()) {

                try {
                    // 建立数据库连接
                    final Connection conn = DBUtils.getMysqlConnection(item.getUrl(), item.getUser(), item.getPassword());
                    // 执行sql
                    String sql = MYSQL_SQL_TEMPLATE.replace("$tableName", item.getTableName()).replace("$tableSchema", item.getTableSchema());
                    LOGGER.info("查询表结构，sql：{}", sql);
                    final PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    // 解析结果集拼装数据库字段对象
                    final ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet == null) {
                        throw new RuntimeException("找不到数据库表信息");
                    }
                    while (resultSet.next()) {
                        final ColumnInfo columnInfo = new ColumnInfo();
                        columnInfo.setName(resultSet.getString(1));
                        columnInfo.setType(resultSet.getString(2));
                        columnInfo.setComment(resultSet.getString(3));
                        columnInfo.setKey(resultSet.getString(4));
                        columnInfoList.add(columnInfo);
                    }
                } catch (SQLException e) {
                    LOGGER.error("查询mysql数据库表结构出错", e);
                    return;
                }
            } else if (DataBaseTypeEnum.ORACLE == item.getDataBaseType()) {
                LOGGER.error("暂时不支持oracle数据库的资源自动生成");
                return;
            }

            if (columnInfoList.isEmpty()) {
                LOGGER.error("没有找到数据库表结构");
                return;
            }

            // 生成domain文件
            if (StringUtils.isNotBlank(param.getDomainPath())) {
                generateDomain(param, item, columnInfoList);
            }

            // 生成mapper文件
            if (StringUtils.isNotBlank(param.getMapperPath())) {
                generateMapper(param, item, columnInfoList);
            }

            // 生成dao文件
            if (StringUtils.isNotBlank(param.getDaoPath())) {
                generateDao(param, item, columnInfoList);
            }

        }

    }

    /**
     * 参数正确性做全方位校验.
     *
     * @param param 参数
     * @return true-通过,false-不通过
     */
    private static boolean validParam(final GenerateSourceParam param) {
        if (param.getItems() == null || param.getItems().isEmpty()) {
            LOGGER.error("参数中明细列表items为空，退出方法");
            return false;
        }

        if (StringUtils.isBlank(param.getDomainPath()) && StringUtils.isBlank(param.getDaoPath()) && StringUtils.isBlank(param.getMapperPath())) {
            LOGGER.error("参数中domian路径、dao路径、mapper路径都为空，退出方法");
            return false;
        }
        for (GenerateSourceParamItem item : param.getItems()) {
            if (item.getDataBaseType() == null) {
                LOGGER.error("必须传递数据库类字段，所传递参数：{}", param);
                return false;
            }
        }
        return true;
    }

    /**
     * 生成domian文件.
     *
     * @param param          生成参数
     * @param item           参数项
     * @param columnInfoList 数据库字段信息列表
     */
    private static void generateDomain(final GenerateSourceParam param, final GenerateSourceParamItem item, final List<ColumnInfo> columnInfoList) {
        // 输出文件内容
        final List<String> fileContent = new ArrayList<>(500);
        // 第一行固定package信息
        DomainTemplate.addPackage(param.getDomainPackage(), fileContent);
        // 输出内容中增加可能出现的import信息
        DomainTemplate.addDomainImportInfo(columnInfoList, fileContent);
        // 增加类注释
        DomainTemplate.addClassComments(param, item, fileContent);
        // 增加类声明
        DomainTemplate.addClassDeclara(item.getClassName(), fileContent);
        // 增加字段声明
        DomainTemplate.addPropertiesDeclara(columnInfoList, fileContent);
        // 增加get、set方法
        DomainTemplate.addSetterAndGetter(columnInfoList, fileContent);
        fileContent.add("}");
        String filePath = param.getDomainPath();
        if (!filePath.endsWith("/")) {
            filePath += "/";
        }
        filePath += item.getClassName() + "PO.java";
        FileUtils.outPutToFileByLine(filePath, fileContent);
        LOGGER.info("成功生成domain文件：{}", filePath);

    }


    /**
     * 生成mapper文件.
     *
     * @param param          生成参数
     * @param item
     * @param columnInfoList 数据库字段信息列表
     */
    private static void generateMapper(final GenerateSourceParam param, final GenerateSourceParamItem item, final List<ColumnInfo> columnInfoList) {
        // 输出文件内容
        final List<String> fileContent = new ArrayList<>(500);
        // 从末班加载文件内容
        MapperTemplate.addContent(param, item, columnInfoList, fileContent);
        String filePath = param.getMapperPath();
        if (!filePath.endsWith("/")) {
            filePath += "/";
        }
        filePath += item.getClassName() + "Mapper.xml";
        FileUtils.outPutToFileByLine(filePath, fileContent);
        LOGGER.info("成功生成mapper文件：{}", filePath);
    }

    /**
     * 生成dao文件.
     *
     * @param param          生成参数
     * @param item
     * @param columnInfoList 数据库字段信息列表
     */
    private static void generateDao(final GenerateSourceParam param, final GenerateSourceParamItem item, final List<ColumnInfo> columnInfoList) {
    }

}
