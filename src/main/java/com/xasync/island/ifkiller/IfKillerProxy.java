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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IfKillerProxy is used to manage the implementation instance of your abstraction. It can find specific instance
 * according to the token you input.
 *
 * @author xasync.com
 */
public class IfKillerProxy<T> {
    private final static Logger LOG = LoggerFactory.getLogger(IfKillerProxy.class);

    /**
     * The instance registration pool, Structure:(token,instance)
     */
    private final Map<String, T> INSTANCE_POOL = new ConcurrentHashMap<>();

    /**
     * The meatClass about your abstraction
     */
    private final Class<T> definition;

    /**
     * The default implement for your abstraction, it will be return when it can't be found
     */
    private T defaultImplementInstance;

    /**
     * Private constructor, you can use {@code create } to instead of it
     *
     * @param clazz the metaClass
     */
    private IfKillerProxy(Class<T> clazz) {
        this.definition = clazz;
    }

    /**
     * Create a proxy for managing the implementation instance of your abstraction.
     *
     * @param clazz the metaClass
     * @param <T>   type
     * @return IfKillerProxy
     */
    public static <T> IfKillerProxy<T> create(Class<T> clazz) {
        return new IfKillerProxy<>(clazz);
    }

    /**
     * Find the implementation instance according to the token you input
     *
     * @param token a token that you declare it by '@IfKiller'
     * @return a implementation instance
     */
    public T meet(String token) {
        String registerKey = Objects.nonNull(token) ? token.trim() : "";
        if (registerKey.isEmpty()) {
            return Optional.ofNullable(defaultImplementInstance)
                    .orElseThrow(() -> new RuntimeException("The token you input is blank"));
        }
        // Get priority from registration pool
        T instance = INSTANCE_POOL.get(registerKey);
        if (Objects.nonNull(instance)) {
            return instance;
        }
        // Find the instance in spring context because of not exists.
        instance = findInstanceInApplicationContext(registerKey);
        if (Objects.nonNull(instance)) {
            //success: register it into pool and return
            INSTANCE_POOL.putIfAbsent(registerKey, instance);
            return instance;
        } else if (Objects.nonNull(defaultImplementInstance)) {
            //default: return the default instance when it is not null
            return defaultImplementInstance;
        } else {
            //fail and no default: throw exception
            String msg = "there is no implementation instance about '" + registerKey
                    + "' and you use '@IfKiller(meets={\"" + registerKey + "\"})' to declare it";
            throw new RuntimeException(msg);
        }
    }

    /**
     * Check if the current proxy is belongs to the metaClass
     *
     * @param clazz the metaClass
     * @return true or false
     */
    boolean belongTo(Class<?> clazz) {
        return Objects.nonNull(clazz) && Objects.equals(clazz, definition);
    }

    /**
     * Find the instance of the current metaClass according to the token
     *
     * @param token implementation
     * @return a implementation instance
     */
    private T findInstanceInApplicationContext(String token) {
        if (Objects.isNull(token)) {
            return null;
        }
        ApplicationContext applicationContext = SpringContexts.getApplicationContext();
        if (Objects.isNull(applicationContext)) {
            throw new RuntimeException("IfKillers depends on SpringContexts, so that you must invokes " +
                    "'SpringContexts.init' for finishing to initialize it before use.");
        }
        // get instances by the metaClass of your abstraction
        Map<String, T> instanceMap = applicationContext.getBeansOfType(definition);
        for (T instance : instanceMap.values()) {
            Class<?> instClazz = instance.getClass();
            IfKiller spec = instClazz.getAnnotation(IfKiller.class);
            if (Objects.isNull(spec)) {
                LOG.warn("'{}' miss the '@IfKiller' annotation", instClazz.getCanonicalName());
                continue;
            }
            //match the token
            boolean isFind = Arrays.stream(spec.meets())
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .allMatch(token::equals);
            //return the current instance if finding
            if (isFind) {
                return instance;
            }
            //By the way, find the default implementation
            if (Objects.isNull(defaultImplementInstance) && spec.isDefault()) {
                defaultImplementInstance = instance;
            }
        }
        // return null if not find
        return null;
    }
}
