package com.weiziplus.springboot.pc.system.controller;

import com.weiziplus.springboot.pc.system.service.SysUserService;
import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.models.SysUser;
import com.weiziplus.springboot.common.utils.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/10 9:01
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc")
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
    public Map getUserList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "allowLogin", required = false) Integer allowLogin,
            @RequestParam(value = "createTime", required = false) String createTime) {
        return ResponseBean.success(service.getUserList(pageNum, pageSize, userName, allowLogin, createTime));
    }

    /**
     * 新增用户
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/addUser")
    public Map addUser(SysUser sysUser) {
        return service.addUser(sysUser);
    }

    /**
     * 更新用户
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/updateUser")
    public Map updateUser(SysUser sysUser) {
        return service.updateUser(sysUser);
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @PostMapping("/deleteUser")
    public Map deleteUser(
            @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
        return service.deleteUser(ids);
    }

    /**
     * 新增用户角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    @PostMapping("/addUserRole")
    public Map addUserRole(Long userId, Long roleId) {
        return service.addUserRole(userId, roleId);
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetUserPassword")
    public Map<String, Object> resetUserPassword(HttpServletRequest request, Long userId, String password) {
        return service.resetUserPassword(request, userId, password);
    }
}
