package com.weiziplus.springboot.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * 统一返回结果
 *
 * @author wanglongwei
 * @date 2019/5/24 15:58
 */
@Getter
@ApiModel("统一返回结果")
public class ResultUtils<T> implements Serializable {

    /**
     * 返回状态码code
     */
    @ApiModelProperty("状态码:200表示成功")
    private Integer code;

    /**
     * 返回提示信息
     */
    @ApiModelProperty("提示信息")
    private String msg;

    /**
     * 返回数据
     */
    @ApiModelProperty("数据")
    private T data;

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
    private ResultUtils(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功
     */
    private static final Integer SUCCESS_CODE = 200;

    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static <T> ResultUtils<T> success(T data) {
        ResultUtils<T> resultUtils = new ResultUtils<>();
        resultUtils.code = SUCCESS_CODE;
        resultUtils.msg = "success";
        resultUtils.data = data;
        return resultUtils;
    }

    /**
     * 成功
     *
     * @return
     */
    public static <T> ResultUtils<T> success() {
        return success(null);
    }

    /**
     * token出错
     */
    private static final Integer ERROR_TOKEN_CODE = 401;

    /**
     * 失败
     */
    private static final Integer ERROR_CODE = 402;

    /**
     * 没有权限，拒绝访问
     */
    private static final Integer ERROR_ROLE_CODE = 403;

    /**
     * 封号
     */
    private static final Integer ERROR_SUSPEND_CODE = 404;

    /**
     * 系统出现异常
     */
    private static final Integer ERROR_EXCEPTION = 500;

    /**
     * 异常
     *
     * @param code
     * @param msg
     * @return
     */
    private static <T> ResultUtils<T> baseError(Integer code, String msg) {
        return new ResultUtils<>(code, msg, null);
    }

    /**
     * token异常
     *
     * @param msg
     * @return
     */
    public static <T> ResultUtils<T> errorToken(String msg) {
        return baseError(ERROR_TOKEN_CODE, msg);
    }

    /**
     * 失败
     *
     * @param msg
     * @return
     */
    public static <T> ResultUtils<T> error(String msg) {
        return baseError(ERROR_CODE, msg);
    }

    /**
     * 没有权限
     *
     * @param msg
     * @return
     */
    public static <T> ResultUtils<T> errorRole(String msg) {
        return baseError(ERROR_ROLE_CODE, msg);
    }

    /**
     * 违规操作，封号
     *
     * @return
     */
    public static <T> ResultUtils<T> errorSuspend() {
        return baseError(ERROR_SUSPEND_CODE, "Rider Kick");
    }

    /**
     * 系统异常
     *
     * @param msg
     * @return
     */
    public static <T> ResultUtils<T> errorException(String msg) {
        return baseError(ERROR_EXCEPTION, msg);
    }

    private static final long serialVersionUID = 1L;
}
