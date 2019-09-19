package com.weiziplus.springboot.config;

import com.weiziplus.springboot.util.ResultUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author wanglongwei
 * @data 2019/5/6 15:50
 */
@RestControllerAdvice
public class GlobalExceptionConfig {

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
        if (RESPONSE_SHOW_RUNTIME_EXCEPTION) {
            return ResultUtils.errorException(String.valueOf(ex));
        } else {
            return ResultUtils.errorException("系统错误，请重试");
        }
    }
}
