package com.weiziplus.springboot.service.system;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.config.GlobalConfig;
import com.weiziplus.springboot.models.SysUser;
import com.weiziplus.springboot.utils.*;
import com.weiziplus.springboot.utils.token.AdminTokenUtil;
import com.weiziplus.springboot.utils.token.JwtTokenUtil;
import com.weiziplus.springboot.mapper.system.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    public ResultUtil getUserList(Integer pageNum, Integer pageSize, String userName, Long roleId, Integer allowLogin, String lastActiveTime, String createTime) {
        if (0 >= pageNum || 0 >= pageSize) {
            return ResultUtil.error("pageNum,pageSize错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtil pageUtil = PageUtil.pageInfo(mapper.getUserList(userName, roleId, allowLogin, lastActiveTime, createTime));
        return ResultUtil.success(pageUtil);
    }

    /**
     * 添加用户
     *
     * @param sysUser
     * @return
     */
    public ResultUtil addUser(SysUser sysUser) {
        if (ValidateUtil.notUsername(sysUser.getUsername())) {
            return ResultUtil.error("用户名不能包含特殊字符");
        }
        if (ValidateUtil.notPassword(sysUser.getPassword())) {
            return ResultUtil.error("密码为6-20位大小写和数字");
        }
        if (ValidateUtil.notRealName(sysUser.getRealName())) {
            return ResultUtil.error("真实姓名格式错误");
        }
        SysUser user = mapper.getUserInfoByName(sysUser.getUsername());
        if (null != user) {
            return ResultUtil.error("用户名已存在");
        }
        sysUser.setPassword(Md5Util.encode(sysUser.getPassword()));
        sysUser.setCreateTime(DateUtil.getNowDateTime());
        sysUser.setSuspendNum(null);
        sysUser.setRoleId(null);
        return ResultUtil.success(baseInsert(sysUser));
    }

    /**
     * 更新用户
     *
     * @param sysUser
     * @return
     */
    public ResultUtil updateUser(HttpServletRequest request, SysUser sysUser) {
        if (ValidateUtil.notUsername(sysUser.getUsername())) {
            return ResultUtil.error("用户名不能包含特殊字符");
        }
        if (ValidateUtil.notRealName(sysUser.getRealName())) {
            return ResultUtil.error("真实姓名格式错误");
        }
        Long nowUserId = JwtTokenUtil.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            if (null != sysUser.getSuspendNum() || GlobalConfig.ALLOW_LOGIN_TWO.equals(sysUser.getAllowLogin())) {
                mapper.suspendSysUser(nowUserId);
                AdminTokenUtil.deleteToken(nowUserId);
                return ResultUtil.errorSuspend();
            }
        }
        SysUser user = mapper.getUserInfoByName(sysUser.getUsername());
        if (null != user && !sysUser.getId().equals(user.getId())) {
            return ResultUtil.error("用户名已存在");
        }
        if (null != user && GlobalConfig.ALLOW_LOGIN_TWO.equals(user.getAllowLogin())) {
            return ResultUtil.error("用户封号中，不能修改状态");
        }
        sysUser.setAllowLogin(null);
        sysUser.setPassword(null);
        sysUser.setRoleId(null);
        sysUser.setSuspendNum(null);
        return ResultUtil.success(baseUpdate(sysUser));
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    public ResultUtil deleteUser(HttpServletRequest request, Long[] ids) {
        if (null == ids || 0 >= ids.length) {
            return ResultUtil.error("ids为空");
        }
        for (Long id : ids) {
            if (GlobalConfig.SUPER_ADMIN_ID.equals(id)) {
                Long nowUserId = JwtTokenUtil.getUserIdByHttpServletRequest(request);
                if (GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
                    return ResultUtil.error("不能删除超级管理员");
                }
                mapper.suspendSysUser(nowUserId);
                AdminTokenUtil.deleteToken(nowUserId);
                return ResultUtil.errorSuspend();
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
    public ResultUtil updateUserRole(HttpServletRequest request, Long userId, Long roleId) {
        if (null == userId || 0 >= userId) {
            return ResultUtil.error("userId不能为空");
        }
        Long nowUserId = JwtTokenUtil.getUserIdByHttpServletRequest(request);
        if (GlobalConfig.SUPER_ADMIN_ID.equals(userId)) {
            if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
                mapper.suspendSysUser(nowUserId);
                AdminTokenUtil.deleteToken(nowUserId);
                return ResultUtil.errorSuspend();
            } else {
                return ResultUtil.error("不能修改超级管理员的角色");
            }
        }
        if (null == roleId || 0 > roleId) {
            return ResultUtil.error("roleId错误");
        }
        if (GlobalConfig.SUPER_ADMIN_ROLE_ID.equals(roleId) && !GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            mapper.suspendSysUser(nowUserId);
            AdminTokenUtil.deleteToken(nowUserId);
            return ResultUtil.errorSuspend();
        }
        return ResultUtil.success(mapper.updateRoleIdByUserIdAndRoleId(userId, roleId));
    }

    /**
     * 修改用户密码
     *
     * @param request
     * @param oldPwd
     * @param newPwd
     * @return
     */
    public ResultUtil updatePassword(HttpServletRequest request, String oldPwd, String newPwd) {
        if (ValidateUtil.notPassword(oldPwd) || ValidateUtil.notPassword(newPwd)) {
            return ResultUtil.error("密码为6-20位大小写和数字");
        }
        Long userId = JwtTokenUtil.getUserIdByHttpServletRequest(request);
        Map<String, Object> map = baseFindByClassAndId(SysUser.class, userId);
        String passwordFiled = "password";
        if (null == map || null == map.get(passwordFiled) || !Md5Util.encode(oldPwd).equals(map.get(passwordFiled).toString())) {
            return ResultUtil.error("原密码错误");
        }
        mapper.resetUserPassword(userId, Md5Util.encode(newPwd));
        AdminTokenUtil.deleteToken(userId);
        return ResultUtil.success();
    }

    /**
     * 重置用户密码
     *
     * @param request
     * @param userId
     * @param password
     * @return
     */
    public ResultUtil resetUserPassword(HttpServletRequest request, Long userId, String password) {
        if (null == userId || 0 > userId) {
            return ResultUtil.error("id不能为空");
        }
        if (ValidateUtil.notPassword(password)) {
            return ResultUtil.error("密码为6-20位大小写和数字");
        }
        Long nowUserId = JwtTokenUtil.getUserIdByHttpServletRequest(request);
        if (GlobalConfig.SUPER_ADMIN_ID.equals(userId) && !GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            mapper.suspendSysUser(nowUserId);
            AdminTokenUtil.deleteToken(nowUserId);
            return ResultUtil.errorSuspend();
        }
        String newPwd = Md5Util.encode(password);
        return ResultUtil.success(mapper.resetUserPassword(userId, newPwd));
    }

    /**
     * 解除封号
     *
     * @param request
     * @param userId
     * @return
     */
    public ResultUtil relieveSuspend(HttpServletRequest request, Long userId) {
        Long nowUserId = JwtTokenUtil.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            mapper.suspendSysUser(nowUserId);
            AdminTokenUtil.deleteToken(nowUserId);
            return ResultUtil.errorSuspend();
        }
        if (null == userId || 0 >= userId) {
            return ResultUtil.error("userId错误");
        }
        return ResultUtil.success(mapper.relieveSuspend(userId));
    }
}
