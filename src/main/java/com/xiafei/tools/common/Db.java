package com.xiafei.tools.common;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public final class Db {

    /**
     * mysql-jdbc驱动名称.
     */
    private static final String MYSQL_JDBC_DRIVER_NAME = "com.mysql.jdbc.Driver";


    /**
     * orcle-jdbc驱动名称.
     */
    private static final String ORACLE_JDBC_DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";

    /**
     * 获得mysql数据库连接.
     *
     * @param url      数据库地址
     * @param user     用户名
     * @param password 密码
     * @return mysql数据库连接
     */
    public static Connection getMysqlConn(final String url, final String user, final String password) {

        return getConn(MYSQL_JDBC_DRIVER_NAME, url, user, password);
    }

    /**
     * 获得oracle数据库连接.
     *
     * @param url      数据库地址
     * @param user     用户名
     * @param password 密码
     * @return oracle数据库连接
     */
    public static Connection getOracleConn(final String url, final String user, final String password) {
        return getConn(ORACLE_JDBC_DRIVER_NAME, url, user, password);
    }

    private static Connection getConn(final String driverClass, final String url,
                                      final String user, final String password) {
        // 加载mysql-jdbc驱动.
        try {
            Class.forName(MYSQL_JDBC_DRIVER_NAME);
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            log.error("无法加载{}驱动", driverClass, e);
            throw new RuntimeException("无法加载数据库驱动" + driverClass);
        } catch (SQLException e) {
            log.error("建立{}数据库连接失败", driverClass, e);
            throw new RuntimeException("建立数据库连接失败" + driverClass);

        }
    }
}
