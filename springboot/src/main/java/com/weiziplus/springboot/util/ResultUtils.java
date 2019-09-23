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
@ApiModel("返回说明")
public class ResultUtils implements Serializable {

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
     * 成功
     */
    private static final Integer SUCCESS_CODE = 200;

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
     * 成功
     *
     * @param data
     * @return
     */
    public static ResultUtils success(Object data) {
        return content(SUCCESS_CODE, "success", data);
    }

    /**
     * 成功
     *
     * @return
     */
    public static ResultUtils success() {
        return content(SUCCESS_CODE, "success", null);
    }

    /**
     * token异常
     *
     * @param msg
     * @return
     */
    public static ResultUtils errorToken(String msg) {
        return content(ERROR_TOKEN_CODE, msg, null);
    }

    /**
     * 失败
     *
     * @param msg
     * @return
     */
    public static ResultUtils error(String msg) {
        return content(ERROR_CODE, msg, null);
    }

    /**
     * 没有权限
     *
     * @param msg
     * @return
     */
    public static ResultUtils errorRole(String msg) {
        return content(ERROR_ROLE_CODE, msg, null);
    }

    /**
     * 违规操作，封号
     *
     * @return
     */
    public static ResultUtils errorSuspend() {
        return content(ERROR_SUSPEND_CODE, "Rider Kick", null);
    }

    /**
     * 系统异常
     *
     * @param msg
     * @return
     */
    public static ResultUtils errorException(String msg) {
        return content(ERROR_EXCEPTION, msg, null);
    }
}
