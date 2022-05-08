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

import com.xasync.island.ifkiller.biz.bark.AnimalBarkAbility;
import com.xasync.island.ifkiller.biz.bark.BarkTypeEnum;
import com.xasync.island.test.BaseSpringTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * IfKillersTest
 *
 * @author xasync.com
 */
public class IfKillersTest extends BaseSpringTest {

    @Test
    public void testAnimalBark() {
        //meet
        String token1 = "cat";
        String voice1 = IfKillers.about(AnimalBarkAbility.class)
                .meet(token1)
                .start();
        System.out.println(voice1);
        Assert.assertEquals(BarkTypeEnum.CAT.getCode(), voice1);

        //no meet and active default
        String token2 = "goose";
        String voice2 = IfKillers.about(AnimalBarkAbility.class)
                .meet(token2)
                .start();
        System.out.println(voice2);
        /* the default implementation is DuckAnimalBarkAbility */
        Assert.assertEquals(BarkTypeEnum.DUCK.getCode(), voice2);
    }
}
