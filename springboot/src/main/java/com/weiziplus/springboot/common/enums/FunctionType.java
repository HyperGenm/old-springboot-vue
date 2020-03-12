package com.weiziplus.springboot.common.enums;

import lombok.Getter;

/**
 * 功能类型
 *
 * @author wanglongwei
 * @date 2020/03/12 13/35
 */
@Getter
public enum FunctionType {

    /**
     * 菜单/按钮
     */
    MENU("菜单", 0),
    BUTTON("按钮", 1);

    private String name;

    private Integer value;

    FunctionType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 判断是否存在某个值
     *
     * @param value
     * @return
     */
    public static boolean contains(Integer value) {
        for (FunctionType functionType : FunctionType.values()) {
            if (functionType.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

}
