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
    public ResultUtil getUserList(Integer pageNum, Integer pageSize, String userName, Integer allowLogin, String createTime) {
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
    public ResultUtil addUser(SysUser sysUser) {
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
    public ResultUtil updateUser(SysUser sysUser) {
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
    public ResultUtil deleteUser(Long[] ids) {
        if (null == ids || 0 >= ids.length) {
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
    public ResultUtil updateUserRole(Long userId, Long roleId) {
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
        if (GlobalConfig.SUPER_ADMIN_ID.equals(userId) && !GlobalConfig.SUPER_ADMIN_ID.equals(JwtTokenUtil.getUserIdByHttpServletRequest(request))) {
            return ResultUtil.error("您没有权限");
        }
        String newPwd = Md5Util.encode(password);
        return ResultUtil.success(mapper.resetUserPassword(userId, newPwd));
    }
}
