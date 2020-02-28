package com.weiziplus.springboot.common.async;

import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.models.SysUser;
import com.weiziplus.springboot.common.models.User;
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
 * @date 2020/02/07 11/43
 */
@Slf4j
@EnableAsync
@Configuration
public class UserAsync extends BaseService {

    /**
     * 更新系统用户最后活跃时间
     *
     * @return
     */
    @Bean("updateUserLastActiveTime")
    public Executor updateAdminUserLastActiveTime() {
        ThreadPoolTaskExecutor executor = AsyncConfig.createExecutor();
        //线程池名的前缀
        executor.setThreadNamePrefix("async-updateUserLastActiveTime-");
        //丢弃最老的一个请求任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        return executor;
    }

    /**
     * 更新系统用户最后活跃时间
     *
     * @param userId
     * @param ip
     */
    @Async("updateUserLastActiveTime")
    public void updateAdminUserLastActiveTime(Long userId, String ip) {
        if (null == userId || 0 > userId) {
            return;
        }
        try {
            SysUser user = new SysUser()
                    .setId(userId)
                    .setLastIpAddress(ip)
                    .setLastActiveTime(DateUtils.getNowDateTime());
            baseUpdate(user);
        } catch (Exception e) {
            log.warn("更新系统用户最后活跃时间，详情:" + e);
        }
    }

    /**
     * 更新web用户最后活跃时间
     *
     * @param userId
     * @param ip
     */
    @Async("updateUserLastActiveTime")
    public void updateWebUserLastActiveTime(Long userId, String ip) {
        if (null == userId || 0 > userId) {
            return;
        }
        try {
            User user = new User()
                    .setId(userId)
                    .setLastIpAddress(ip)
                    .setLastActiveTime(DateUtils.getNowDateTime());
            baseUpdate(user);
        } catch (Exception e) {
            log.warn("更新web用户最后活跃时间，详情:" + e);
        }
    }

}