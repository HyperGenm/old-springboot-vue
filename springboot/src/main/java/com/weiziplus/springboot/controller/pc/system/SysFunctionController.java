package com.weiziplus.springboot.controller.pc.system;

import com.weiziplus.springboot.interceptor.AdminAuthToken;
import com.weiziplus.springboot.interceptor.SystemLog;
import com.weiziplus.springboot.models.SysFunction;
import com.weiziplus.springboot.util.ResultUtils;
import com.weiziplus.springboot.service.system.SysFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author wanglongwei
 * @data 2019/5/10 8:23
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc/sysFunction")
public class SysFunctionController {
    @Autowired
    SysFunctionService service;

    /**
     * 获取所有功能树形列表
     *
     * @return
     */
    @GetMapping("/getAllFunctionTree")
    @SystemLog(description = "查看功能列表")
    public ResultUtils getAllFunctionTree() {
        return ResultUtils.success(service.getFunTree());
    }

    /**
     * 获取所有功能树形列表
     *
     * @return
     */
    @GetMapping("/getAllFunctionTreeNotButton")
    @SystemLog(description = "查看功能列表")
    public ResultUtils getAllFunctionTreeNotButton() {
        return ResultUtils.success(service.getAllFunctionTreeNotButton());
    }

    /**
     * 根据角色id获取功能列表
     *
     * @param roleId
     * @return
     */
    @GetMapping("/getRoleFunList")
    public ResultUtils getRoleFunList(Long roleId) {
        return ResultUtils.success(service.getRoleFunList(roleId));
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
    public ResultUtils getFunctionList(
            @RequestParam(value = "parentId", defaultValue = "0") Long parentId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return service.getFunctionListByParentId(parentId, pageNum, pageSize);
    }

    /**
     * 新增功能
     *
     * @param sysFunction
     * @return
     */
    @PostMapping("/addFunction")
    @SystemLog(description = "新增功能")
    public ResultUtils addFunction(SysFunction sysFunction) {
        return service.addFunction(sysFunction);
    }

    /**
     * 修改功能
     *
     * @param sysFunction
     * @return
     */
    @PostMapping("/updateFunction")
    @SystemLog(description = "修改功能")
    public ResultUtils updateFunction(SysFunction sysFunction) {
        return service.updateFunction(sysFunction);
    }

    /**
     * 删除功能
     *
     * @param ids
     * @return
     */
    @PostMapping("/deleteFunction")
    @SystemLog(description = "删除功能")
    public ResultUtils deleteFunction(Long[] ids) {
        return service.deleteFunction(ids);
    }
}
