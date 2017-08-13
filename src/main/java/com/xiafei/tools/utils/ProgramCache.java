package com.xiafei.tools.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <P>Description: 编程式缓存。 </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/8/10</P>
 * <P>UPDATE DATE: 2017/8/10</P>
 *
 * @param <T> 缓存对象类型
 * @author qixiafei
 * @version 0.0.1-SNAPSHOT
 * @since java 1.7.0
 */
public class ProgramCache<T> {

    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor used when none specified in constructor.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * The table, resized as necessary. Length MUST Always be a power of two.
     */
    transient Item<T>[] table = null;


    /**
     * The number of key-value mappings contained in this map.
     */
    transient int size;

    /**
     * The next size value at which to resize (capacity * load factor).
     *
     * @serial
     */
    int threshold;

    /**
     * The load factor for the hash table.
     *
     * @serial
     */
    final float loadFactor;

    /**
     * The number of times this ProgramCache has been structurally modified
     * Structural modifications are those that change the number of mappings in
     * the ProgramCache or otherwise modify its internal structure (e.g.,
     * rehash).  This field is used to make iterators on Collection-views of
     * the ProgramCache fail-fast.  (See ConcurrentModificationException).
     */
    transient int modCount;

    /**
     * The default threshold of map capacity above which alternative hashing is
     * used for String keys. Alternative hashing reduces the incidence of
     * collisions due to weak hash code calculation for String keys.
     * <p/>
     * This value may be overridden by defining the system property
     * {@code jdk.map.althashing.threshold}. A property value of {@code 1}
     * forces alternative hashing to be used at all times whereas
     * {@code -1} value ensures that alternative hashing is never used.
     */
    static final int ALTERNATIVE_HASHING_THRESHOLD_DEFAULT = Integer.MAX_VALUE;

    /**
     * holds values which can't be initialized until after VM is booted.
     */
    private static class Holder {

        /**
         * Table capacity above which to switch to use alternative hashing.
         */
        static final int ALTERNATIVE_HASHING_THRESHOLD;

        static {
            String altThreshold = java.security.AccessController.doPrivileged(
                    new sun.security.action.GetPropertyAction(
                            "jdk.map.althashing.threshold"));

            int threshold;
            try {
                threshold = (null != altThreshold)
                        ? Integer.parseInt(altThreshold)
                        : ALTERNATIVE_HASHING_THRESHOLD_DEFAULT;

                // disable alternative hashing if -1
                if (threshold == -1) {
                    threshold = Integer.MAX_VALUE;
                }

                if (threshold < 0) {
                    throw new IllegalArgumentException("value must be positive integer.");
                }
            } catch (IllegalArgumentException failed) {
                throw new Error("Illegal value for 'jdk.map.althashing.threshold'", failed);
            }

            ALTERNATIVE_HASHING_THRESHOLD = threshold;
        }
    }

    /**
     * A randomizing value associated with this instance that is applied to
     * hash code of keys to make hash collisions harder to find. If 0 then
     * alternative hashing is disabled.
     */
    transient int hashSeed = 0;

    public ProgramCache(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: "
                    + initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: "
                    + loadFactor);

        this.loadFactor = loadFactor;
        threshold = initialCapacity;
    }

    public ProgramCache(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public ProgramCache() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public T put(T value) {
        if (value == null) {
            return null;
        }
        if (table == null) {
            inflateTable(threshold);
        }
        int hash = hash(value);
        int i = indexFor(hash, table.length);
        for (Item<T> e = table[i]; e != null; e = e.next) {
            T k;
            if (e.hash == hash && ((k = e.value) == value || value.equals(k))) {
                T oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }

        modCount++;
        addItem(hash, value, i);
        return null;
    }

    public T get(T key) {
        if (key == null)
            return null;
        Item<T> item = getItem(key);

        return null == item ? null : item.getValue();
    }

    public boolean contains(T key) {
        return getItem(key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T remove(T key) {
        Item<T> e = removeItem(key);
        return (e == null ? null : e.value);
    }

    public void putAll(List<T> values) {
        int numKeysToBeAdded = values.size();
        if (numKeysToBeAdded == 0)
            return;

        if (table == null) {
            inflateTable((int) Math.max(numKeysToBeAdded * loadFactor, threshold));
        }

        /*
         * Expand the map if the map if the number of mappings to be added
         * is greater than or equal to threshold.  This is conservative; the
         * obvious condition is (m.size() + size) >= threshold, but this
         * condition could result in a map with twice the appropriate capacity,
         * if the keys to be added overlap with the keys already in this map.
         * By using the conservative calculation, we subject ourself
         * to at most one extra resize.
         */
        if (numKeysToBeAdded > threshold) {
            int targetCapacity = (int) (numKeysToBeAdded / loadFactor + 1);
            if (targetCapacity > MAXIMUM_CAPACITY)
                targetCapacity = MAXIMUM_CAPACITY;
            int newCapacity = table.length;
            while (newCapacity < targetCapacity)
                newCapacity <<= 1;
            if (newCapacity > table.length)
                resize(newCapacity);
        }

        for (T v : values) put(v);
    }

    public void clear() {
        modCount++;
        Arrays.fill(table, null);
        size = 0;
    }

    private static int roundUpToPowerOf2(int number) {
        // assert number >= 0 : "number must be non-negative";
        int rounded = number >= MAXIMUM_CAPACITY
                ? MAXIMUM_CAPACITY
                : (rounded = Integer.highestOneBit(number)) != 0
                ? (Integer.bitCount(number) > 1) ? rounded << 1 : rounded
                : 1;

        return rounded;
    }

    /**
     * Inflates the table.
     */
    private void inflateTable(int toSize) {
        // Find a power of 2 >= toSize
        int capacity = roundUpToPowerOf2(toSize);

        threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
        table = new Item[capacity];
        initHashSeedAsNeeded(capacity);
    }

    /**
     * Initialize the hashing mask value. We defer initialization until we
     * really need it.
     */
    private final boolean initHashSeedAsNeeded(int capacity) {
        boolean currentAltHashing = hashSeed != 0;
        boolean useAltHashing = sun.misc.VM.isBooted() &&
                (capacity >= ProgramCache.Holder.ALTERNATIVE_HASHING_THRESHOLD);
        boolean switching = currentAltHashing ^ useAltHashing;
        if (switching) {
            hashSeed = useAltHashing
                    ? sun.misc.Hashing.randomHashSeed(this)
                    : 0;
        }
        return switching;
    }

    /**
     * Retrieve object hash code and applies a supplemental hash function to the
     * result hash, which defends against poor quality hash functions.  This is
     * critical because ProgramCache uses power-of-two length hash tables, that
     * otherwise encounter collisions for hashCodes that do not differ
     * in lower bits. Note: Null keys always map to hash 0, thus index 0.
     */
    private final int hash(Object k) {
        int h = hashSeed;
        if (0 != h && k instanceof String) {
            return sun.misc.Hashing.stringHash32((String) k);
        }

        h ^= k.hashCode();

        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * Returns index for hash code h.
     */
    private static int indexFor(int h, int length) {
        // assert Integer.bitCount(length) == 1 : "length must be a non-zero power of 2";
        return h & (length - 1);
    }


    /**
     * Returns the entry associated with the specified key in the
     * ProgramCache.  Returns null if the ProgramCache contains no mapping
     * for the key.
     */
    private final Item<T> getItem(T key) {
        if (size == 0) {
            return null;
        }

        int hash = (key == null) ? 0 : hash(key);
        for (Item<T> e = table[indexFor(hash, table.length)];
             e != null;
             e = e.next) {

            T k;
            if (e.hash == hash && ((k = e.value) == key || (key != null && key.equals(k))))
                return e;
        }
        return null;
    }


    private void resize(int newCapacity) {
        Item<T>[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        Item<T>[] newTable = new Item[newCapacity];
        transfer(newTable, initHashSeedAsNeeded(newCapacity));
        table = newTable;
        threshold = (int) Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
    }

    private void transfer(Item<T>[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        for (Item<T> e : table) {
            while (null != e) {
                Item<T> next = e.next;
                if (rehash) {
                    e.hash = null == e.value ? 0 : hash(e.value);
                }
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }


    final Item<T> removeItem(T key) {
        if (size == 0) {
            return null;
        }
        int hash = (key == null) ? 0 : hash(key);
        int i = indexFor(hash, table.length);
        Item<T> prev = table[i];
        Item<T> e = prev;

        while (e != null) {
            Item<T> next = e.next;
            Object k;
            if (e.hash == hash &&
                    ((k = e.value) == key || (key != null && key.equals(k)))) {
                modCount++;
                size--;
                if (prev == e)
                    table[i] = next;
                else
                    prev.next = next;
                return e;
            }
            prev = e;
            e = next;
        }

        return e;
    }


    private void addItem(int hash, T value, int bucketIndex) {
        if ((size >= threshold) && (null != table[bucketIndex])) {
            resize(2 * table.length);
            hash = hash(value);
            bucketIndex = indexFor(hash(value), table.length);
        }

        createItem(hash, value, bucketIndex);
    }

    private void createItem(int hash, T value, int bucketIndex) {
        Item<T> e = table[bucketIndex];
        table[bucketIndex] = new Item<>(hash, value, e);
        size++;
    }

    private static class Item<T> {
        T value;
        Item<T> next;
        int hash;

        Item(int h, T v, Item<T> n) {
            value = v;
            next = n;
            hash = h;
        }

        public final T getValue() {
            return value;
        }

        public final T setValue(T newValue) {
            T oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            return value.equals(o);
        }

        public final int hashCode() {
            return Objects.hashCode(value);
        }

        public final String toString() {
            return value.toString();
        }

    }
}
