package com.weiziplus.springboot.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.common.utils.PageBean;
import com.weiziplus.springboot.pc.system.mapper.SysLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
    public Map<String, Object> getLogList(Integer pageNum, Integer pageSize, String username, Long roleId, String createTime) {
        PageHelper.startPage(pageNum, pageSize);
        return PageBean.pageInfo(mapper.getLogList(username, roleId, createTime));
    }
}
