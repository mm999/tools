package com.xiafei.tools.httpclient;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * <P>Description: 时间递增执行工具. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/2/6</P>
 * <P>UPDATE DATE: 2018/2/6</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Slf4j
public class AscendingInvoke {

    /**
     * 延迟调用递增最大等级.
     */
    private static final int MAX_LEVEL = 8;

    /**
     * 每个线程已经执行过的调用等级.
     */
    private static final ThreadLocal<Integer> THREAD_LEVEL =
            ThreadLocal.withInitial(() -> 1);

    /**
     * 每个线程独立计算下一次轮询时间.
     */
    private static final ThreadLocal<Long> THREAD_DELAY =
            ThreadLocal.withInitial(() -> TimeUnit.MINUTES.toMillis(1));

    /**
     * 开始递增调用.
     *
     * @param task 调用执行的具体任务
     */
    public static void start(final Task task) {
        new Thread(() -> doIt(task)).start();
    }

    /**
     * 开始递增调用.
     *
     * @param task 调用执行的具体任务
     */
    private static void doIt(final Task task) {

        try {
            task.invoke();
        } catch (Exception e) {
            final Integer level = THREAD_LEVEL.get();
            long curDelay = THREAD_DELAY.get();
            long curDelayMinutes =  curDelay/60000;
            if (level == MAX_LEVEL) {
                log.error("时间递增调用任务已经达到最大时间间隔{}分钟、最大尝试次数{}次，放弃该条回调任务，任务数据={}",
                        curDelayMinutes, MAX_LEVEL, task.desc(), e);
                THREAD_LEVEL.remove();
                THREAD_DELAY.remove();
                return;
            }
            long nextDelay = curDelay << 1;
            long nextDelayMinutes =nextDelay/60000;
            log.warn("递增调用任务执行失败，增加时间间隔，当前时间间隔{}分钟，增加后时间间隔{}分钟，当前第{}次尝试，一共会尝试{}次，"
                    + "任务数据={}", curDelayMinutes, nextDelayMinutes, level, MAX_LEVEL, task.desc(), e);
            THREAD_LEVEL.set(level + 1);
            THREAD_DELAY.set(nextDelay);
            try {
                Thread.sleep(nextDelay);
            } catch (InterruptedException e1) {
                log.error("线程收到中断请求，响应中断，任务结束，任务数据={}", task.desc(), e);
                return;
            }
            doIt(task);
        }
    }

    /**
     * 递增调用任务.
     */
    public interface Task {

        /**
         * 执行回调任务，如果回调失败一定要抛出异常.
         */
        void invoke() throws Exception;

        /**
         * 返回任务描述，用于记录日志.
         */
        String desc();
    }

}
