package com.weiziplus.springboot.core.pc.system.controller;

import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.interceptor.SysUserLog;
import com.weiziplus.springboot.common.util.PageUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.core.pc.system.service.UserLogService;
import com.weiziplus.springboot.core.pc.system.vo.LogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2019/5/13 15:33
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc/userLog")
public class UserLogController {

    @Autowired
    UserLogService service;

    @GetMapping("/getPageList")
    @SysUserLog(description = "查看用户系统日志")
    public ResultUtils<PageUtils<List<LogVo>>> getPageList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "ipAddress", required = false) String ipAddress,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        return service.getPageList(pageNum, pageSize, username, description, type, ipAddress, startTime, endTime);
    }
}
