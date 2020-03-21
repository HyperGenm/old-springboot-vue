package com.weiziplus.springboot.core.pc.system.controller;

import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.interceptor.SysUserLog;
import com.weiziplus.springboot.common.models.SysError;
import com.weiziplus.springboot.common.util.PageUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.core.pc.system.service.SysErrorService;
import com.weiziplus.springboot.core.pc.system.vo.SysLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/03/21 15/12
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc/sysError")
public class SysErrorController {

    @Autowired
    SysErrorService service;

    @GetMapping("/getPageList")
    @SysUserLog(description = "查看系统异常")
    public ResultUtils<PageUtils<List<SysError>>> getPageList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "createTime", required = false) String createTime) {
        return service.getPageList(pageNum, pageSize, createTime);
    }

}
