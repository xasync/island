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
package com.xasync.island.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;

/**
 * SpringContexts
 *
 * @author xasync.com
 */
public class SpringContexts {
    private final static Logger LOG = LoggerFactory.getLogger(SpringContexts.class);
    private final static AtomicBoolean INIT_FLAG = new AtomicBoolean(false);
    private static ApplicationContext APPLICATION_CONTEXT = null;

    /**
     * This is a static method for initializing SpringContexts
     *
     * @param applicationContext the application context of spring
     */
    public static void init(ApplicationContext applicationContext) {
        if (Objects.isNull(applicationContext)) {
            LOG.warn("Abort to initialize SpringContexts because of receiving a null ApplicationContext");
            return;
        }
        synchronized (INIT_FLAG) {
            if (INIT_FLAG.get() && Objects.nonNull(APPLICATION_CONTEXT)) {
                return;
            }
            APPLICATION_CONTEXT = applicationContext;
            INIT_FLAG.set(true);
            LOG.info("Success to initialize SpringContexts. (: Power by island!");
        }
    }

    /**
     * Get the current application context of spring
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    /**
     * Get an instance by the metaClass, it equals to '@Autowired'
     *
     * @param metaClass meta class
     * @param <T>       type
     * @return instance
     */
    public static <T> T autowire(Class<T> metaClass) {
        checkInit();
        Object instance = APPLICATION_CONTEXT.getAutowireCapableBeanFactory()
                .autowire(metaClass, AUTOWIRE_BY_TYPE, false);
        return metaClass.cast(instance);
    }

    /**
     * Extract the value of placeholder, it equals to '@Value'
     *
     * @param text placeholder
     * @return value
     */
    public static String value(String text) {
        checkInit();
        ConfigurableEnvironment cEnv = (ConfigurableEnvironment) APPLICATION_CONTEXT.getEnvironment();
        return cEnv.resolvePlaceholders(text);
    }


    private static void checkInit() {
        if (Objects.isNull(APPLICATION_CONTEXT)) {
            String reason = INIT_FLAG.get() ? "initialize successfully(null-value)" : "invoke the init method before use";
            throw new RuntimeException("SpringContexts don't " + reason);
        }
    }
}
