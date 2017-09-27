package com.xiafei.tools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <P>Description: 数据库工具. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/13</P>
 * <P>UPDATE DATE: 2017/7/13</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public final class DBUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBUtils.class);
    /**
     * mysql-jdbc驱动名称.
     */
    public static final String MYSQL_JDBC_DRIVER_NAME = "com.mysql.jdbc.Driver";

    /**
     * 获得mysql数据库连接.
     *
     * @param url      数据库地址
     * @param user     用户名
     * @param password 密码
     * @return
     */
    public static Connection getMysqlConnection(final String url, final String user, final String password) {
        Connection conn = null;
        // 加载mysql-jdbc驱动.
        try {
            Class.forName(MYSQL_JDBC_DRIVER_NAME);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            LOGGER.error("无法加载mysql驱动", e);
            throw new RuntimeException("无法加载mysql驱动");
        } catch (SQLException e) {
            LOGGER.error("建立mysql数据库连接失败", e);
            throw new RuntimeException("建立mysql数据库连接失败");

        }
        return conn;
    }
}
