package com.weiziplus.springboot.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置
 *
 * @author wanglongwei
 * @date 2019/09/23 09:34
 */
@Configuration
public class RabbitmqConfig {

    /**
     * 系统日志
     */
    public final static String QUEUE_SYS_LOG = "sys_log";

    @Bean
    public Queue queueSysLog() {
        return new Queue(QUEUE_SYS_LOG);
    }

}
