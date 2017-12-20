package com.xiafei.tools.enums;

/**
 * <P>Description: 枚举方法示例. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/20</P>
 * <P>UPDATE DATE: 2017/12/20</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public enum TestEnum {

    A {
        public Integer getCode() {
            return 1;
        }
    },
    B {
        @Override
        protected Integer getCode() {
            return null;
        }
    };

    protected abstract Integer getCode();
}
