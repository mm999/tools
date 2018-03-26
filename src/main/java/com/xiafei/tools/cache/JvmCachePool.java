package com.xiafei.tools.cache;

import com.xiafei.tools.common.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <P>Description: 基于jvm内部的缓存池，一个项目可以统一用一个. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/20</P>
 * <P>UPDATE DATE: 2017/12/20</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Slf4j
@Component
public class JvmCachePool {

    /**
     * 缓存池.
     */
    private static final ConcurrentHashMap<String, Item> CACHE = new ConcurrentHashMap<>();

    /**
     * 缓存失效时间.
     */
    @Value("${cache.jvm.expire.millis}")
    private Long millis;

    /**
     * 尝试从缓存中读取数据.
     * 如果缓存命中，直接返回数据。
     * 如果缓存未命中，调用获取数据的方法，然后将结果缓存并返回数据。
     * 如果缓存命中，但已过失效时间，当做未命中处理。
     *
     * @param key     缓存key，需要唯一
     * @param lockObj 用于同步锁的对象
     * @param task    可以获取数据的实现
     * @param <D>     缓存对象泛型
     * @throws Exception task执行抛出的异常
     */
    public <D> D getAndRefreshIfExpire(final String key, final Object lockObj, final Task<D> task) throws Exception {
        if (task == null || StringUtils.isBlank(key)) {
            throw new Exception();
        }
        Item<D> cached = CACHE.get(key);
        if (cached == null) {
            // 只有缓存被初始化时才有可能进入这个分支
            synchronized (lockObj) {
                cached = CACHE.get(key);
                if (cached == null) {
                    cached = new Item<>(task.invoke());
                    CACHE.put(key, cached);
                } else {
                    log.debug("key=[{}],load from jvmCache", key);
                }
            }
        } else if ((System.currentTimeMillis() - cached.getInitTime()) > millis) {
            synchronized (lockObj) {
                Item<D> maybeNewItem = CACHE.get(key);
                if (cached == maybeNewItem) {
                    CACHE.put(key, new Item<>(task.invoke()));
                } else {
                    log.debug("key=[{}],load from jvmCache", key);
                    return maybeNewItem.getData();
                }
            }
        } else {
            log.debug("key=[{}],load from jvmCache", key);
        }
        return cached.getData();
    }


    public static class Item<T> {
        /**
         * 初始化时间
         */
        private long initTime = System.currentTimeMillis();
        private T data;

        Item(T data) {
            this.data = data;
        }

        public long getInitTime() {
            return initTime;
        }

        public T getData() {
            return data;
        }
    }

    public interface Task<D> {
        D invoke() throws Exception;
    }
}
