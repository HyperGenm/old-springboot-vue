package com.weiziplus.springboot.controller.pc.system;

import com.weiziplus.springboot.interceptor.AdminAuthToken;
import com.weiziplus.springboot.interceptor.SystemLog;
import com.weiziplus.springboot.utils.ResultUtil;
import com.weiziplus.springboot.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author wanglongwei
 * @data 2019/5/13 15:33
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc/sysLog")
public class SysLogController {
    @Autowired
    SysLogService service;

    @GetMapping("/getLogList")
    @SystemLog(description = "查看系统日志")
    public ResultUtil getPageList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "ipAddress", required = false) String ipAddress,
            @RequestParam(value = "createTime", required = false) String createTime) {
        return service.getPageList(pageNum, pageSize, username, roleId, description, ipAddress, createTime);
    }
}
