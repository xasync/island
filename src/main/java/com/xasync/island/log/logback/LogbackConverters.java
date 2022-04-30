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

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.ClassicConverter;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * LogbackConverters
 *
 * @author xasync.com
 */
public class LogbackConverters {
    public static final String LOGBACK_CONVERTER_SUFFIX = "Converter";

    public static void register(Class<? extends ClassicConverter> converter, String shortName) {
        register(converter, shortName, null);
    }

    public static void register(Class<? extends ClassicConverter> converter, String shortName, String longName) {
        Objects.requireNonNull(converter);
        ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
        boolean provideByLogback = (loggerFactory instanceof LoggerContext);
        if (!provideByLogback) {
            System.out.println("Abort to register '" + converter.getName()
                    + "' because the current LoggerFactory is not provide by logback");
            return;
        }
        String lName = Objects.nonNull(longName) ? longName.trim() : "";
        if (lName.isEmpty()) {
            lName = extractLongNameOfConverter(converter);
        }
        //register the long name
        if (PatternLayout.DEFAULT_CONVERTER_MAP.containsKey(lName)) {
            throw new RuntimeException(String.format("duplicated converter's longName: %s", lName));
        }
        PatternLayout.DEFAULT_CONVERTER_MAP.put(lName, converter.getName());

        //register the converter
        if (PatternLayout.CONVERTER_CLASS_TO_KEY_MAP.containsKey(converter.getName())) {
            //rollback
            PatternLayout.DEFAULT_CONVERTER_MAP.remove(lName, converter.getName());
            throw new RuntimeException(String.format("'%s' is already registered", converter.getName()));
        }
        //register the short name when shortName is not blank.
        String sName = Objects.nonNull(shortName) ? shortName.trim() : "";
        if (!sName.isEmpty()) {
            PatternLayout.DEFAULT_CONVERTER_MAP.put(sName, converter.getName());
        }
    }

    public static String extractLongNameOfConverter(Class<? extends ClassicConverter> converter) {
        Objects.requireNonNull(converter);
        String simpleName = converter.getSimpleName();
        //direct to return simpleName if it misses the converter's suffix
        if (!simpleName.endsWith(LOGBACK_CONVERTER_SUFFIX)) {
            return simpleName;
        }
        StringBuilder sb = new StringBuilder();
        //include the first char
        sb.append(simpleName.charAt(0));
        for (int index = 1; index < simpleName.length(); index++) {
            char c = simpleName.charAt(index);
            //quit to find if it meets an upper case char.
            if (Character.isUpperCase(c)) {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString().toLowerCase();
    }
}
