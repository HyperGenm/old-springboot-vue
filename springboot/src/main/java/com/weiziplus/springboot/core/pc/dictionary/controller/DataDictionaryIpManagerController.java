package com.weiziplus.springboot.core.pc.dictionary.controller;

import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.interceptor.SystemLog;
import com.weiziplus.springboot.common.models.DataDictionaryValue;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.core.pc.dictionary.service.DataDictionaryIpManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ip管理
 *
 * @author wanglongwei
 * @date 2020/02/24 19/57
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc/dataDictionary/ipManager")
public class DataDictionaryIpManagerController {

    @Autowired
    DataDictionaryIpManagerService service;

    @GetMapping("/getIpRole")
    @SystemLog(description = "查看ip规则")
    public ResultUtils<String> getIpRole() {
        return service.getIpRole();
    }

    @PostMapping("/updateIpRole")
    @SystemLog(description = "更新ip规则", type = SystemLog.TYPE_UPDATE)
    public ResultUtils updateIpRole(HttpServletRequest request, String role) {
        return service.updateIpRole(request, role);
    }

    @GetMapping("/getIpList")
    @SystemLog(description = "查看ip名单")
    public ResultUtils<List<DataDictionaryValue>> getIpList(
            @RequestParam(value = "ipAddress", required = false) String ipAddress,
            @RequestParam(value = "type", required = false) String type) {
        return service.getIpList(ipAddress, type);
    }

    @PostMapping("/addIp")
    @SystemLog(description = "新增ip", type = SystemLog.TYPE_INSERT)
    public ResultUtils addIp(HttpServletRequest request, String type, String ipAddress, String remark) {
        return service.addIp(request, type, ipAddress, remark);
    }

    @PostMapping("/deleteIp")
    @SystemLog(description = "删除ip", type = SystemLog.TYPE_DELETE)
    public ResultUtils deleteIp(HttpServletRequest request, Integer id) {
        return service.deleteIp(request, id);
    }

}
