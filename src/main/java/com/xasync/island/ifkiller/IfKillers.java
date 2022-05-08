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

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IfKillers
 * <p>
 * Usages:
 * 1. You need to abstract your business requirements, then define the abilities and tokens to activate them.
 * 2. Write an implementation class based on your token and declare it by '@IfKiller'.
 * 3. Enjoy it like the follows:
 * <pre>
 *     // your abstract
 *     public interface Bark{
 *         void start();
 *     }
 *
 *     // dog
 *     &#64;IfKiller(meets={"dog"})
 *     public class DogBark{
 *         public void start(){
 *             System.out.println("dog");
 *         }
 *     }
 *     // cat
 *     &#64;IfKiller(meets={"cat"}, isDefault=true)
 *     public class CatBark{
 *         public void start(){
 *             System.out.println("cat");
 *         }
 *     }
 *
 *     // Invoke
 *     String token='cat'; //you can use '#' to join dimensions, likes: cat#3#male(animal + age + gender)
 *     IfKiller.about(Bark.class)
 *          .meet(token)
 *          .start();
 * </pre>
 *
 * @author xasync.com
 */
public final class IfKillers {

    /**
     * The registration pool, Structure:(The canonical name of your abstract, IfKillerProxy)
     */
    private final static Map<String, IfKillerProxy<?>> POOL = new ConcurrentHashMap<>();

    /**
     * The Main facade
     *
     * @param clazz The metaClass of your abstract
     * @param <T>   type
     * @return The execution result
     */
    @SuppressWarnings("unchecked")
    public static <T> IfKillerProxy<T> about(Class<T> clazz) {
        Objects.requireNonNull(clazz, "The metaClass is null");
        String registerKey = clazz.getCanonicalName();
        // Get priority from registration pool
        IfKillerProxy<?> proxy = POOL.get(registerKey);
        if (Objects.nonNull(proxy) && proxy.belongTo(clazz)) {
            return (IfKillerProxy<T>) proxy;
        }
        // create a new IfKillerProxy and return if not exists.
        IfKillerProxy<T> newProxy = IfKillerProxy.create(clazz);
        POOL.putIfAbsent(registerKey, newProxy);
        return newProxy;
    }
}
