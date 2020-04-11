package com.weiziplus.springboot.common.async;

import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.models.SysUserLog;
import com.weiziplus.springboot.common.models.UserLog;
import com.weiziplus.springboot.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wanglongwei
 * @date 2020/02/07 11/37
 */
@Slf4j
@EnableAsync
@Configuration
public class LogAsync extends BaseService {

    @Autowired
    ErrorAsync errorAsync;

    /**
     * 存放日志
     *
     * @return
     */
    @Bean("saveLog")
    public Executor saveLog() {
        ThreadPoolTaskExecutor executor = AsyncConfig.createExecutor();
        //线程池名的前缀
        executor.setThreadNamePrefix("async-saveLog-");
        //在调用者线程中运行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 将系统用户操作异步记录到数据库
     *
     * @param sysUserLog
     */
    @Async("saveLog")
    public void saveSysUserLog(SysUserLog sysUserLog) {
        if (null == sysUserLog) {
            return;
        }
        //将当前操作记录到数据库
        sysUserLog.setCreateTime(DateUtils.getNowDateTime());
        try {
            baseInsert(sysUserLog);
        } catch (Exception e) {
            log.warn("将系统用户操作异步记录到数据库出错，详情:" + e);
            errorAsync.saveError(e, "将系统用户操作异步保存到数据库");
        }
    }

    /**
     * 将api操作异步记录到数据库
     *
     * @param userLog
     */
    @Async("saveLog")
    public void saveUserLog(UserLog userLog) {
        if (null == userLog) {
            return;
        }
        //将当前操作记录到数据库
        userLog.setCreateTime(DateUtils.getNowDateTime());
        try {
            baseInsert(userLog);
        } catch (Exception e) {
            log.warn("将api操作异步记录到数据库，详情:" + e);
            errorAsync.saveError(e, "将api操作异步保存到数据库");
        }
    }
}
