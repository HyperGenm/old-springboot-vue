package com.weiziplus.springboot.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.util.PageUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.common.util.token.AdminTokenUtils;
import com.weiziplus.springboot.core.pc.system.mapper.SysUserLogMapper;
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
public class SysUserLogService {

    @Autowired
    SysUserLogMapper mapper;

    /**
     * 获取日志列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ResultUtils<PageUtils<List<SysLogVo>>> getPageList(HttpServletRequest request, Integer pageNum, Integer pageSize, String username, Long roleId,
                                                              String description, Integer type, String ipAddress, String startTime, String endTime) {
        Long nowUserId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        //是否是超级管理员,0:是
        Integer isSuperAdmin = null;
        if (GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            isSuperAdmin = 0;
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<List<SysLogVo>> pageUtil = PageUtils.pageInfo(mapper.getList(isSuperAdmin, username, roleId, description, type, ipAddress, startTime, endTime));
        return ResultUtils.success(pageUtil);
    }
}
