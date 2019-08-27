package com.weiziplus.springboot.controller.pc.system;

import com.weiziplus.springboot.interceptor.AdminAuthToken;
import com.weiziplus.springboot.interceptor.SystemLog;
import com.weiziplus.springboot.service.system.SysAbnormalIpService;
import com.weiziplus.springboot.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author wanglongwei
 * @data 2019/8/5 14:35
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc/sysAbnormalIp")
public class SysAbnormalIpController {

    @Autowired
    SysAbnormalIpService service;

    @GetMapping("/getPageList")
    @SystemLog(description = "查看异常ip列表")
    public ResultUtils getPageList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return service.getPageList(pageNum, pageSize);
    }
}
