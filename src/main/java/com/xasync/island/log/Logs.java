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
package com.xasync.island.log;

import com.xasync.island.datastruct.SoftConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Logs
 *
 * @author xasync.com
 */
public class Logs {
    /**
     * Declares the default EnhanceLogger which will be used if missing the logger provide.
     */
    private final static EnhanceLogger DEFAULT_ENHANCE_LOGGER = new EnhanceLogger(
            LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME));
    /**
     * Pool is used to store a that has been requested to avoid creating it every time
     */
    private final static SoftConcurrentHashMap<String, EnhanceLogger> POOL = new SoftConcurrentHashMap<>();

    /**
     * Create a EnhanceLogger's instance basing on the logger you provide
     *
     * @param logger slf4j's logger
     * @return EnhanceLogger
     */
    public static EnhanceLogger on(Logger logger) {
        if (Objects.isNull(logger)) {
            return DEFAULT_ENHANCE_LOGGER;
        }
        EnhanceLogger eLog = POOL.getOnSoft(logger.getName());
        if (Objects.nonNull(eLog)) {
            return eLog;
        }
        eLog = new EnhanceLogger(logger);
        POOL.putAsSoftIfAbsent(logger.getName(), eLog);
        return eLog;
    }

    /**
     * Create a EnhanceLogger's instance basing on the logger's name you provide
     *
     * @param loggerName the logger name
     * @return EnhanceLogger
     */
    public static EnhanceLogger on(String loggerName) {
        String name;
        if (Objects.isNull(loggerName) || (name = loggerName.trim()).isEmpty()) {
            return DEFAULT_ENHANCE_LOGGER;
        }
        EnhanceLogger eLog = POOL.getOnSoft(name);
        if (Objects.nonNull(eLog)) {
            return eLog;
        }
        eLog = new EnhanceLogger(LoggerFactory.getLogger(name));
        POOL.putAsSoftIfAbsent(name, eLog);
        return eLog;
    }

    /**
     * Create a EnhanceLogger's instance basing on caller's metaclass you provide
     *
     * @param clazz Meta-Class of caller object
     * @return EnhanceLogger
     */
    public static EnhanceLogger on(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return DEFAULT_ENHANCE_LOGGER;
        }
        EnhanceLogger eLog = POOL.getOnSoft(clazz.getName());
        if (Objects.nonNull(eLog)) {
            return eLog;
        }
        eLog = new EnhanceLogger(LoggerFactory.getLogger(clazz));
        POOL.putAsSoftIfAbsent(clazz.getName(), eLog);
        return eLog;
    }


}
