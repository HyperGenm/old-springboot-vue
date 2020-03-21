package com.weiziplus.springboot.common.config;

import com.weiziplus.springboot.common.async.ErrorAsync;
import com.weiziplus.springboot.common.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author wanglongwei
 * @date 2019/5/6 15:50
 */
@RestControllerAdvice
public class GlobalExceptionConfig {

    @Autowired
    ErrorAsync errorAsync;

    /**
     * 是否將异常详情展示给前端
     */
    @Value("${global.response-show-runtime-exception:false}")
    private final Boolean RESPONSE_SHOW_RUNTIME_EXCEPTION = false;

    /**
     * 捕获运行时异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResultUtils runtimeExceptionHandler(RuntimeException ex) {
        ex.printStackTrace();
        errorAsync.saveError(ex);
        if (RESPONSE_SHOW_RUNTIME_EXCEPTION) {
            return ResultUtils.errorException("系统异常，详情:" + ex.getMessage());
        } else {
            return ResultUtils.errorException("系统错误，请重试");
        }
    }

    /**
     * 捕获所有异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultUtils exceptionHandler(Exception ex) {
        ex.printStackTrace();
        errorAsync.saveError(ex);
        if (RESPONSE_SHOW_RUNTIME_EXCEPTION) {
            return ResultUtils.errorException("系统异常，详情:" + ex.getMessage());
        } else {
            return ResultUtils.errorException("系统错误，请重试");
        }
    }

    /**
     * 405异常
     *
     * @return
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResultUtils httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResultUtils.error("请使用" + ex.getSupportedHttpMethods() + "方法请求,详情:" + ex.getMessage());
    }

}
