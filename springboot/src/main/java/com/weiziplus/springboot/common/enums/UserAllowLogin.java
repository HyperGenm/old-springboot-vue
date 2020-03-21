package com.weiziplus.springboot.common.enums;

import lombok.Getter;

/**
 * 用户允许登录
 *
 * @author wanglongwei
 * @date 2020/03/12 13/50
 */
@Getter
public enum UserAllowLogin {

    /**
     * 允许/禁止
     */
    ALLOW("允许", 0),
    FORBID("禁止", 1);

    private String name;

    private Integer value;

    UserAllowLogin(String name, Integer value) {
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
        for (UserAllowLogin userAllowLogin : UserAllowLogin.values()) {
            if (userAllowLogin.getValue().equals(value)) {
                return userAllowLogin.getName();
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
        for (UserAllowLogin value : UserAllowLogin.values()) {
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
        for (UserAllowLogin allowLogin : UserAllowLogin.values()) {
            if (allowLogin.value.equals(value)) {
                return true;
            }
        }
        return false;
    }


}
