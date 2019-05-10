package com.weiziplus.springboot.api.controller;

import com.weiziplus.springboot.api.service.LoginService;
import com.weiziplus.springboot.common.interceptor.WebAuthToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/10 17:02
 */
@RestController
@RequestMapping("/api")
@Api(tags = "登陆接口")
public class LoginController {
    @Autowired
    LoginService service;

    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", required = true, paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", required = true, paramType = "form")
    })
    @PostMapping("/login")
    public Map<String, Object> login(String username, String password) {
        return service.login(username, password);
    }

    @ApiOperation(value = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", required = true, paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", required = true, paramType = "form")
    })
    @PostMapping("/register")
    public Map<String, Object> register(String username, String password) {
        return service.register(username, password);
    }

    @WebAuthToken
    @ApiOperation(value = "退出")
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        return service.logout(request);
    }
}
