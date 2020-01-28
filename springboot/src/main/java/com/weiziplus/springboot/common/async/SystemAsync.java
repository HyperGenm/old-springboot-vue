package com.weiziplus.springboot.common.async;

import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.core.pc.system.mapper.SysUserMapper;
import com.weiziplus.springboot.common.models.SysLog;
import com.weiziplus.springboot.common.util.DateUtils;
import com.weiziplus.springboot.common.util.ToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

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
        if (null == sysLog) {
            return;
        }
        //将当前操作记录到数据库
        sysLog.setCreateTime(DateUtils.getNowDateTime());
        baseInsert(sysLog);
    }

    /**
     * 更新系统用户最后活跃时间
     *
     * @param userId
     * @param ip
     */
    @Async("updateAdminUserLastActiveTime")
    public void updateLastActiveTime(Long userId, String ip) {
        if (null == userId || 0 > userId) {
            return;
        }
        sysUserMapper.updateLastActiveTimeByIdAndIp(userId, ip);
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
