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

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IfKillers
 *
 * @author xasync.com
 */
public final class IfKillers {

    private final static Map<String, IfKillerProxy<?>> POOL = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> IfKillerProxy<T> about(Class<T> clazz) {
        Objects.requireNonNull(clazz, "");
        IfKillerProxy<?> proxy = POOL.get(clazz.getCanonicalName());
        if (Objects.nonNull(proxy) && proxy.belongTo(clazz)) {
            return (IfKillerProxy<T>) proxy;
        }
        IfKillerProxy<T> newProxy = IfKillerProxy.create(clazz);
        POOL.putIfAbsent(clazz.getCanonicalName(), newProxy);
        return newProxy;
    }
}
