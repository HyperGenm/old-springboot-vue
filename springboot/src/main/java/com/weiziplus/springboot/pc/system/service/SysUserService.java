package com.weiziplus.springboot.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.models.SysUser;
import com.weiziplus.springboot.common.utils.*;
import com.weiziplus.springboot.common.utils.token.JwtTokenUtil;
import com.weiziplus.springboot.pc.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/10 9:00
 */
@Slf4j
@Service
public class SysUserService extends BaseService {
    @Autowired
    SysUserMapper mapper;

    /**
     * 获取用户列表
     *
     * @param pageNum
     * @param pageSize
     * @param userName
     * @param allowLogin
     * @return
     */
    public Map<String, Object> getUserList(Integer pageNum, Integer pageSize, String userName, Integer allowLogin, String createTime) {
        if (0 >= pageNum || 0 >= pageSize) {
            return ResultUtil.error("pageNum,pageSize错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> map = PageUtil.pageInfo(mapper.getUserList(userName, allowLogin, createTime));
        return ResultUtil.success(map);
    }

    /**
     * 添加用户
     *
     * @param sysUser
     * @return
     */
    public Map<String, Object> addUser(SysUser sysUser) {
        if (ValidateUtil.notUsername(sysUser.getUsername())) {
            return ResultUtil.error("用户名不能包含特殊字符");
        }
        if (ValidateUtil.notPassword(sysUser.getPassword())) {
            return ResultUtil.error("密码为6-20位大小写和数字");
        }
        if (null != sysUser.getRealName() && ValidateUtil.notRealName(sysUser.getRealName())) {
            return ResultUtil.error("真实姓名格式错误");
        }
        SysUser user = mapper.getUserInfoByName(sysUser.getUsername());
        if (null != user) {
            return ResultUtil.error("用户名已存在");
        }
        sysUser.setPassword(Md5Util.encode(sysUser.getPassword()));
        sysUser.setCreateTime(DateUtil.getDate());
        return ResultUtil.success(baseInsert(sysUser));
    }

    /**
     * 更新用户
     *
     * @param sysUser
     * @return
     */
    public Map<String, Object> updateUser(SysUser sysUser) {
        if (ValidateUtil.notUsername(sysUser.getUsername())) {
            return ResultUtil.error("用户名不能包含特殊字符");
        }
        if (null != sysUser.getRealName() && ValidateUtil.notRealName(sysUser.getRealName())) {
            return ResultUtil.error("真实姓名格式错误");
        }
        SysUser user = mapper.getUserInfoByName(sysUser.getUsername());
        if (null != user && !sysUser.getId().equals(user.getId())) {
            return ResultUtil.error("用户名已存在");
        }
        return ResultUtil.success(baseUpdate(sysUser));
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    public Map<String, Object> deleteUser(Long[] ids) {
        if (0 >= ids.length) {
            return ResultUtil.error("ids为空");
        }
        for (Long id : ids) {
            if (GlobalConfig.SUPER_ADMIN_ID.equals(id)) {
                return ResultUtil.error("不能删除超级管理员");
            }
        }
        return ResultUtil.success(baseDeleteByClassAndIds(SysUser.class, ids));
    }

    /**
     * 更新用户角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    public Map<String, Object> updateUserRole(Long userId, Long roleId) {
        if (null == userId || 0 >= userId) {
            return ResultUtil.error("userId不能为空");
        }
        if (GlobalConfig.SUPER_ADMIN_ID.equals(userId)) {
            return ResultUtil.error("不能修改超级管理员角色");
        }
        if (null == roleId || 0 > roleId) {
            return ResultUtil.error("roleId不能为空");
        }
        return ResultUtil.success(mapper.updateRoleIdByUserIdAndRoleId(userId, roleId));
    }

    /**
     * 重置用户密码
     *
     * @param request
     * @param userId
     * @param password
     * @return
     */
    public Map<String, Object> resetUserPassword(HttpServletRequest request, Long userId, String password) {
        if (null == userId || 0 > userId) {
            return ResultUtil.error("id不能为空");
        }
        if (ValidateUtil.notPassword(password)) {
            return ResultUtil.error("密码为6-20位大小写和数字");
        }
        if (GlobalConfig.SUPER_ADMIN_ID.equals(userId) && !GlobalConfig.SUPER_ADMIN_ID.equals(JwtTokenUtil.getUserIdByHttpServletRequest(request))) {
            return ResultUtil.error("您没有权限");
        }
        String newPwd = Md5Util.encode(password);
        return ResultUtil.success(mapper.resetUserPassword(userId, newPwd));
    }
}
