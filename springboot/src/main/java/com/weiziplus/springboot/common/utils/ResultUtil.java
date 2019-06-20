package com.weiziplus.springboot.common.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果
 *
 * @author wanglongwei
 * @data 2019/5/24 15:58
 */
@Data
public class ResultUtil implements Serializable {

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
     * 成功状态的code
     */
    private static final Integer SUCCESS_CODE = 200;

    /**
     * 失败状态的code
     */
    private static final Integer ERROR_CODE = 403;

    /**
     * token出错的code
     */
    private static final Integer ERROR_TOKEN_CODE = 401;

    /**
     * 基础返回处理
     *
     * @param code
     * @param msg
     * @param data
     * @return
     */
    private static ResultUtil content(Integer code, String msg, Object data) {
        ResultUtil resultUtil = new ResultUtil();
        resultUtil.setCode(code);
        resultUtil.setMsg(msg);
        resultUtil.setData(data);
        return resultUtil;
    }

    public static ResultUtil success(String msg, Object data) {
        return content(SUCCESS_CODE, msg, data);
    }

    public static ResultUtil success(Object data) {
        return content(SUCCESS_CODE, "success", data);
    }

    public static ResultUtil success(String msg) {
        return content(SUCCESS_CODE, msg, null);
    }

    public static ResultUtil success() {
        return content(SUCCESS_CODE, "success", null);
    }

    public static ResultUtil error(Integer code, String msg, Object data) {
        return content(code, msg, data);
    }

    public static ResultUtil error(Integer code, String msg) {
        return content(code, msg, null);
    }

    public static ResultUtil error(Integer code, Object data) {
        return content(code, "error", data);
    }

    public static ResultUtil error(Integer code) {
        return content(code, "error", null);
    }

    public static ResultUtil error(String msg, Object data) {
        return content(ERROR_CODE, msg, data);
    }

    public static ResultUtil error(String msg) {
        return content(ERROR_CODE, msg, null);
    }

    public static ResultUtil error() {
        return content(ERROR_CODE, "error", null);
    }

    public static ResultUtil errorToken(String msg, Object data) {
        return error(ERROR_TOKEN_CODE, msg, data);
    }

    public static ResultUtil errorToken(String msg) {
        return error(ERROR_TOKEN_CODE, msg, null);
    }

    public static ResultUtil errorToken() {
        return error(ERROR_TOKEN_CODE, "token失效", null);
    }
}
