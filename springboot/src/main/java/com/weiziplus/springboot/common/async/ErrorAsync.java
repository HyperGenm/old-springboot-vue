package com.weiziplus.springboot.common.async;

import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.models.SysError;
import com.weiziplus.springboot.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wanglongwei
 * @date 2020/03/21 14/52
 */
@Slf4j
@EnableAsync
@Configuration
public class ErrorAsync extends BaseService {

    /**
     * 存放日志
     *
     * @return
     */
    @Bean("sysError")
    public Executor sysError() {
        ThreadPoolTaskExecutor executor = AsyncConfig.createExecutor();
        //线程池创建时候初始化的线程数
        executor.setCorePoolSize(2);
        //线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(3);
        //用来缓冲执行任务的队列
        executor.setQueueCapacity(50);
        //线程池名的前缀
        executor.setThreadNamePrefix("async-sysError-");
        //在调用者线程中运行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 将系统异常异步存储到数据库
     *
     * @param ex
     */
    @Async("sysError")
    public void saveError(RuntimeException ex) {
        if (null == ex) {
            return;
        }
        try {
            SysError error = createError(ex.getStackTrace()[0])
                    .setContent(ex.toString());
            baseInsert(error);
        } catch (Exception e) {
            log.warn("将系统异常异步存储到数据库出错，详情:" + e);
        }
    }

    /**
     * 将系统异常异步存储到数据库
     *
     * @param ex
     */
    @Async("sysError")
    public void saveError(Exception ex) {
        if (null == ex) {
            return;
        }
        try {
            SysError error = createError(ex.getStackTrace()[0])
                    .setContent(ex.toString());
            baseInsert(error);
        } catch (Exception e) {
            log.warn("将系统异常异步存储到数据库出错，详情:" + e);
        }
    }

    /**
     * 创建error
     *
     * @param stackTraceElement
     * @return
     */
    private SysError createError(StackTraceElement stackTraceElement) {
        return new SysError()
                .setClassName(stackTraceElement.getClassName())
                .setMethodName(stackTraceElement.getMethodName())
                .setLineNumber(stackTraceElement.getLineNumber())
                .setCreateTime(DateUtils.getNowDateTime());
    }

}
