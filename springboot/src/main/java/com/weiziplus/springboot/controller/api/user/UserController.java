package com.weiziplus.springboot.controller.api.user;

import com.weiziplus.springboot.interceptor.AuthTokenIgnore;
import com.weiziplus.springboot.interceptor.WebAuthToken;
import com.weiziplus.springboot.models.User;
import com.weiziplus.springboot.service.user.UserService;
import com.weiziplus.springboot.util.PageUtils;
import com.weiziplus.springboot.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @ApiOperation(value = "获取用户列表--示例---不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "Integer", required = true, defaultValue = "1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "Integer", required = true, defaultValue = "10", paramType = "query"),
    })
    @GetMapping("/getUserList")
    @AuthTokenIgnore
    public ResultUtils<PageUtils<List<User>>> getUserList(Integer pageNum, Integer pageSize) {
        return service.getUserList(pageNum, pageSize);
    }

    @ApiOperation(value = "删除redis缓存---示例")
    @PostMapping("/deleteRedis")
    @AuthTokenIgnore
    public ResultUtils deleteRedis() {
        return service.deleteRedis();
    }
}
