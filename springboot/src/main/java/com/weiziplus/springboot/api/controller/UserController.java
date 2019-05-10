package com.weiziplus.springboot.api.controller;

import com.weiziplus.springboot.api.service.UserService;
import com.weiziplus.springboot.common.interceptor.WebAuthToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/10 17:10
 */
@WebAuthToken
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理")
public class UserController {
    @Autowired
    UserService service;

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getInfo")
    public Map<String, Object> getInfo(HttpServletRequest request) {
        return service.getInfo(request);
    }
}
