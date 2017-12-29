package com.xiafei.tools.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <P>Description: Jvm超时类缓存，用的时候自己new. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/29</P>
 * <P>UPDATE DATE: 2017/12/29</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public class JvmExCache<D> {

    /**
     * 声明读写锁.
     */
    private static ReentrantReadWriteLock RW;

    /**
     * 读锁.
     */
    private static Lock R;

    /**
     * 写锁.
     */
    private static Lock W;

    /**
     * 缓存失效的时间点.
     */
    private long exAt;

    /**
     * 缓存失效间隔.
     */
    private long exInterval;

    /**
     * 缓存数据.
     */
    private D data;

    /**
     * 初始化缓存对象，使用公平锁.
     *
     * @param exInterval 超时时间
     */
    public JvmExCache(final long exInterval) {
        RW = new ReentrantReadWriteLock(true);
        R = RW.readLock();
        W = RW.writeLock();
        this.exInterval = exInterval;
    }

    /**
     * 初始化缓存对象，指定是否公平锁.
     *
     * @param exInterval 超时时间
     * @param isFair     是否公平锁s
     */
    public JvmExCache(final long exInterval, final boolean isFair) {
        RW = new ReentrantReadWriteLock(isFair);
        R = RW.readLock();
        W = RW.writeLock();
        this.exInterval = exInterval;
    }

    /**
     * 从缓存中取数据，方法会自动判断如果过期的话刷新数据.
     *
     * @param task 能够获得缓存中数据的接口实现
     * @return 缓存中的数据.
     * @throws Exception task可能抛出的异常
     */
    public D getAndRefreshIfEx(final Task<D> task) throws Exception {
        R.lock();

        if (data == null) {
            R.unlock();
            return refreshData(task);
        }

        if (exAt < System.currentTimeMillis()) {
            R.unlock();
            return refreshData(task);
        } else {
            try {
                return data;
            } finally {
                R.unlock();
            }
        }
    }

    private D refreshData(final Task<D> task) throws Exception {
        if (W.tryLock()) {
            try {
                data = task.invoke();
                exAt = System.currentTimeMillis() + exInterval;
                return data;
            } finally {
                W.unlock();
            }
        } else {
            R.lock();
            try {
                return data;
            } finally {
                R.unlock();
            }
        }
    }


    /**
     * 内部接口.
     *
     * @param <D> 接口方法返回数据类型泛型
     */
    public interface Task<D> {

        /**
         * 调用该方法可以返回缓存数据对象.
         *
         * @return 缓存的数据对象
         * @throws Exception 接口实现可能抛出异常
         */
        D invoke() throws Exception;
    }
}
