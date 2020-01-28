package com.weiziplus.springboot.common.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * <p>
 * executor.setRejectedExecutionHandler();
 * 四种拒绝策略：
 * 1、AbortPolicy策略
 * 该策略直接抛出异常，阻止系统工作
 * 2、CallerRunsPolicy策略
 * 只要线程池未关闭，该策略直接在调用者线程中运行当前被丢弃的任务。
 * 3、DiscardOldestPolicy策略
 * 丢弃最老的一个请求任务，也就是丢弃一个即将被执行的任务，并尝试再次提交当前任务。
 * 4、DiscardPolicy策略
 * 丢弃无法处理的任务，不予任何处理。
 *
 * @author wanglongwei
 * @date 2019/10/10 10/43
 */
@EnableAsync
@Configuration
public class AsyncTaskPoolConfig {

    /**
     * 创建线程池
     *
     * @return
     */
    private ThreadPoolTaskExecutor createExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //线程池创建时候初始化的线程数
        executor.setCorePoolSize(2);
        //线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(5);
        //用来缓冲执行任务的队列
        executor.setQueueCapacity(100);
        //允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        return executor;
    }

    /**
     * 将系统用户操作存放日志
     *
     * @return
     */
    @Bean("sysLog")
    public Executor sysLog() {
        ThreadPoolTaskExecutor executor = createExecutor();
        //线程池名的前缀
        executor.setThreadNamePrefix("sysLog-");
        //在调用者线程中运行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 更新系统用户最后活跃时间
     *
     * @return
     */
    @Bean("updateAdminUserLastActiveTime")
    public Executor updateAdminUserLastActiveTime() {
        ThreadPoolTaskExecutor executor = createExecutor();
        //线程池名的前缀
        executor.setThreadNamePrefix("updateAdminUserLastActiveTime-");
        //丢弃最老的一个请求任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        return executor;
    }

    /**
     * 删除本地文件
     *
     * @return
     */
    @Bean("deleteFile")
    public Executor deleteFile() {
        ThreadPoolTaskExecutor executor = createExecutor();
        //线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(10);
        //线程池名的前缀
        executor.setThreadNamePrefix("deleteFile-");
        //在调用者线程中运行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}
