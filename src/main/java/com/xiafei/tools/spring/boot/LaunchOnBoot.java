package com.xiafei.tools.spring.boot;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * <P>Description: 随系统启动（在spring容器加载完毕后）类范例. </P>
 * <p>一个项目中允许有多个这种随容器启动类，在不考虑注入的情况，按照类名字母的ASCII码值顺序先后执行，
 * 但若有两个启动类A，B，默认先执行A的afterPropertiesSet()方法，如果A依赖B，那么会先执行B的afterPropertiesSet()方法</p>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/9/11</P>
 * <P>UPDATE DATE: 2017/9/11</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@Component
public class LaunchOnBoot implements InitializingBean {

    // 这个方法里的代码会在spring容器加载完毕后执行
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("spring has boot");
    }


}