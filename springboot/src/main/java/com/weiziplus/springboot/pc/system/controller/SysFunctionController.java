package com.weiziplus.springboot.pc.system.controller;

import com.weiziplus.springboot.pc.system.service.SysFunctionService;
import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.models.SysFunction;
import com.weiziplus.springboot.common.utils.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/10 8:23
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc")
public class SysFunctionController {
    @Autowired
    SysFunctionService service;

    /**
     * 获取所有功能树形列表
     *
     * @return
     */
    @GetMapping("/getAllFunctionTree")
    public Map<String, Object> getAllFunctionTree() {
        return ResponseBean.success(service.getFunTree());
    }

    /**
     * 根据parentId获取功能列表
     *
     * @param parentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/getFunctionList")
    public Map<String, Object> getFunctionList(
            @RequestParam(value = "parentId", defaultValue = "0L") Long parentId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return ResponseBean.success(service.getFunctionListByParentId(parentId, pageNum, pageSize));
    }

    /**
     * 新增功能
     *
     * @param sysFunction
     * @return
     */
    @PostMapping("/addFunction")
    public Map<String, Object> addFunction(SysFunction sysFunction) {
        return service.addFunction(sysFunction);
    }

    /**
     * 修改功能
     *
     * @param sysFunction
     * @return
     */
    @PostMapping("/updateFunction")
    public Map<String, Object> updateFunction(SysFunction sysFunction) {
        return service.updateFunction(sysFunction);
    }

    /**
     * 删除功能
     *
     * @param ids
     * @return
     */
    @PostMapping("/deleteFunction")
    public Map<String, Object> deleteFunction(Long[] ids) {
        return service.deleteFunction(ids);
    }
}
