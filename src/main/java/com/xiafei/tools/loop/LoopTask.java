package com.xiafei.tools.loop;

/**
 * <P>Description: 固定时间间隔轮询任务类型. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/2/6</P>
 * <P>UPDATE DATE: 2018/2/6</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Deprecated
public interface LoopTask {

    /**
     * 循环执行的具体内容，不要抛出异常.
     */
    void invoke();

    /**
     * 循环周期间隔，单位ms.
     *
     * @return 循环周期的间隔，单位ms
     */
    long period();

    /**
     * 第一次执行时间，可以为空，若为空则启动项目延迟LoopConfig中写死的延迟后立即启动.
     *
     * @return 下一次执行的时间戳
     */
    Long firstTime();

    /**
     * 是否并发执行.
     *
     * @return true-并发，false-不并发
     */
    boolean concurrent();
}
