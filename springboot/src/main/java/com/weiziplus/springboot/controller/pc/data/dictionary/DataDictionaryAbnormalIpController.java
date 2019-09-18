package com.weiziplus.springboot.controller.pc.data.dictionary;

import com.weiziplus.springboot.interceptor.AdminAuthToken;
import com.weiziplus.springboot.interceptor.SystemLog;
import com.weiziplus.springboot.service.data.dictionary.DataDictionaryAbnormalIpService;
import com.weiziplus.springboot.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author wanglongwei
 * @data 2019/8/5 10:17
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc/dataDictionary/abnormalIp")
public class DataDictionaryAbnormalIpController {

    @Autowired
    DataDictionaryAbnormalIpService service;

    @GetMapping("/getPageList")
    @SystemLog(description = "查看异常ip列表")
    public ResultUtils getPageList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return service.getPageList(pageNum, pageSize);
    }
}
