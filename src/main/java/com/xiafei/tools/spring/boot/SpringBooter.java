package com.xiafei.tools.spring.boot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <P>Description: spring启动. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/9/11</P>
 * <P>UPDATE DATE: 2017/9/11</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class SpringBooter {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-config.xml");

    }
}
