package com.weiziplus.springboot.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.core.pc.system.mapper.SysLogMapper;
import com.weiziplus.springboot.common.util.PageUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.common.util.token.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wanglongwei
 * @date 2019/5/13 15:34
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
    public ResultUtils getPageList(HttpServletRequest request, Integer pageNum, Integer pageSize, String username, Long roleId,
                                   String description, String ipAddress, String startTime, String endTime) {
        Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        //是否是超级管理员,0:是
        Integer isSuperAdmin = null;
        if (GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            isSuperAdmin = 0;
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils pageUtil = PageUtils.pageInfo(mapper.getList(isSuperAdmin, username, roleId, description, ipAddress, startTime, endTime));
        return ResultUtils.success(pageUtil);
    }
}
