package com.weiziplus.springboot.core.pc.system.controller;

import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.interceptor.SystemLog;
import com.weiziplus.springboot.common.models.SysUser;
import com.weiziplus.springboot.core.pc.system.service.SysUserService;
import com.weiziplus.springboot.common.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wanglongwei
 * @date 2019/5/10 9:01
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc/sysUser")
public class SysUserController {

    @Autowired
    SysUserService service;

    /**
     * 获取用户列表
     *
     * @param pageNum
     * @param pageSize
     * @param userName
     * @param roleId
     * @param allowLogin
     * @param lastActiveTime
     * @param createTime
     * @return
     */
    @GetMapping("/getPageList")
    @SystemLog(description = "查看用户列表")
    public ResultUtils getPageList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "allowLogin", required = false) Integer allowLogin,
            @RequestParam(value = "lastActiveTime", required = false) String lastActiveTime,
            @RequestParam(value = "createTime", required = false) String createTime) {
        return service.getPageList(pageNum, pageSize, userName, roleId, allowLogin, lastActiveTime, createTime);
    }

    /**
     * 新增用户
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/addUser")
    @SystemLog(description = "新增用户")
    public ResultUtils addUser(SysUser sysUser) {
        return service.addUser(sysUser);
    }

    /**
     * 更新用户
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/updateUser")
    @SystemLog(description = "更新用户")
    public ResultUtils updateUser(HttpServletRequest request, SysUser sysUser) {
        return service.updateUser(request, sysUser);
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @PostMapping("/deleteUser")
    @SystemLog(description = "删除用户")
    public ResultUtils deleteUser(
            HttpServletRequest request,
            @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
        return service.deleteUser(request, ids);
    }

    /**
     * 更新用户角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    @PostMapping("/updateUserRole")
    @SystemLog(description = "更新用户角色")
    public ResultUtils updateUserRole(HttpServletRequest request, Long userId, Long roleId) {
        return service.updateUserRole(request, userId, roleId);
    }

    /**
     * 修改密码
     *
     * @param request
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @PostMapping("/updatePassword")
    @SystemLog(description = "修改密码")
    public ResultUtils updatePassword(HttpServletRequest request, String oldPwd, String newPwd) {
        return service.updatePassword(request, oldPwd, newPwd);
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetUserPassword")
    @SystemLog(description = "重置用户密码")
    public ResultUtils resetUserPassword(HttpServletRequest request, Long userId, String password) {
        return service.resetUserPassword(request, userId, password);
    }

    /**
     * 解除封号
     */
    @PostMapping("/relieveSuspend")
    @SystemLog(description = "解除封号")
    public ResultUtils relieveSuspend(HttpServletRequest request, Long userId) {
        return service.relieveSuspend(request, userId);
    }

    /**
     * 修改头像
     *
     * @return
     */
    @PostMapping("/updateIcon")
    @SystemLog(description = "修改头像")
    public ResultUtils updateIcon(HttpServletRequest request, MultipartFile file) {
        return service.updateIcon(request, file);
    }
}
