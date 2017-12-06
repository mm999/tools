package com.xiafei.tools.retry;

import org.slf4j.Logger;

/**
 * <P>Description: 重试工具（针对Dubbo接口，返回值类型是Message，Code是以本包内的Code场景使用）. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/5</P>
 * <P>UPDATE DATE: 2017/12/5</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public class RetryUtils {

    private RetryUtils() {

    }

    /**
     * 初始化重试时间间隔（毫秒）.
     */
    private static final int INIT_DELAY_MILLIS = 1;
    /**
     * 最大重试时间间隔.
     */
    private static final int MAX_DELAY_MILLIS = 2096;

    /**
     * 线程本地变量-当前重试时间间隔.
     */
    private static final ThreadLocal<Integer> DELAY_MILLIS = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return INIT_DELAY_MILLIS;
        }

        @Override
        public Integer get() {
            if (DELAY_MILLIS.get() < MAX_DELAY_MILLIS) {
                int newDelaySeconds = super.get() << 1;
                if (newDelaySeconds > MAX_DELAY_MILLIS)
                    newDelaySeconds = MAX_DELAY_MILLIS;
                super.set(newDelaySeconds);
                return newDelaySeconds;
            } else {
                return MAX_DELAY_MILLIS;
            }
        }

        @Override
        public void set(Integer value) {
            super.set(value);
        }

        @Override
        public void remove() {
            super.remove();
        }
    };

    /**
     * 调用dubbo接口并返回Message类型的重试方法入口.
     * 这个方法实现的是调用Dubbo接口网络异常或返回服务端系统异常时会采用延迟时间2的指数幂策略重试;
     * 初始重试时间由本地常量[INIT_DELAY_MILLIS]决定，最大重试时间间隔由本地常量[MAX_DELAY_MILLIS]决定'
     * 使用这方法的场景:当调用dubbo接口，想要保证绝对提交成功的情况下，前提是服务端要做好幂等.
     *
     * @param task       要执行的调用任务类实现.
     * @param methodName 日志记录的方法名.
     * @param invokeDesc 调用接口描述.
     * @param log        日志接口对象
     * @param args       调用接口的参数
     * @param <T>        Message返回值中Data的泛型
     * @return 调用Dubbo接口后返回值
     */
    public static <T> Message<T> retryDubboReturnMessage(RetryTask<T> task, String methodName, String invokeDesc,
                                                         Logger log, Object... args) {
        try {
            while (true) {
                final Message<T> msg;
                // 如果发生需要重试错误，本次延迟多久
                int currentDelayMillis = DELAY_MILLIS.get();
                try {
                    log.info("{},invoke {},param={}", methodName, invokeDesc, args);
                    msg = task.invoke(args);
                    log.info("{},invoke {},resp={}", methodName, invokeDesc, msg);

                } catch (Exception e) {
                    log.error("{},invoke {}, catch Exception,wait {} millis to retry!", methodName, invokeDesc,
                            currentDelayMillis, e);
                    try {
                        Thread.sleep(currentDelayMillis);
                    } catch (InterruptedException ex) {
                        // 线程被提前叫醒了
                        log.error("{},invoke {}, catch Exception and Thread.sleep throw Exception,retry immediately!",
                                methodName, invokeDesc, ex);
                    }
                    continue;
                }
                if (!Messages.isSuccess(msg)) {
                    if (msg.getCode() == Code.SYSTEMEXCEPTION.getValue()) {
                        log.error("{},invoke {}，server-side SYSTEMEXCEPTION" +
                                ", wait {} millis to retry", methodName, invokeDesc, currentDelayMillis);
                        try {
                            Thread.sleep(currentDelayMillis);
                        } catch (InterruptedException e) {
                            // 线程被提前叫醒了
                            log.error("{},invoke {}, server-side SYSTEMEXCEPTION and Thread.sleep throw Exception",
                                    methodName, invokeDesc, e);
                        }
                    } else {
                        return msg;
                    }
                } else {
                    return msg;
                }
            }

        } finally {
            DELAY_MILLIS.set(INIT_DELAY_MILLIS);
        }

    }

    /**
     * 重试的任务，对接口的真正调用写在实现这个接口的类的invoke方法的重写方法里.
     *
     * @param <T> Dubbo接口返回对象Message中Data的泛型
     */
    public interface RetryTask<T> {
        /**
         * 调用Dubbo接口真实方法.
         *
         * @param args 调用dubbo接口的参数
         * @return 调用Dubbo接口返回值
         * @throws Exception 调用dubbo接口抛出的异常
         */
        Message<T> invoke(Object... args) throws Exception;
    }


}
