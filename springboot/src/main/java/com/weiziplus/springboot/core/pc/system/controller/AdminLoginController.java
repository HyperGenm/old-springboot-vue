package com.weiziplus.springboot.core.pc.system.controller;

import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.interceptor.SysUserLog;
import com.weiziplus.springboot.core.pc.system.service.AdminLoginService;
import com.weiziplus.springboot.common.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wanglongwei
 * @date 2019/5/9 11:07
 */
@RestController
@ApiIgnore
@RequestMapping("/pc")
public class AdminLoginController {

    @Autowired
    AdminLoginService service;

    /**
     * 生成验证码
     *
     * @param response
     * @param uuid
     */
    @GetMapping("/getValidateCode")
    public void getValidateCode(HttpServletResponse response, String uuid) {
        service.getValidateCode(response, uuid);
    }

    /**
     * 系统用户登录
     *
     * @param request
     * @param username
     * @param password
     * @param code
     * @param uuid
     * @return
     */
    @PostMapping("/login")
    @SysUserLog(description = "系统用户登录")
    public ResultUtils login(HttpServletRequest request, String username, String password, String code, String uuid) {
        return service.login(request, username, password, code, uuid);
    }

    /**
     * 系统用户退出登录
     *
     * @param request
     * @return
     */
    @AdminAuthToken
    @GetMapping("/logout")
    @SysUserLog(description = "系统用户退出登录")
    public ResultUtils logout(HttpServletRequest request) {
        return service.logout(request);
    }
}
