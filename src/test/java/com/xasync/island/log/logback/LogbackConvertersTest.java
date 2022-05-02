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
package com.xasync.island.log.logback;

import ch.qos.logback.classic.pattern.ClassOfCallerConverter;
import ch.qos.logback.classic.pattern.DateConverter;
import org.junit.Assert;
import org.junit.Test;

/**
 * LogbackConvertersTest
 *
 * @author xasync.com
 */
public class LogbackConvertersTest {

    @Test
    public void testExtractLongNameOfConverter() {
        Assert.assertEquals("class",
                LogbackConverters.extractLongNameOfConverter(ClassOfCallerConverter.class));
        Assert.assertEquals("date",
                LogbackConverters.extractLongNameOfConverter(DateConverter.class));
    }
}
