package com.weiziplus.springboot.rabbitmq;

import com.rabbitmq.client.Channel;
import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.models.SysLog;
import com.weiziplus.springboot.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author wanglongwei
 * @date 2019/09/23 09:41
 */
@Slf4j
@Component
public class RabbitmqReceiver extends BaseService {

    @RabbitListener(queues = RabbitmqConfig.QUEUE_SYS_LOG)
    public void receiverSysLog(SysLog sysLog, Channel channel, Message message) throws IOException {
        try {
            sysLog.setCreateTime(DateUtils.getNowDateTime());
            baseInsert(sysLog);
        } catch (Exception e) {
            log.debug("rabbitmq将日志信息插入数据库出错,详情:" + e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}
