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

    private String status;

    private Integer value;

    UserAllowLogin(String status, Integer value) {
        this.status = status;
        this.value = value;
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
