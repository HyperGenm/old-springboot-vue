package com.weiziplus.springboot.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装result返回
 *
 * @author wanglongwei
 * @data 2019/4/22 9:35
 */
public class ResponseBean {

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

    public static Map<String, Object> content(Integer code, String msg, Object data) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }

    public static Map<String, Object> success(String msg, Object data) {
        return content(SUCCESS_CODE, msg, data);
    }

    public static Map<String, Object> success(Object data) {
        return content(SUCCESS_CODE, "success", data);
    }

    public static Map<String, Object> success(String msg) {
        return content(SUCCESS_CODE, msg, null);
    }

    public static Map<String, Object> success() {
        return content(SUCCESS_CODE, "success", null);
    }

    public static Map<String, Object> error(Integer code, String msg, Object data) {
        return content(code, msg, data);
    }

    public static Map<String, Object> error(Integer code, String msg) {
        return content(code, msg, null);
    }

    public static Map<String, Object> error(Integer code, Object data) {
        return content(code, "error", data);
    }

    public static Map<String, Object> error(Integer code) {
        return content(code, "error", null);
    }

    public static Map<String, Object> error(String msg, Object data) {
        return content(ERROR_CODE, msg, data);
    }

    public static Map<String, Object> error(String msg) {
        return content(ERROR_CODE, msg, null);
    }

    public static Map<String, Object> error() {
        return content(ERROR_CODE, "error", null);
    }

    public static Map<String, Object> errorToken(String msg, Object data) {
        return error(ERROR_TOKEN_CODE, msg, data);
    }

    public static Map<String, Object> errorToken(String msg) {
        return error(ERROR_TOKEN_CODE, msg, null);
    }

    public static Map<String, Object> errorToken() {
        return error(ERROR_TOKEN_CODE, "token失效", null);
    }
}
