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
package com.xasync.island.ifkiller.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * IfKiller is used to declare that the current implementation class needs to be scanned, and you don't add '@Component'
 * repeatedly('@IfKiller' equals '@Component'). Of course, it is very important that you need to define some
 * tokens you expect to meet by setting the meets field.
 *
 * @author xasync.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface IfKiller {

    /**
     * Some tokens you expect to meet. if system meets your token, it will let the class to handles.
     *
     * @return tokens
     */
    String[] meets();

    /**
     * Whether is the current class default, defaults false.
     *
     * @return true or false
     */
    boolean isDefault() default false;
}
