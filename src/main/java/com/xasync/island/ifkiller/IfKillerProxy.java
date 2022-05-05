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
package com.xasync.island.ifkiller;

import com.xasync.island.ifkiller.annotation.IfKiller;
import com.xasync.island.spring.SpringContexts;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IfKillerProxy
 *
 * @author xasync.com
 */
public class IfKillerProxy<T> {
    private final Map<String, T> INSTANCE_POOL = new ConcurrentHashMap<>();
    private final Class<T> definition;

    private T defaultImplementInstance;

    private IfKillerProxy(Class<T> clazz) {
        this.definition = clazz;
    }

    public static <T> IfKillerProxy<T> create(Class<T> clazz) {
        return new IfKillerProxy<>(clazz);
    }


    public T meet(String identity) {
        String indexKey = Objects.nonNull(identity) ? identity.trim() : "";
        if (indexKey.isEmpty()) {
            return Optional.ofNullable(defaultImplementInstance)
                    .orElseThrow(() -> new RuntimeException(""));
        }
        T instance = INSTANCE_POOL.get(indexKey);
        if (Objects.nonNull(instance)) {
            return instance;
        }

        instance = findInstanceInApplicationContext(indexKey);
        if (Objects.nonNull(instance)) {
            INSTANCE_POOL.putIfAbsent(indexKey, instance);
            return instance;
        } else if (Objects.nonNull(defaultImplementInstance)) {
            return defaultImplementInstance;
        } else {
            throw new RuntimeException("");
        }
    }


    boolean belongTo(Class<?> clazz) {
        return Objects.nonNull(clazz) && Objects.equals(clazz, definition);
    }

    private T findInstanceInApplicationContext(String name) {
        if (Objects.isNull(name)) {
            return null;
        }
        try {
            Map<String, T> instanceMap = SpringContexts.getApplicationContext()
                    .getBeansOfType(definition);
            for (T instance : instanceMap.values()) {
                Class<?> instClazz = instance.getClass();
                IfKiller spec = instClazz.getAnnotation(IfKiller.class);
                if (Objects.isNull(spec)) {
                    continue;
                }
                boolean isFind = Arrays.stream(spec.meets()).allMatch(name::endsWith);
                if (isFind) {
                    return instance;
                }
                //
                if (spec.isDefault() && Objects.isNull(defaultImplementInstance)) {
                    defaultImplementInstance = instance;
                }
            }
            return null;
        } catch (Throwable ex) {
            return null;
        }
    }
}
