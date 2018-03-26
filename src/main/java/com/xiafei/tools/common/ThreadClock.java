package com.xiafei.tools.common;

import java.util.concurrent.TimeUnit;

/**
 * <P>Description: 线程安全的计时器. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/21</P>
 * <P>UPDATE DATE: 2017/12/21</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public class ThreadClock {

    public static void main(String[] args) throws InterruptedException {
        begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("pause:" + pause());
        TimeUnit.SECONDS.sleep(1);
        System.out.println("pause:" + pause());
        TimeUnit.SECONDS.sleep(1);
        System.out.println("goOn:" + goOn());
        TimeUnit.SECONDS.sleep(1);
        System.out.println("goOn:" + goOn());

        TimeUnit.SECONDS.sleep(1);
        System.out.println("pause:" + pause());
        TimeUnit.SECONDS.sleep(1);
        System.out.println("goOn:" + goOn());
        TimeUnit.SECONDS.sleep(1);
        System.out.println("stop:" + stop());
    }

    /**
     * 开始时间.
     */
    private static ThreadLocal<Long> TIME_BEGIN = new ThreadLocal<Long>();

    /**
     * 上一次有效暂停的时间（暂停的情况下再次暂停不更新这里）.
     */
    private static ThreadLocal<Long> LAST_PAUSE = new ThreadLocal<Long>();

    /**
     * 暂停消耗的时间.
     */
    private static ThreadLocal<Long> PAUSE_EXPEND = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            // 该方法会在第一次get时被调用（如果没有set过的话）,每个线程会调用一次
            return 0L;
        }
    };

    /**
     * 是否被暂停的状态.
     */
    private static ThreadLocal<Boolean> IS_PAUSE = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            // 该方法会在第一次get时被调用（如果没有set过的话）,每个线程会调用一次
            return false;
        }
    };


    /**
     * 开始计时
     */
    public static void begin() {
        TIME_BEGIN.set(System.currentTimeMillis());
    }

    /**
     * 暂停，返回消逝的时间.
     *
     * @return 消逝的时间
     */
    public static long pause() {
        long elapse;
        if (IS_PAUSE.get()) {
            elapse = LAST_PAUSE.get() - TIME_BEGIN.get() - PAUSE_EXPEND.get();
        } else {

            long now = System.currentTimeMillis();
            elapse = now - TIME_BEGIN.get() - PAUSE_EXPEND.get();

            LAST_PAUSE.set(now);
            IS_PAUSE.set(true);
        }
        return elapse;
    }

    /**
     * 继续，返回消逝的时间.
     *
     * @return 消逝的时间
     */
    public static long goOn() {
        long elapse;
        if (IS_PAUSE.get()) {

            long now = System.currentTimeMillis();
            elapse = LAST_PAUSE.get() - TIME_BEGIN.get() - PAUSE_EXPEND.get();

            PAUSE_EXPEND.set(PAUSE_EXPEND.get() + now - LAST_PAUSE.get());
            IS_PAUSE.set(false);

        } else {
            elapse = System.currentTimeMillis() - TIME_BEGIN.get() - PAUSE_EXPEND.get();
        }
        return elapse;
    }

    /**
     * 停止，返回消逝的时间.
     *
     * @return 消逝的时间
     */
    public static long stop() {
        return System.currentTimeMillis() - TIME_BEGIN.get() - PAUSE_EXPEND.get();
    }
}
