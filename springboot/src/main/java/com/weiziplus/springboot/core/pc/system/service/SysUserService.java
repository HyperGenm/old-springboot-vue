package com.weiziplus.springboot.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.common.async.SystemAsync;
import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.models.SysUser;
import com.weiziplus.springboot.common.util.*;
import com.weiziplus.springboot.common.util.token.AdminTokenUtils;
import com.weiziplus.springboot.core.pc.system.mapper.SysUserMapper;
import com.weiziplus.springboot.core.pc.system.vo.SysUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.util.List;
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
     * 获取用户列表
     *
     * @param pageNum
     * @param pageSize
     * @param userName
     * @param allowLogin
     * @return
     */
    public ResultUtils<PageUtils<List<SysUserVo>>> getPageList(Integer pageNum, Integer pageSize, String userName, Long roleId, Integer allowLogin, String lastActiveTime, String createTime) {
        if (0 >= pageNum || 0 >= pageSize) {
            return ResultUtils.error("pageNum,pageSize错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<List<SysUserVo>> pageUtil = PageUtils.pageInfo(mapper.getUserList(userName, roleId, allowLogin, lastActiveTime, createTime));
        return ResultUtils.success(pageUtil);
    }

    /**
     * 添加用户
     *
     * @param sysUser
     * @return
     */
    public ResultUtils addUser(SysUser sysUser) {
        //用户名最小长度
        int minUsernameLength = 2;
        if (ToolUtils.isBlank(sysUser.getUsername()) || minUsernameLength > sysUser.getUsername().length()) {
            return ResultUtils.error("用户名最少两位");
        }
        //md5长度是32位
        int md5Length = 32;
        if (ToolUtils.isBlank(sysUser.getPassword()) || md5Length != sysUser.getPassword().length()) {
            return ResultUtils.error("密码设置错误");
        }
        //正常格式真实姓名:中文或英文包括空格和点
        if (ValidateUtils.notRealName(sysUser.getRealName())) {
            return ResultUtils.error("真实姓名格式错误");
        }
        //如果填写了手机号码
        if (!ToolUtils.isBlank(sysUser.getPhone())) {
            if (ValidateUtils.notPhone(sysUser.getPhone())) {
                return ResultUtils.error("手机号码格式不正确");
            }
        }
        SysUser user = baseFindOneDataByClassAndColumnAndValue(SysUser.class, SysUser.COLUMN_USERNAME, sysUser.getUsername());
        if (null != user) {
            return ResultUtils.error("用户名已存在");
        }
        sysUser.setPassword(Md5Utils.encode(sysUser.getPassword()))
                .setRoleId(null)
                .setCreateTime(DateUtils.getNowDateTime());
        baseInsert(sysUser);
        return ResultUtils.success();
    }

    /**
     * 更新用户
     *
     * @param sysUser
     * @return
     */
    public ResultUtils updateUser(HttpServletRequest request, SysUser sysUser) {
        Long nowUserId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            //非超级管理员操作超级管理员，强制下线
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
        }
        //正常格式真实姓名:中文或英文包括空格和点
        if (ValidateUtils.notRealName(sysUser.getRealName())) {
            return ResultUtils.error("真实姓名格式错误");
        }
        //如果填写了手机号码
        if (!ToolUtils.isBlank(sysUser.getPhone())) {
            if (ValidateUtils.notPhone(sysUser.getPhone())) {
                return ResultUtils.error("手机号码格式不正确");
            }
        }
        SysUser user = baseFindByClassAndId(SysUser.class, sysUser.getId());
        if (null == user) {
            return ResultUtils.error("id错误");
        }
        //如果用户禁用---强制用户下线
        if (SysUser.ALLOW_LOGIN_FORBID.equals(sysUser.getAllowLogin())) {
            AdminTokenUtils.deleteToken(sysUser.getId());
        }
        //当前几项无需修改
        sysUser.setUsername(null)
                .setPassword(null)
                .setCreateTime(null)
                .setLastActiveTime(null)
                .setLastIpAddress(null)
                .setUsername(null);
        baseUpdate(sysUser);
        return ResultUtils.success();
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
            if (!GlobalConfig.SUPER_ADMIN_ID.equals(id)) {
                continue;
            }
            Long nowUserId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
            if (GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
                return ResultUtils.error("不能删除超级管理员");
            }
            //非超级管理员删除超级管理员
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
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
    public ResultUtils updateUserRole(HttpServletRequest request, Long userId, Integer roleId) {
        if (null == userId || 0 >= userId) {
            return ResultUtils.error("userId不能为空");
        }
        Long nowUserId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        //要修改的用户是超级管理员
        if (GlobalConfig.SUPER_ADMIN_ID.equals(userId)) {
            //非超级管理员修改超级管理员的角色，强制下线
            if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
                AdminTokenUtils.deleteToken(nowUserId);
                return ResultUtils.errorSuspend();
            } else {
                return ResultUtils.error("不能修改超级管理员的角色");
            }
        }
        if (null == roleId || 0 > roleId) {
            return ResultUtils.error("roleId错误");
        }
        //要设置的角色是超级管理员
        if (GlobalConfig.SUPER_ADMIN_ROLE_ID.equals(roleId)) {
            //非超级管理员设置用户为超级管理员，强制下线
            if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
                AdminTokenUtils.deleteToken(nowUserId);
                return ResultUtils.errorSuspend();
            } else {
                return ResultUtils.error("超级管理员只能有一个");
            }
        }
        //用户权限更改，强制下线
        AdminTokenUtils.deleteToken(userId);
        SysUser sysUser = new SysUser()
                .setId(userId)
                .setRoleId(roleId);
        baseUpdate(sysUser);
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
        //md5长度是32位
        int md5Length = 32;
        if (ToolUtils.isBlank(oldPwd) || md5Length != oldPwd.length()) {
            return ResultUtils.error("原密码错误");
        }
        if (ToolUtils.isBlank(newPwd) || md5Length != newPwd.length()) {
            return ResultUtils.error("新密码错误");
        }
        Long userId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        SysUser sysUser = baseFindByClassAndId(SysUser.class, userId);
        if (!Md5Utils.encode(oldPwd).equals(sysUser.getPassword())) {
            return ResultUtils.error("原密码错误");
        }
        SysUser newUser = new SysUser()
                .setId(sysUser.getId())
                .setPassword(Md5Utils.encode(newPwd));
        baseUpdate(newUser);
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
        //md5长度是32位
        int md5Length = 32;
        if (ToolUtils.isBlank(password) || md5Length != password.length()) {
            return ResultUtils.error("密码格式不正确");
        }
        Long nowUserId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        //非超级管理员重置超级管理员密码，强制用户下线
        if (GlobalConfig.SUPER_ADMIN_ID.equals(userId) && !GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
        }
        SysUser sysUser = new SysUser()
                .setId(userId)
                .setPassword(Md5Utils.encode(password));
        baseUpdate(sysUser);
        return ResultUtils.success();
    }

    /**
     * 修改头像
     *
     * @param request
     * @param file
     * @return
     */
    public ResultUtils updateIcon(HttpServletRequest request, MultipartFile file) {
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
        String path = FileUtils.upFile(file, "user/icon");
        if (null == path) {
            return ResultUtils.error("文件上传失败，请重试");
        }
        Long userId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        SysUser sysUser = baseFindByClassAndId(SysUser.class, userId);
        //异步删除原来的图片
        systemAsync.deleteFile(sysUser.getIcon());
        sysUser.setIcon(path);
        baseUpdate(sysUser);
        return ResultUtils.success(GlobalConfig.getMybatisFilePathPrefix() + path);
    }
}
