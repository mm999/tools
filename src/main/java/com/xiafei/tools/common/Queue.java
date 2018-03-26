package com.xiafei.tools.common;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <P>Description: FIFO队列，线程安全. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/7/10</P>
 * <P>UPDATE DATE: 2017/7/10</P>
 *
 * @param <T> 队列中放的对象类型
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
public class Queue<T> implements Iterable<T> {

    /**
     * 默认容量*2因子.
     */
    private static final float DEFAULT_INCREASE_FACTOR = 1.0f;

    /**
     * 默认容量减半因子.
     */
    private static final float DEFAULT_REDUCE_FACTOR = 0.25f;

    /**
     * 实际使用的容量*2因子.
     */
    private final float increaseFactor;

    /**
     * 实际使用的容量减半因子.
     */
    private final float reduceFactor;

    /**
     * 队列初始大小.
     */
    private final int initCap;

    /**
     * 队列内容.
     */
    private T[] content;

    /**
     * 队列中实际存放数量.
     */
    private volatile int n;

    /**
     * 头指针.
     */
    private volatile int front;

    /**
     * 无参构造器.
     */
    @SuppressWarnings("unchecked")
    public Queue() {
        super();
        this.initCap = 1 << 2;
        this.increaseFactor = DEFAULT_INCREASE_FACTOR;
        this.reduceFactor = DEFAULT_REDUCE_FACTOR;
        this.content = (T[]) new Object[initCap];
    }

    /**
     * 可指定队列初始大小的构造器.
     *
     * @param pInitCap 初始大小
     */
    @SuppressWarnings("unchecked")
    public Queue(final int pInitCap) {
        this.initCap = pInitCap;
        this.increaseFactor = DEFAULT_INCREASE_FACTOR;
        this.reduceFactor = DEFAULT_REDUCE_FACTOR;
        this.content = (T[]) new Object[initCap];
    }

    /**
     * 可指定队列初始大小和容量*2因子的构造器.
     *
     * @param pInitCap        初始大小
     * @param pIncreaseFactor 容量*2因子，0到1之间的浮点数
     */
    @SuppressWarnings("unchecked")
    public Queue(final int pInitCap, final float pIncreaseFactor) {
        this.initCap = pInitCap;
        this.increaseFactor = pIncreaseFactor;
        this.reduceFactor = DEFAULT_REDUCE_FACTOR;
        this.content = (T[]) new Object[initCap];
    }

    /**
     * 可指定队列初始大小和容量*2因子的构造器.
     *
     * @param pInitCap        初始大小
     * @param pIncreaseFactor 容量*2因子，0到1之间的浮点数,应大于容量减半因子
     * @param pReduceFactor   容量减半因子，必须小于容量*2因子
     */
    @SuppressWarnings("unchecked")
    public Queue(final int pInitCap, final float pIncreaseFactor, final float pReduceFactor) {
        if (pIncreaseFactor <= pReduceFactor) {
            throw new IllegalArgumentException("容量减半因子，必须小于容量*2因子");
        }
        this.initCap = pInitCap;
        this.increaseFactor = pIncreaseFactor;
        this.reduceFactor = pReduceFactor;
        this.content = (T[]) new Object[initCap];
    }

    /**
     * 在队列末尾插入对象，不允许插入null.
     *
     * @param item 要入队的元素
     */
    public synchronized void enqueue(final T item) {
        if (item == null) {
            throw new IllegalArgumentException("队列不允许插入null元素");
        }

        if ((n + front) == content.length) {
            // 如果队列满了

            if (n >= (int) (content.length * increaseFactor)) {
                // 如果数量n达到扩容条件
                final int resizeTo = Math.min(n * 2, Integer.MAX_VALUE);
                resize(resizeTo);
            } else {

                if (front > 0) {
                    // 若头指针不指向0，先不扩容，将头指针指向起始位置.
                    tidy();
                } else {
                    // 若头指针指向0还是满了，扩容
                    final int resizeTo = Math.min(n * 2, Integer.MAX_VALUE);
                    resize(resizeTo);
                }
            }
        }

        content[front + n++] = item;
    }

    /**
     * 出队.
     *
     * @return 先进入队列的元素
     */
    public synchronized T dequeue() {
        if (n == 0) {
            return null;
        }
        final T result = content[front];
        // 防止对象游离
        content[front++] = null;
        n--;
        // 当头指针移动到队列的一半时，将内容整体左移顶头，不改变队列长度
        if (front >= n) {
            tidy();
        }

        if (n > initCap && n < (int) (content.length * reduceFactor)) {
            resize(content.length / 2);
        }
        return result;
    }


    /**
     * 返回队列中元素的数量.
     *
     * @return 队列中元素的数量
     */
    public int size() {
        return n;
    }

    /**
     * 判断队列是否为空的方法.
     *
     * @return true - 空，false - 非空
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 返回迭代器.
     *
     * @return 该队列对象的迭代器s
     */
    @Override
    public Iterator<T> iterator() {
        return new QueueIterator();
    }

    /**
     * 改变队列内置对象数组的大小.
     *
     * @param resizeTo 改变后队列内置对象数组的大小
     */
    @SuppressWarnings("unchecked")
    private void resize(final int resizeTo) {
        final T[] newContent = (T[]) new Object[resizeTo];
        System.arraycopy(content, front, newContent, 0, n);
        content = newContent;
        front = 0;
    }


    /**
     * 整理队列，将内容左移顶头.
     */
    @SuppressWarnings("unchecked")
    private void tidy() {
        final T[] newContent = (T[]) new Object[content.length];
        System.arraycopy(content, front, newContent, 0, n);
        content = newContent;
        front = 0;
    }

    /**
     * 队列迭代器.
     */
    private final class QueueIterator implements Iterator<T> {


        /**
         * 迭代器对最大值，初始化成队列元素个数.
         */
        private int max;

        /**
         * 迭代器当前位置.
         */
        private int index;

        /**
         * 构造器.
         */
        private QueueIterator() {
            this.max = n;
            this.index = front;
        }

        @Override
        public boolean hasNext() {
            return index < max;
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return content[index++];
        }

        @Override
        public void remove() {
            // 队列不允许在迭代过程中删除元素
            throw new UnsupportedOperationException("remove() method is not supported");
        }

    }

}
