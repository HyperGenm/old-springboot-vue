package com.weiziplus.springboot.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.common.util.PageUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.core.pc.system.mapper.UserLogMapper;
import com.weiziplus.springboot.core.pc.system.vo.LogVo;
import com.weiziplus.springboot.core.pc.system.vo.SysLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2019/5/13 15:34
 */
@Service
public class UserLogService {

    @Autowired
    UserLogMapper mapper;

    /**
     * 获取日志列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ResultUtils<PageUtils<List<LogVo>>> getPageList(Integer pageNum, Integer pageSize, String username, String url, Integer type, String description, String ipAddress, String startTime, String endTime) {
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<List<LogVo>> pageUtil = PageUtils.pageInfo(mapper.getList(username, url, type, description, ipAddress, startTime, endTime));
        return ResultUtils.success(pageUtil);
    }
}
