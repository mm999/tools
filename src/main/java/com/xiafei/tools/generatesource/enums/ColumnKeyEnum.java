package com.xiafei.tools.generatesource.enums;

/**
 * <P>Description: 数据库字段约束名. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/13</P>
 * <P>UPDATE DATE: 2017/7/13</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public enum ColumnKeyEnum {

    MYSQL_PRIMARY("PRI", "主键");

    public final String code;
    public final String desc;

    ColumnKeyEnum(final String pCode, final String pDesc) {
        code = pCode;
        desc = pDesc;
    }

    public static ColumnKeyEnum instance(final String code) {
        if (code == null) {
            return null;
        }
        for (ColumnKeyEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

}
