/*
Copyright 2022~Forever xasync.com under one or more contributor authorized.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.xasync.island.datastruct;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * SoftConcurrentHashMap
 *
 * @author xasync.com
 */
public class SoftConcurrentHashMap<K, V> extends ConcurrentHashMap<K, SoftConcurrentHashMap.SoftDataNode<K, V>> {

    private final static int CLEAR_THRESHOLD = 10;

    /**
     * Declares the instance of ReferenceQueue for constructing each SoftDataNode and
     * observing if the SoftDataNode is release by GC
     */
    private final ReferenceQueue<V> referenceQueue = new ReferenceQueue<>();

    /**
     *
     */
    private final LongAdder nullValueOfNodeCount = new LongAdder();

    /**
     * Non-parameter constructor
     */
    public SoftConcurrentHashMap() {
        super();
    }

    /**
     * Create an instance with the initial capacity you expect, and the loadFactor defaults 0.75
     *
     * @param initialCapacity The number of elements you expect the container to hold
     */
    public SoftConcurrentHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Creates a new, empty map with an initial table size based on the given number of elements (initialCapacity)
     * and initial table density (loadFactor).
     *
     * @param initialCapacity The number of elements you expect the container to hold
     * @param loadFactor      the load factor (table density) for establishing the initial table size
     */
    public SoftConcurrentHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Creates a new, empty map with an initial table size based on the given number of elements (initialCapacity),
     * initial table density (loadFactor), and number of concurrently updating threads (concurrencyLevel).
     *
     * @param initialCapacity  The number of elements you expect the container to hold
     * @param loadFactor       the load factor (table density) for establishing the initial table size
     * @param concurrencyLevel the estimated number of concurrently updating threads.
     */
    public SoftConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        super(initialCapacity, loadFactor, concurrencyLevel);
    }

    /**
     * Maps the specified key to the specified value in this table. Neither the key nor the value can be null.
     * The value will be converted to SoftDataNode automatically.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     */
    public void putAsSoft(K key, V value) {
        SoftDataNode<K, V> sdn = new SoftDataNode<>(key, value, referenceQueue);
        this.put(key, sdn);
    }


    /**
     * If the specified key is not already associated with a value (or is mapped to null) associates it with the given value
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     */
    public void putAsSoftIfAbsent(K key, V value) {
        if (!this.containsKey(key)) {
            putAsSoft(key, value);
        }
    }

    /**
     * Copies all mappings from the specified map to this one.
     *
     * @param map mappings to be stored in this map
     */
    public void putAllAsSoft(Map<K, V> map) {
        if (Objects.isNull(map) || map.isEmpty()) {
            return;
        }
        map.forEach(this::putAsSoft);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     *
     * @param key key with which the specified value is to be associated
     * @return the value
     */
    public V getOnSoft(K key) {
        SoftDataNode<K, V> sdn = this.get(key);
        if (Objects.isNull(sdn)) {
            return null;
        }
        V value = sdn.get();
        //the value is released by GC and returns null directly.
        if (Objects.isNull(value)) {
            clearInvalidDataNode(key);
            return null;
        }
        return value;
    }

    /**
     * Returns the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key.
     *
     * @param key          key with which the specified value is to be associated
     * @param defaultValue the default value if this map contains no mapping for the key.
     * @return the value
     */
    public V getOnSoftOrDefault(K key, V defaultValue) {
        V value = getOnSoft(key);
        return Objects.nonNull(value) ? value : defaultValue;
    }


    /**
     * Returns a Collection view of the values contained in this map.
     *
     * @return the values contained in this map.
     */
    public Collection<V> valuesOnSoft() {
        return this.values().stream().map(SoftDataNode::get).collect(Collectors.toList());
    }

    /**
     * Returns a Set view of the mappings contained in this map.
     *
     * @return a Set view
     */
    public Set<Entry<K, V>> entrySetOnSoft() {
        Set<Entry<K, V>> entrySet = new HashSet<>();
        for (Map.Entry<K, SoftDataNode<K, V>> entry : this.entrySet()) {
            entrySet.add(new SoftEntrySetView<>(entry.getKey(), this));
        }
        return entrySet;
    }

    /**
     * Performs the given action for each entry in this map until all entries have been processed by action
     *
     * @param action an anonymous function which needs to consume each entry in this map
     */
    public void forEachOnSoft(BiConsumer<? super K, ? super V> action) {
        super.forEach((k, v) -> action.accept(k, v.get()));
    }

    private void clearInvalidDataNode(K key) {
        this.remove(key);
        nullValueOfNodeCount.increment();
        //If the threshold is exceeded, clear the data that has not been accessed for a long time but has been GC.
        if (nullValueOfNodeCount.sum() >= CLEAR_THRESHOLD) {
            try {
                for (int i = 0; i < this.size(); i++) {
                    SoftDataNode<K, V> poll = (SoftDataNode<K, V>) referenceQueue.poll();
                    if (Objects.isNull(poll)) {
                        break;
                    }
                    this.remove(poll.getIndex());
                }
            } catch (Throwable ex) {
                //pass
            }
            nullValueOfNodeCount.reset();
        }
    }

    /**
     * SoftDataNode
     *
     * @param <K> the type of key
     * @param <V> the type of value
     */
    public static class SoftDataNode<K, V> extends SoftReference<V> {
        /**
         * the index with which the specified value is to be associated
         */
        private final K index;

        /**
         * Constructor
         *
         * @param index node's index
         * @param data  node's value
         */
        public SoftDataNode(K index, V data) {
            super(data);
            this.index = index;
        }

        /**
         * Constructor
         *
         * @param index node's index
         * @param data  node's value
         * @param q     ReferenceQueue
         */
        public SoftDataNode(K index, V data, ReferenceQueue<? super V> q) {
            super(data, q);
            this.index = index;
        }

        /**
         * Get the index
         *
         * @return the index
         */
        public K getIndex() {
            return index;
        }
    }

    /**
     * SoftEntrySetView
     *
     * @param <K> the type of key
     * @param <V> the type of value
     */
    private static class SoftEntrySetView<K, V> extends AbstractMap.SimpleImmutableEntry<K, V> {
        private final SoftConcurrentHashMap<K, V> map;

        public SoftEntrySetView(K key, SoftConcurrentHashMap<K, V> map) {
            super(key, null);
            Objects.requireNonNull(key, "requires a non-null key");
            this.map = map;
        }

        @Override
        public K getKey() {
            return super.getKey();
        }

        @Override
        public V getValue() {
            return map.getOnSoft(getKey());
        }

        @Override
        public boolean equals(Object other) {
            Object oKey = (other instanceof SoftConcurrentHashMap.SoftEntrySetView) ? ((SoftEntrySetView<?, ?>) other).getKey() : String.valueOf(other);
            return getKey().equals(oKey);
        }

        @Override
        public int hashCode() {
            return getKey().hashCode();
        }
    }


}
