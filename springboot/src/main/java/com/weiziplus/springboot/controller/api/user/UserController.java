package com.weiziplus.springboot.controller.api.user;

import com.weiziplus.springboot.models.User;
import com.weiziplus.springboot.service.user.UserService;
import com.weiziplus.springboot.interceptor.WebAuthToken;
import com.weiziplus.springboot.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wanglongwei
 * @date 2019/5/10 17:10
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
    public ResultUtils<User> getInfo(HttpServletRequest request) {
        return service.getInfo(request);
    }
}
