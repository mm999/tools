package com.xiafei.tools.generatesource.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * <P>Description: jdbcType和JavaType对应关系枚举 </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/13</P>
 * <P>UPDATE DATE: 2017/7/13</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public enum JdbcTypeJavaTypeEnum {
    STRING("String", new ArrayList<String>() {{
        add("VARCHAR");
        add("VARCHAR2");
        add("CHAR");
        add("CLOB");
        add("LONGVARCHAR");
        add("NVARCHAR");
        add("TEXT");
    }}),
    LONG("Long", new ArrayList<String>() {{
        add("BIGINT");
    }}),
    BYTE("Byte", new ArrayList<String>() {{
        add("TINYINT");
    }}),
    BYTE_ARRAY("Byte[]", new ArrayList<String>() {{
        add("BINARY");
        add("BLOB");
        add("LONGVARBINARY");
        add("VARBINARY");
    }}),
    BOOLEAN("Boolean", new ArrayList<String>() {{
        add("BIT");
    }}),
    DATE("Date", new ArrayList<String>() {{
        add("DATE");
        add("TIMESTAMP");
        add("DATETIME");
        add("TIME");
    }}),
    BIGDECIMAL("BigDecimal", new ArrayList<String>() {{
        add("DECIMAL");
        add("NUMERIC");
    }}),
    DOUBLE("Double", new ArrayList<String>() {{
        add("DOUBLE");
        add("DOUBLE PRECISION");
        add("FLOAT");
    }}),
    FLOAT("Float", new ArrayList<String>() {{
        add("REAL");
    }}),
    INTEGER("Integer", new ArrayList<String>() {{
        add("INTEGER");
        add("INT");
        add("SMALLINT");
    }}),
    OBJECT("Object", new ArrayList<String>() {{
        add("JAVA_OBJECT");
        add("OTHER");
    }});

    /**
     * java类型.
     */
    public final String javaType;
    /**
     * java类型对应的jdbc类型.s
     */
    public final List<String> jdbcTypeList;

    JdbcTypeJavaTypeEnum(final String javaType, final List<String> jdbcTypeList) {
        this.javaType = javaType;
        this.jdbcTypeList = jdbcTypeList;
    }

    public static JdbcTypeJavaTypeEnum instance(final String jdbcType) {
        if (jdbcType == null) {
            return null;
        }
        for (JdbcTypeJavaTypeEnum e : values()) {
            if (e.jdbcTypeList.contains(jdbcType)) {
                return e;
            }
        }
        return null;
    }
}
