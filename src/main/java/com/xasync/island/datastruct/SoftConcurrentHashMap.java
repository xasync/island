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
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * SoftConcuHashMap
 *
 * @author xasync.com
 */
public class SoftConcurrentHashMap<K, V> extends ConcurrentHashMap<K, SoftConcurrentHashMap.SoftDataNode<K, V>> {
    private final ReferenceQueue<V> referenceQueue = new ReferenceQueue<>();

    public SoftConcurrentHashMap() {
        super();
    }

    public SoftConcurrentHashMap(int initialCapacity) {
        super(initialCapacity);
    }


    public SoftConcurrentHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }


    public SoftConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        super(initialCapacity, loadFactor, concurrencyLevel);
    }

    public void putAsSoft(K key, V value) {
        SoftDataNode<K, V> sdn = new SoftDataNode<>(key, value, referenceQueue);
        this.put(key, sdn);
    }

    public void putAllAsSoft(Map<K, V> map) {
        if (Objects.isNull(map) || map.isEmpty()) {
            return;
        }
        map.forEach(this::putAsSoft);
    }

    public V getOnSoft(K key) {
        SoftDataNode<K, V> sdn = this.get(key);
        if (Objects.isNull(sdn)) {
            return null;
        }
        V value = sdn.get();
        if (Objects.isNull(value)) {
            this.remove(key);
            return null;
        }
        return value;
    }

    public V getOnSoftOrDefault(K key, V defaultValue) {
        V value = getOnSoft(key);
        return Objects.nonNull(value) ? value : defaultValue;
    }

    public void putAsSoftIfAbsent(K key, V value) {
        if (!this.containsKey(key)) {
            putAsSoft(key, value);
        }
    }

    public Collection<V> valuesOnSoft() {
        return this.values().stream().map(SoftDataNode::get).collect(Collectors.toList());
    }

    public Set<Entry<K, V>> entrySetOnSoft() {
        Set<Entry<K, V>> entrySet = new HashSet<>();
        for (Map.Entry<K, SoftDataNode<K, V>> entry : this.entrySet()) {
            entrySet.add(new SoftMapEntry<>(entry.getKey(), this));
        }
        return entrySet;
    }

    public void forEachOnSoft(BiConsumer<? super K, ? super V> action) {
        super.forEach((k, v) -> action.accept(k, v.get()));
    }

    private static class SoftMapEntry<K, V> extends AbstractMap.SimpleImmutableEntry<K, V> {
        private final SoftConcurrentHashMap<K, V> map;

        public SoftMapEntry(K key, SoftConcurrentHashMap<K, V> map) {
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
            Object oKey = (other instanceof SoftMapEntry) ? ((SoftMapEntry<?, ?>) other).getKey() : String.valueOf(other);
            return getKey().equals(oKey);
        }

        @Override
        public int hashCode() {
            return getKey().hashCode();
        }
    }

    public static class SoftDataNode<K, V> extends SoftReference<V> {
        private final K index;

        public SoftDataNode(K index, V data) {
            super(data);
            this.index = index;
        }

        public SoftDataNode(K index, V data, ReferenceQueue<? super V> q) {
            super(data, q);
            this.index = index;
        }

        public K getIndex() {
            return index;
        }
    }
}
