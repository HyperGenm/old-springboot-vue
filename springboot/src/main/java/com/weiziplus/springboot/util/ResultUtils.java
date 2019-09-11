package com.weiziplus.springboot.util;

import lombok.Getter;

import java.io.Serializable;

/**
 * 统一返回结果
 *
 * @author wanglongwei
 * @data 2019/5/24 15:58
 */
@Getter
public class ResultUtils implements Serializable {

    /**
     * 返回状态码code
     */
    private Integer code;

    /**
     * 返回提示信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 创建无参数ResultUtil对象---fastjson反序列化需要无参数
     */
    public ResultUtils() {
    }

    /**
     * 创建ResultUtil对象
     *
     * @param code
     * @param msg
     * @param data
     */
    private ResultUtils(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 基础返回处理
     *
     * @param code
     * @param msg
     * @param data
     * @return
     */
    private static ResultUtils content(Integer code, String msg, Object data) {
        return new ResultUtils(code, msg, data);
    }

    /**
     * 成功状态的code
     */
    private static final Integer SUCCESS_CODE = 200;

    /**
     * 失败状态的code
     */
    private static final Integer ERROR_CODE = 403;

    /**
     * 封号的code
     */
    private static final Integer ERROR_SUSPEND_CODE = 404;

    /**
     * token出错的code
     */
    private static final Integer ERROR_TOKEN_CODE = 401;

    /**
     * admin用户没有权限的code
     */
    private static final Integer ERROR_ROLE_CODE = 402;

    public static ResultUtils success(String msg, Object data) {
        return content(SUCCESS_CODE, msg, data);
    }

    public static ResultUtils success(Object data) {
        return content(SUCCESS_CODE, "success", data);
    }

    public static ResultUtils success(String msg) {
        return content(SUCCESS_CODE, msg, null);
    }

    public static ResultUtils success() {
        return content(SUCCESS_CODE, "success", null);
    }

    public static ResultUtils error(String msg, Object data) {
        return content(ERROR_CODE, msg, data);
    }

    public static ResultUtils error(String msg) {
        return content(ERROR_CODE, msg, null);
    }

    public static ResultUtils error() {
        return content(ERROR_CODE, "error", null);
    }

    public static ResultUtils errorSuspend() {
        return content(ERROR_SUSPEND_CODE, "Rider Kick", null);
    }

    public static ResultUtils errorToken(String msg) {
        return content(ERROR_TOKEN_CODE, msg, null);
    }

    public static ResultUtils errorRole(String msg) {
        return content(ERROR_ROLE_CODE, msg, null);
    }
}
