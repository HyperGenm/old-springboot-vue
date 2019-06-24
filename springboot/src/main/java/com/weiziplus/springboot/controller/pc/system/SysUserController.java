package com.weiziplus.springboot.controller.pc.system;

import com.weiziplus.springboot.interceptor.AdminAuthToken;
import com.weiziplus.springboot.interceptor.SystemLog;
import com.weiziplus.springboot.models.SysUser;
import com.weiziplus.springboot.utils.ResultUtil;
import com.weiziplus.springboot.service.system.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wanglongwei
 * @data 2019/5/10 9:01
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
     * @param allowLogin
     * @return
     */
    @GetMapping("/getUserList")
    @SystemLog(description = "查看用户列表")
    public ResultUtil getUserList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "allowLogin", required = false) Integer allowLogin,
            @RequestParam(value = "createTime", required = false) String createTime) {
        return service.getUserList(pageNum, pageSize, userName, allowLogin, createTime);
    }

    /**
     * 新增用户
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/addUser")
    @SystemLog(description = "新增用户")
    public ResultUtil addUser(SysUser sysUser) {
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
    public ResultUtil updateUser(SysUser sysUser) {
        return service.updateUser(sysUser);
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @PostMapping("/deleteUser")
    @SystemLog(description = "删除用户")
    public ResultUtil deleteUser(
            @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
        return service.deleteUser(ids);
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
    public ResultUtil updateUserRole(Long userId, Long roleId) {
        return service.updateUserRole(userId, roleId);
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
    public ResultUtil updatePassword(HttpServletRequest request, String oldPwd, String newPwd) {
        return service.updatePassword(request, oldPwd, newPwd);
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetUserPassword")
    @SystemLog(description = "重置用户密码")
    public ResultUtil resetUserPassword(HttpServletRequest request, Long userId, String password) {
        return service.resetUserPassword(request, userId, password);
    }
}
