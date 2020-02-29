package com.weiziplus.springboot.core.pc.tools.controller;

import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.core.pc.tools.service.ToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wanglongwei
 * @date 2019/8/24 16:46
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc/tools")
public class ToolsController {

    @Autowired
    ToolsService service;

    /**
     * 文件上传
     *
     * @return
     */
    @PostMapping("/upload")
    public ResultUtils upload(MultipartFile file) {
        return service.upload(file);
    }

    /**
     * 文件下载
     *
     * @return
     */
    @PostMapping("/downTemp")
    public void downTemp(HttpServletResponse response) {
        service.downTemp(response);
    }
}
