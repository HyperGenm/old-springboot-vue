package com.weiziplus.springboot.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.common.async.SystemAsync;
import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.models.SysUser;
import com.weiziplus.springboot.common.util.*;
import com.weiziplus.springboot.common.util.token.AdminTokenUtils;
import com.weiziplus.springboot.common.util.token.JwtTokenUtils;
import com.weiziplus.springboot.core.pc.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * @author wanglongwei
 * @date 2019/5/10 9:00
 */
@Slf4j
@Service
public class SysUserService extends BaseService {

    @Autowired
    SysUserMapper mapper;

    @Autowired
    SystemAsync systemAsync;

    /**
     * 用户允许禁止登录
     */
    public static final Integer ADMIN_USER_ALLOW_LOGIN_ONE = 1;

    /**
     * 系统用户封号中
     */
    public static final Integer ADMIN_USER_ALLOW_LOGIN_TWO = 2;

    /**
     * 获取用户列表
     *
     * @param pageNum
     * @param pageSize
     * @param userName
     * @param allowLogin
     * @return
     */
    public ResultUtils getPageList(Integer pageNum, Integer pageSize, String userName, Long roleId, Integer allowLogin, String lastActiveTime, String createTime) {
        if (0 >= pageNum || 0 >= pageSize) {
            return ResultUtils.error("pageNum,pageSize错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils pageUtil = PageUtils.pageInfo(mapper.getUserList(userName, roleId, allowLogin, lastActiveTime, createTime));
        return ResultUtils.success(pageUtil);
    }

    /**
     * 添加用户
     *
     * @param sysUser
     * @return
     */
    public ResultUtils addUser(SysUser sysUser) {
        //中英文开头、数字、下划线
        if (ValidateUtils.notChinaEnglishNumberUnderline(sysUser.getUsername())) {
            return ResultUtils.error("用户名不能包含特殊字符");
        }
        if (ValidateUtils.notPassword(sysUser.getPassword())) {
            return ResultUtils.error("密码为6-20位大小写和数字");
        }
        //正常格式真实姓名:中文或英文包括空格和点
        if (ValidateUtils.notRealName(sysUser.getRealName())) {
            return ResultUtils.error("真实姓名格式错误");
        }
        SysUser user = mapper.getUserInfoByName(sysUser.getUsername());
        if (null != user) {
            return ResultUtils.error("用户名已存在");
        }
        sysUser.setPassword(Md5Utils.encode(sysUser.getPassword()))
                .setCreateTime(DateUtils.getNowDateTime())
                .setSuspendNum(null)
                .setRoleId(null);
        return ResultUtils.success(baseInsert(sysUser));
    }

    /**
     * 更新用户
     *
     * @param sysUser
     * @return
     */
    public ResultUtils updateUser(HttpServletRequest request, SysUser sysUser) {
        //中英文开头、数字、下划线
        if (ValidateUtils.notChinaEnglishNumberUnderline(sysUser.getUsername())) {
            return ResultUtils.error("用户名不能包含特殊字符");
        }
        //正常格式真实姓名:中文或英文包括空格和点
        if (ValidateUtils.notRealName(sysUser.getRealName())) {
            return ResultUtils.error("真实姓名格式错误");
        }
        Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            if (null != sysUser.getSuspendNum() || ADMIN_USER_ALLOW_LOGIN_TWO.equals(sysUser.getAllowLogin())) {
                mapper.suspendSysUser(nowUserId);
                AdminTokenUtils.deleteToken(nowUserId);
                return ResultUtils.errorSuspend();
            }
        }
        SysUser user = mapper.getUserInfoByName(sysUser.getUsername());
        if (null != user && !user.getId().equals(sysUser.getId())) {
            return ResultUtils.error("用户名已存在");
        }
        if (null != user && ADMIN_USER_ALLOW_LOGIN_TWO.equals(user.getAllowLogin())) {
            user.setAllowLogin(null);
        }
        //如果用户禁用---强制用户下线
        if (ADMIN_USER_ALLOW_LOGIN_ONE.equals(sysUser.getAllowLogin())) {
            AdminTokenUtils.deleteToken(sysUser.getId());
        }
        SysUser newUser = new SysUser()
                .setId(sysUser.getId())
                .setAllowLogin(sysUser.getAllowLogin())
                .setRealName(sysUser.getRealName())
                .setUsername(sysUser.getUsername());
        return ResultUtils.success(baseUpdate(newUser));
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    public ResultUtils deleteUser(HttpServletRequest request, Long[] ids) {
        if (null == ids || 0 >= ids.length) {
            return ResultUtils.error("ids为空");
        }
        for (Long id : ids) {
            if (GlobalConfig.SUPER_ADMIN_ID.equals(id)) {
                Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
                if (GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
                    return ResultUtils.error("不能删除超级管理员");
                }
                mapper.suspendSysUser(nowUserId);
                AdminTokenUtils.deleteToken(nowUserId);
                return ResultUtils.errorSuspend();
            }
        }
        return ResultUtils.success(baseDeleteByClassAndIds(SysUser.class, ids));
    }

    /**
     * 更新用户角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    public ResultUtils updateUserRole(HttpServletRequest request, Long userId, Long roleId) {
        if (null == userId || 0 >= userId) {
            return ResultUtils.error("userId不能为空");
        }
        Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        if (GlobalConfig.SUPER_ADMIN_ID.equals(userId)) {
            if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
                mapper.suspendSysUser(nowUserId);
                AdminTokenUtils.deleteToken(nowUserId);
                return ResultUtils.errorSuspend();
            } else {
                return ResultUtils.error("不能修改超级管理员的角色");
            }
        }
        if (null == roleId || 0 > roleId) {
            return ResultUtils.error("roleId错误");
        }
        if (GlobalConfig.SUPER_ADMIN_ROLE_ID.equals(roleId) && !GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            mapper.suspendSysUser(nowUserId);
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
        }
        //用户权限更改，强制下线
        AdminTokenUtils.deleteToken(userId);
        mapper.updateRoleIdByUserIdAndRoleId(userId, roleId);
        return ResultUtils.success();
    }

    /**
     * 修改用户密码
     *
     * @param request
     * @param oldPwd
     * @param newPwd
     * @return
     */
    public ResultUtils updatePassword(HttpServletRequest request, String oldPwd, String newPwd) {
        if (ValidateUtils.notPassword(oldPwd) || ValidateUtils.notPassword(newPwd)) {
            return ResultUtils.error("密码为6-20位大小写和数字");
        }
        Long userId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        Map<String, Object> map = baseFindByClassAndId(SysUser.class, userId);
        String passwordFiled = "password";
        if (null == map || null == map.get(passwordFiled)) {
            return ResultUtils.error("原密码错误");
        }
        if (!Md5Utils.encode(oldPwd).equals(ToolUtils.valueOfString(map.get(passwordFiled)))) {
            return ResultUtils.error("原密码错误");
        }
        mapper.resetUserPassword(userId, Md5Utils.encode(newPwd));
        AdminTokenUtils.deleteToken(userId);
        return ResultUtils.success();
    }

    /**
     * 重置用户密码
     *
     * @param request
     * @param userId
     * @param password
     * @return
     */
    public ResultUtils resetUserPassword(HttpServletRequest request, Long userId, String password) {
        if (null == userId || 0 > userId) {
            return ResultUtils.error("id不能为空");
        }
        if (ValidateUtils.notPassword(password)) {
            return ResultUtils.error("密码为6-20位大小写和数字");
        }
        Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        if (GlobalConfig.SUPER_ADMIN_ID.equals(userId) && !GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            mapper.suspendSysUser(nowUserId);
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
        }
        String newPwd = Md5Utils.encode(password);
        return ResultUtils.success(mapper.resetUserPassword(userId, newPwd));
    }

    /**
     * 解除封号
     *
     * @param request
     * @param userId
     * @return
     */
    public ResultUtils relieveSuspend(HttpServletRequest request, Long userId) {
        Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            mapper.suspendSysUser(nowUserId);
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
        }
        if (null == userId || 0 >= userId) {
            return ResultUtils.error("userId错误");
        }
        return ResultUtils.success(mapper.relieveSuspend(userId));
    }

    /**
     * 修改头像
     *
     * @param request
     * @param file
     * @return
     */
    public ResultUtils updateIcon(HttpServletRequest request, MultipartFile file) {
        Long userId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        BufferedImage image = FileUtils.getImage(file);
        if (null == image) {
            return ResultUtils.error("请上传图片");
        }
        //长宽比
        float minScale = 0.7F;
        float maxScale = 1.2F;
        double scale = 1F;
        if (0 != image.getWidth()) {
            scale = (float) image.getHeight() / image.getWidth();
        }
        if (scale < minScale || scale > maxScale) {
            return ResultUtils.error("头像建议长宽比1:1");
        }
        String path = FileUtils.upFilePc(file, "user/icon");
        if (null == path) {
            return ResultUtils.error("文件上传失败，请重试");
        }
        //异步删除原来的图片
        Map<String, Object> sysUserMap = baseFindByClassAndId(SysUser.class, userId);
        systemAsync.deleteFile(ToolUtils.valueOfString(sysUserMap.get("icon")));
        SysUser user = new SysUser()
                .setId(userId)
                .setIcon(path);
        baseUpdate(user);
        return ResultUtils.success(GlobalConfig.getMybatisFilePathPrefix() + path);
    }
}
