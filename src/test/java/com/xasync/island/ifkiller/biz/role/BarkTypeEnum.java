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
package com.xasync.island.ifkiller.biz.role;

/**
 * RoleTypeEnum
 *
 * @author xasync.com
 */
public enum BarkTypeEnum {
    DOG(BarkTypeEnum.DOG_CODE, "animal-dog"),
    CAT(BarkTypeEnum.CAT_CODE, "animal-cat"),
    DUCK(BarkTypeEnum.DUCK_CODE, "animal-duck"),
    TIGER(BarkTypeEnum.TIGER_CODE, "animal-tiger");

    public final static String DOG_CODE = "dog";
    public final static String CAT_CODE = "cat";
    public final static String TIGER_CODE = "tiger";
    public final static String DUCK_CODE = "duck";

    private final String code;
    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    BarkTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
