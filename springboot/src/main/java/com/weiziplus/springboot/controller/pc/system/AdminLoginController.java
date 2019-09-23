package com.weiziplus.springboot.controller.pc.system;

import com.weiziplus.springboot.interceptor.AdminAuthToken;
import com.weiziplus.springboot.interceptor.SystemLog;
import com.weiziplus.springboot.util.ResultUtils;
import com.weiziplus.springboot.service.system.AdminLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/getValidateCode")
    public void getValidateCode(HttpServletRequest request, HttpServletResponse response) {
        service.getValidateCode(request, response);
    }

    /**
     * 系统用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    @SystemLog(description = "系统用户登录")
    public ResultUtils login(HttpServletRequest request, HttpSession session, String username, String password, String code) {
        return service.login(request, session, username, password, code);
    }

    /**
     * 系统用户退出登录
     *
     * @param request
     * @return
     */
    @AdminAuthToken
    @GetMapping("/logout")
    @SystemLog(description = "系统用户退出登录")
    public ResultUtils logout(HttpServletRequest request) {
        return service.logout(request);
    }
}
