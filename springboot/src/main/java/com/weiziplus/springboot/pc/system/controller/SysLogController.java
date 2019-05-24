package com.weiziplus.springboot.pc.system.controller;

import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.interceptor.SystemLog;
import com.weiziplus.springboot.common.utils.ResultUtil;
import com.weiziplus.springboot.pc.system.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/13 15:33
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc")
public class SysLogController {
    @Autowired
    SysLogService service;

    @GetMapping("/getLogList")
    @SystemLog(description = "查看系统日志")
    public Map<String, Object> getLogList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "createTime", required = false) String createTime,
            @RequestParam(value = "description", required = false) String description) {
        return ResultUtil.success(service.getLogList(pageNum, pageSize, username, roleId, createTime,description));
    }
}
