package com.weiziplus.springboot.common.utils;

import java.io.Serializable;

/**
 * 统一返回结果
 *
 * @author wanglongwei
 * @data 2019/5/24 15:58
 */
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
     * 获取状态码
     *
     * @return
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取提示信息
     *
     * @return
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 获取返回数据
     *
     * @return
     */
    public Object getData() {
        return data;
    }

    /**
     * 创建无参数ResultUtil对象---fastjson反序列化需要无参数
     */
    public ResultUtil() {
    }

    /**
     * 创建ResultUtil对象
     *
     * @param code
     * @param msg
     * @param data
     */
    private ResultUtil(Integer code, String msg, Object data) {
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
    private static ResultUtil content(Integer code, String msg, Object data) {
        return new ResultUtil(code, msg, data);
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
     * token出错的code
     */
    private static final Integer ERROR_TOKEN_CODE = 401;

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
        return content(ERROR_TOKEN_CODE, msg, data);
    }

    public static ResultUtil errorToken(String msg) {
        return content(ERROR_TOKEN_CODE, msg, null);
    }

    public static ResultUtil errorToken() {
        return content(ERROR_TOKEN_CODE, "token失效", null);
    }
}
