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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.util.Objects;

/**
 * ILogger
 *
 * @author xasync.com
 */
public class EnhanceLogger implements Logger {
    /**
     * A logger instance
     */
    private final Logger log;

    public EnhanceLogger(Logger logger) {
        if (Objects.nonNull(logger)) {
            this.log = logger;
        } else {
            this.log = LoggerFactory.getLogger(this.getClass());
        }
    }

    private static String fillValues(String s, Object... objects) {
        if (Objects.isNull(s)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        int vIndex = 0;
        int len = s.length();
        while (index < len) {
            char c = s.charAt(index);
            if (c != '{' || index == len - 1) {
                sb.append(c);
                index += 1;
                continue;
            }
            //equals '{' and check if the next char is '}'
            char nc = s.charAt(index + 1);
            if (nc != '}') {
                sb.append(c);
                index += 1;
                continue;
            }
            Object v = vIndex < objects.length ? objects[vIndex] : null;
            sb.append(Objects.nonNull(v) ? v.toString() : "");
            vIndex += 1;
            index += 2;
        }
        return sb.toString();
    }

    public void trace(Throwable throwable, String s, Object... objects) {
        log.trace(fillValues(s, objects), throwable);
    }

    public void debug(Throwable throwable, String s, Object... objects) {
        log.debug(fillValues(s, objects), throwable);
    }

    public void info(Throwable throwable, String s, Object... objects) {
        log.info(fillValues(s, objects), throwable);
    }

    public void warn(Throwable throwable, String s, Object... objects) {
        log.warn(fillValues(s, objects), throwable);
    }

    public void error(Throwable throwable, String s, Object... objects) {
        log.error(fillValues(s, objects), throwable);
    }


    /*============================== implements from Logger(slf4j) =================================*/

    @Override
    public String getName() {
        return log.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public void trace(String s) {
        log.trace(s);
    }

    @Override
    public void trace(String s, Object o) {
        log.trace(s, o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        log.trace(s, o, o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        log.trace(s, objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        log.trace(s, throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return log.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String s) {
        log.trace(marker, s);
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        log.trace(marker, s, o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        log.trace(marker, s, o, o1);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        log.trace(marker, s, objects);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        log.trace(marker, s, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void debug(String s) {
        log.debug(s);
    }

    @Override
    public void debug(String s, Object o) {
        log.debug(s, o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        log.debug(s, o, o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        log.debug(s, objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        log.debug(s, throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return log.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String s) {
        log.debug(marker, s);
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        log.debug(marker, s, o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        log.debug(marker, s, o, o1);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        log.debug(marker, s, objects);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        log.debug(marker, s, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(String s) {
        log.info(s);
    }

    @Override
    public void info(String s, Object o) {
        log.info(s, o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        log.info(s, o, o1);
    }

    @Override
    public void info(String s, Object... objects) {
        log.info(s, objects);
    }

    @Override
    public void info(String s, Throwable throwable) {
        log.info(s, throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return log.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String s) {
        log.info(marker, s);
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        log.info(marker, s, o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        log.info(marker, s, o, o1);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        log.info(marker, s, objects);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        log.info(marker, s, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(String s) {
        log.warn(s);
    }

    @Override
    public void warn(String s, Object o) {
        log.warn(s, o);
    }

    @Override
    public void warn(String s, Object... objects) {
        log.warn(s, objects);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        log.warn(s, o, o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        log.warn(s, throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return log.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String s) {
        log.warn(marker, s);
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        log.warn(marker, s, o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        log.warn(marker, s, o, o1);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        log.warn(marker, s, objects);
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        log.warn(marker, s, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public void error(String s) {
        log.error(s);
    }

    @Override
    public void error(String s, Object o) {
        log.error(s, o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        log.error(s, o, o1);
    }

    @Override
    public void error(String s, Object... objects) {
        log.error(s, objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        log.error(s, throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return log.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String s) {
        log.error(marker, s);
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        log.error(marker, s, o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        log.error(marker, s, o, o1);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        log.error(marker, s, objects);
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        log.error(marker, s, throwable);
    }
}
