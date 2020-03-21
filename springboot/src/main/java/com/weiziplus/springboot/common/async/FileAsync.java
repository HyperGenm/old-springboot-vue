package com.weiziplus.springboot.common.async;

import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.util.ToolUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 文件异步处理
 *
 * @author wanglongwei
 * @date 2020/02/07 11/33
 */
@EnableAsync
@Configuration
public class FileAsync {

    /**
     * 删除本地文件
     *
     * @return
     */
    @Bean("deleteFile")
    public Executor deleteFile() {
        ThreadPoolTaskExecutor executor = AsyncConfig.createExecutor();
        //线程池创建时候初始化的线程数
        executor.setCorePoolSize(2);
        //线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(3);
        //用来缓冲执行任务的队列
        executor.setQueueCapacity(50);
        //线程池名的前缀
        executor.setThreadNamePrefix("async-deleteFile-");
        //在调用者线程中运行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 删除本地文件
     *
     * @param relativePath
     */
    @Async("deleteFile")
    public void deleteFile(String relativePath) {
        if (ToolUtils.isBlank(relativePath)) {
            return;
        }
        File file = new File(GlobalConfig.getBaseFilePath() + relativePath);
        //如果是目录，不进行操作
        if (file.isDirectory()) {
            return;
        }
        if (file.isFile() && file.exists()) {
            boolean delete = file.delete();
        }
    }

    /**
     * 删除本地文件
     *
     * @param relativePathList
     */
    @Async("deleteFile")
    public void deleteFiles(List<String> relativePathList) {
        if (null == relativePathList || 0 >= relativePathList.size()) {
            return;
        }
        for (String path : relativePathList) {
            File file = new File(GlobalConfig.getBaseFilePath() + path);
            //如果是目录，不进行操作
            if (file.isDirectory()) {
                continue;
            }
            if (file.isFile() && file.exists()) {
                boolean delete = file.delete();
            }
        }
    }

}
