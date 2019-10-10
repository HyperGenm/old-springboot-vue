package com.weiziplus.springboot.async;

import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.mapper.system.SysUserMapper;
import com.weiziplus.springboot.models.SysLog;
import com.weiziplus.springboot.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 异步处理系统用户操作
 *
 * @author wanglongwei
 * @date 2019/10/10 09/31
 */
@Slf4j
@Component
public class SystemAsync extends BaseService {

    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * 将系统用户操作异步记录到数据库
     *
     * @param sysLog
     */
    @Async("sysLog")
    public void handleSysLog(SysLog sysLog) {
        //将当前操作记录到数据库
        sysLog.setCreateTime(DateUtils.getNowDateTime());
        baseInsert(sysLog);
    }

    @Async("updateAdminUserLastActiveTime")
    public void updateLastActiveTime(Long userId, String ip) {
        //更新用户最后活跃时间
        sysUserMapper.updateLastActiveTimeByIdAndIp(userId, ip);
    }

}
