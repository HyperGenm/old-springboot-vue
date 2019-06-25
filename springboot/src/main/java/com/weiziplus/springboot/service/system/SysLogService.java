package com.weiziplus.springboot.service.system;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.utils.PageUtil;
import com.weiziplus.springboot.mapper.system.SysLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wanglongwei
 * @data 2019/5/13 15:34
 */
@Service
public class SysLogService {
    @Autowired
    SysLogMapper mapper;

    /**
     * 获取日志列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageUtil getLogList(Integer pageNum, Integer pageSize, String username, Long roleId, String createTime, String description) {
        PageHelper.startPage(pageNum, pageSize);
        return PageUtil.pageInfo(mapper.getLogList(username, roleId, createTime,description));
    }
}
