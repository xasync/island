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

import ch.qos.logback.classic.pattern.Abbreviator;
import ch.qos.logback.classic.pattern.ClassNameOnlyAbbreviator;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.pattern.TargetLengthBasedClassNameAbbreviator;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.List;
import java.util.Objects;

/**
 * FocusClassOfCallerConverter enhances the function of ClassOfCallerConverter
 * Usages:
 * Step1: Add it into your logback
 * <code>
 * &lt;include resource="island-logback-converter.xml"/&gt;
 * </code>
 * Step2: Use it in your pattern
 * <code>
 * "%date{ISO8601} %level %thread %fc{15} %msg%n" // equals to %fc{15,0} or %c or %class
 * "%date{ISO8601} %level %thread %focusClass{15} %msg%n"
 * "%date{ISO8601} %level %thread %fc{15,1} %msg%n"
 * </code>
 *
 * @author xasync.com
 */
public class FocusClassOfCallerConverter extends ClassicConverter {

    /**
     * The short name about registering itself into logback
     */
    public final static String SHORT_NAME = "fc";


    /**
     * The long name about registering itself into logback
     */
    public final static String LONG_NAME = "focusClass";

    /**
     * By default, the length of the class name allowed to be output
     */
    private final static int DEFAULT_PRINT_MAX_LEN = 25;

    /**
     * Provide the ability about shortening the class name of caller focused
     */
    private Abbreviator abbreviator;

    /**
     * The index of the caller of focus in the stack
     */
    private Integer indexOfStack;

    @Override
    public String convert(ILoggingEvent event) {
        String fqn = this.getFullyQualifiedName(event);
        return this.abbreviator == null ? fqn : this.abbreviator.abbreviate(fqn);
    }

    @Override
    public void start() {
        List<String> optionList = this.getOptionList();
        if (Objects.isNull(optionList) || optionList.isEmpty()) {
            this.abbreviator = new ClassNameOnlyAbbreviator();
            return;
        }
        //extract and config the max print length from the first option.
        int printMaxLen;
        try {
            String printMaxLenOptStr = optionList.get(0);
            printMaxLen = Integer.parseInt(printMaxLenOptStr);
            if (printMaxLen <= 0) {
                printMaxLen = DEFAULT_PRINT_MAX_LEN;
            }
        } catch (Throwable ex) {
            printMaxLen = DEFAULT_PRINT_MAX_LEN;
            ex.printStackTrace();
        }
        this.abbreviator = new TargetLengthBasedClassNameAbbreviator(printMaxLen);

        //extract the invoker's index in stack focused.
        try {
            boolean noSecondOption = optionList.size() < 2;
            if (noSecondOption) {
                this.indexOfStack = 0;
                return;
            }
            String indexOfStackOptStr = optionList.get(1);
            int optVal = Integer.parseInt(indexOfStackOptStr);
            this.indexOfStack = Math.max(optVal, 0);
        } catch (Throwable ex) {
            this.indexOfStack = 0;
            ex.printStackTrace();
        }
    }

    /**
     * Obtain the class name of caller which you focus
     *
     * @param event the log event of logback
     * @return the class name
     */
    protected String getFullyQualifiedName(ILoggingEvent event) {
        try {
            StackTraceElement[] cda = event.getCallerData();
            if (Objects.isNull(cda)) {
                return "?";
            }
            StackTraceElement stackTopElement = null;
            for (int index = 0; index < cda.length; index++) {
                if (index == indexOfStack) {
                    stackTopElement = cda[index];
                    break;
                }
            }
            if (Objects.isNull(stackTopElement)) {
                stackTopElement = cda[0];
            }
            return stackTopElement.getClassName();
        } catch (Throwable ex) {
            return "?";
        }
    }
}
