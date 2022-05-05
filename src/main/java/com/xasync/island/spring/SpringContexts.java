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

import org.springframework.context.ApplicationContext;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SpringContexts
 *
 * @author xasync.com
 */
public class SpringContexts {
    private final static AtomicBoolean INIT_FLAG = new AtomicBoolean(false);
    private static ApplicationContext APPLICATION_CONTEXT = null;

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    public static void init(ApplicationContext applicationContext) {
        if (Objects.isNull(applicationContext)) {
            System.out.println("Abort to initialize SpringContexts because of receiving a null ApplicationContext");
            return;
        }
        synchronized (INIT_FLAG) {
            if (INIT_FLAG.get()) {
                return;
            }
            APPLICATION_CONTEXT = applicationContext;
            INIT_FLAG.set(true);
        }
    }
}
