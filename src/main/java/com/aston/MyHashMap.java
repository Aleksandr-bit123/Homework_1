package com.aston;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Custom realize HashMap (not synchronized) with basic methods:
 * {@code get, put, delete, values, keySet, entrySet}
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author Maksimov Aleksandr
 * <p>
 */
class MyHashMap<K, V> {

    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int INITIAL_CAPACITY = 1 << 4; // aka 16

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30; //aka 1073741824

    /**
     * The load factor used when none specified in constructor.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * Basic hash bin node, used for all entries.
     **/
    static class Entry<K, V> {

        final K key;
        V value;
        MyHashMap.Entry<K, V> next;
        Integer bucketIndex;

        Entry(K key, V value, MyHashMap.Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;

            return o instanceof MyHashMap.Entry<?, ?> e
                    && Objects.equals(key, e.getKey())
                    && Objects.equals(value, e.getValue());
        }
    }

    private final MyHashMap.Entry<K, V>[] buckets;

    //The number of key-value mappings contained in this map.
    private int size = 0;

    /* ------------------------------------------------------------ */
    // Constructors

    /**
     * Constructs an empty {@code MyHashMap} with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public MyHashMap() {
        this(INITIAL_CAPACITY);
    }

    private MyHashMap(int capacity) {
        if (capacity > MAXIMUM_CAPACITY) capacity = MAXIMUM_CAPACITY;
        this.buckets = new Entry[capacity];
    }

    /* ------------------------------------------------------------ */
    // Private methods

    private int getBucketSize() {
        return this.buckets.length;
    }

    //Hash-function. Return index of bucket.
    private int getBucketIndex(K key) {
        return getHash(key) % getBucketSize();
    }

    //Return hash code of key
    private int getHash(K key) {
        return Objects.hashCode(key);
    }

    /* ------------------------------------------------------------ */
    // Public methods

    /**
     * Return the number of key-value mappings contained in this map.
     **/
    public int getSize() {
        return size;
    }
    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or
     * {@code null} if there was no mapping for {@code key}.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with {@code key}.)
     */
    public V put(K key, V value) {
        Entry<K, V> entry = new Entry<>(key, value, null);

        int bucketIndex = getBucketIndex(key);
        entry.bucketIndex = bucketIndex;

        Entry<K, V> existing = buckets[bucketIndex];
        if (existing == null) {
            buckets[bucketIndex] = entry;
            size++;
            return entry.getValue();
        } else {
            while (existing.next != null) {
                if (Objects.equals(existing.key, key)) {
                    existing.value = value;
                    return existing.value;
                }
                existing = existing.next;
            }

            if (Objects.equals(existing.key, key)) {
                existing.value = value;
                return existing.value;
            } else {
                existing.next = entry;
                size++;
                return entry.getValue();
            }
        }
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param key key with which the specified value is to be associated
     * @return the node, or {@code null} if none
     **/
    public V get(K key) {
        Entry<K, V> bucket = buckets[getBucketIndex(key)];

        while (bucket != null) {
            if (Objects.equals(bucket.key, key)) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with {@code key}, or
     * {@code null} if there was no mapping for {@code key}.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with {@code key}.)
     */
    public V remove(K key) {
        int bucketIndex = getBucketIndex(key);
        Entry<K, V> existing = buckets[bucketIndex];

        if (existing == null) {
            return null;
        } else {
            Entry<K, V> previousExisting = buckets[bucketIndex];
            V value;
            while (existing.next != null) {
                if (Objects.equals(existing.key, key)) {
                   if (previousExisting == existing) {
                       buckets[bucketIndex] = existing.next;
                   } else {
                       previousExisting.next = existing.next;
                   }
                    value = existing.value;
                    size--;
                    return value;
                }
                previousExisting = existing;
                existing = existing.next;
            }
            if (Objects.equals(existing.key, key)) {
                value = existing.value;
                buckets[bucketIndex] = null;
                size--;
                return value;
            } else {
                return null;
            }
        }
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     *
     * @return a set view of the keys contained in this map
     */
    public Set<K> keySet() {
        KeyIterator iterator = new KeyIterator();
        Set<K> keySet = new HashSet<>();
        while (iterator.hasNext()) {
            keySet.add(iterator.next());
        }
        return keySet;
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     *
     * @return a view of the values contained in this map
     */
    public Collection<V> values() {
        ValueIterator iterator = new ValueIterator();
        Collection<V> values = new LinkedList<>();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }
        return values;
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     *
     * @return a set view of the mappings contained in this map
     */
    public Set<Entry<K, V>> entrySet() {
        EntryIterator iterator = new EntryIterator();
        Set<Entry<K, V>> entrySet = new HashSet<>();
        while (iterator.hasNext()) {
            entrySet.add(iterator.next());
        }
        return entrySet;
    }

    /* ------------------------------------------------------------ */
    // iterators
    abstract class HashIterator {
        Entry<K, V> next;        // next entry to return
        Entry<K, V> current;     // current entry
        int index;             // current slot

        HashIterator() {
            Entry<K, V>[] t = buckets;
            current = next = null;
            index = 0;
            if (t != null && size > 0) { // advance to first entry
                do {
                } while (index < t.length && (next = t[index++]) == null);
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Entry<K, V> nextNode() {
            Entry<K, V>[] t;
            Entry<K, V> e = next;
            if (e == null)
                throw new NoSuchElementException();
            if ((next = (current = e).next) == null && (t = buckets) != null) {
                do {
                } while (index < t.length && (next = t[index++]) == null);
            }
            return e;
        }

    }

    final class KeyIterator extends HashIterator
            implements Iterator<K> {
        public K next() {
            return nextNode().key;
        }
    }

    final class ValueIterator extends HashIterator
            implements Iterator<V> {
        public V next() {
            return nextNode().value;
        }
    }

    final class EntryIterator extends HashIterator
            implements Iterator<Entry<K, V>> {
        public Entry<K, V> next() {
            return nextNode();
        }
    }

    @Override
    public String toString() {
        Set<Entry<K,V>> entrySet = entrySet();
        entrySet.stream().collect(Collectors.groupingBy(e -> e.bucketIndex))
                .entrySet()
                .forEach(e -> System.out.println(e.toString()));
        return null;
    }
}
