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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LogsTest
 *
 * @author xasync.com
 */
public class LogsTest {
    private final static Logger logger = LoggerFactory.getLogger(LogsTest.class);

    @Test
    public void testError() {
        Logs.on(logger).error(new RuntimeException("test"), "i love {}, and you love {}", "romeo", "juilar");

        Logs.on(logger).warn(new RuntimeException("test"), "i love {}, and you love {}", "romeo", "juilar");

        Logs.on(logger).info(new RuntimeException("test"), "i love {}, and you love {}", "romeo", "juilar");

        Logs.on(LogsTest.class).error(new RuntimeException("test"), "i love {}, and you love {}", "romeo", "juilar");

        Logs.on("ROOT").error(new RuntimeException("test"), "i love {}, and you love {}", "romeo", "juilar");
    }
}