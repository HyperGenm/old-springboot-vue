package com.weiziplus.springboot.common.base;

import lombok.Getter;

/**
 * where条件枚举
 *
 * @author wanglongwei
 * @date 2020/07/28 17/46
 */
@Getter
public enum BaseWhereEnum {
    /**
     * sql条件
     */
    EQUAL("等于", "="),
    NOT_EQUAL("不等于", "<>"),
    MORE_THAN("大于", ">"),
    LESS_THAN("小于", "<"),
    MORE_THAN_EQUAL("大于等于", ">="),
    LESS_THAN_EQUAL("小于等于", "<="),
    IN("IN", "IN"),
    NOT_IN("NOT_IN", "NOT IN"),
    POSITION("POSITION", "POSITION");

    private String name;
    private String value;

    BaseWhereEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 是否存在
     *
     * @param value
     * @return
     */
    public static boolean contains(String value) {
        for (BaseWhereEnum where : BaseWhereEnum.values()) {
            if (where.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
