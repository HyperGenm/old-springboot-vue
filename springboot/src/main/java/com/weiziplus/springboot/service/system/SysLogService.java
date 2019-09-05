package com.weiziplus.springboot.service.system;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.config.GlobalConfig;
import com.weiziplus.springboot.mapper.system.SysLogMapper;
import com.weiziplus.springboot.util.PageUtils;
import com.weiziplus.springboot.util.ResultUtils;
import com.weiziplus.springboot.util.token.AdminTokenUtils;
import com.weiziplus.springboot.util.token.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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
    public ResultUtils getPageList(HttpServletRequest request, Integer pageNum, Integer pageSize, String username, Long roleId,
                                   String description, String ipAddress, String createTime) {
        Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        //是否是超级管理员,0:是
        Integer isSuperAdmin = null;
        if (GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            isSuperAdmin = 0;
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils pageUtil = PageUtils.pageInfo(mapper.getList(isSuperAdmin, username, roleId, description, ipAddress, createTime));
        return ResultUtils.success(pageUtil);
    }
}
