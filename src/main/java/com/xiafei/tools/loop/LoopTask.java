package com.xiafei.tools.loop;

/**
 * <P>Description: 定时任务类型. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/2/6</P>
 * <P>UPDATE DATE: 2018/2/6</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public interface LoopTask {

    /**
     * 循环执行的具体内容，不要抛出异常.
     */
    void invoke();

    /**
     * 延迟多久执行，单位ms.
     *
     * @return 延迟多久执行，单位ms
     */
    long delay();

    /**
     * 是否并发执行.
     *
     * @return true-并发，false-不并发
     */
    boolean concurrent();
}
