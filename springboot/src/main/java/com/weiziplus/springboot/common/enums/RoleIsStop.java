package com.weiziplus.springboot.common.enums;

import lombok.Getter;

/**
 * 角色是否停用
 *
 * @author wanglongwei
 * @date 2020/03/12 13/40
 */
@Getter
public enum RoleIsStop {

    /**
     * 启用/禁用
     */
    ENABLE("启用", 0),
    DISABLE("禁用", 1);

    private String name;

    private Integer value;

    RoleIsStop(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 根据value获取name
     *
     * @param value
     * @return
     */
    public static String getNameByValue(Integer value) {
        for (RoleIsStop roleIsStop : RoleIsStop.values()) {
            if (roleIsStop.getValue().equals(value)) {
                return roleIsStop.getName();
            }
        }
        return null;
    }

    /**
     * 根据name获取value
     *
     * @param name
     * @return
     */
    public static Integer getValueByName(String name) {
        for (RoleIsStop value : RoleIsStop.values()) {
            if (value.getName().equals(name)) {
                return value.getValue();
            }
        }
        return null;
    }

    /**
     * 判断是否存在某个值
     *
     * @param value
     * @return
     */
    public static boolean contains(Integer value) {
        for (RoleIsStop roleIsStop : RoleIsStop.values()) {
            if (roleIsStop.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

}
