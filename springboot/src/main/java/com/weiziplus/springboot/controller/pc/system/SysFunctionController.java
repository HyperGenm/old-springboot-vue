package com.weiziplus.springboot.controller.pc.system;

import com.weiziplus.springboot.interceptor.AdminAuthToken;
import com.weiziplus.springboot.interceptor.SystemLog;
import com.weiziplus.springboot.models.SysFunction;
import com.weiziplus.springboot.service.system.SysFunctionService;
import com.weiziplus.springboot.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wanglongwei
 * @date 2019/5/10 8:23
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc/sysFunction")
public class SysFunctionController {

    @Autowired
    SysFunctionService service;

    /**
     * 获取所有功能树形分页类型数据
     *
     * @return
     */
    @GetMapping("/getAllFunctionTreePageList")
    public ResultUtils getAllFunctionTreePageList() {
        return service.getAllFunctionTreePageList();
    }

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
     * 新增功能
     *
     * @param sysFunction
     * @return
     */
    @PostMapping("/addFunction")
    @SystemLog(description = "新增功能")
    public ResultUtils addFunction(HttpServletRequest request, SysFunction sysFunction) {
        return service.addFunction(request, sysFunction);
    }

    /**
     * 修改功能
     *
     * @param sysFunction
     * @return
     */
    @PostMapping("/updateFunction")
    @SystemLog(description = "修改功能")
    public ResultUtils updateFunction(HttpServletRequest request, SysFunction sysFunction) {
        return service.updateFunction(request, sysFunction);
    }

    /**
     * 删除功能
     *
     * @param ids
     * @return
     */
    @PostMapping("/deleteFunction")
    @SystemLog(description = "删除功能")
    public ResultUtils deleteFunction(HttpServletRequest request, Long[] ids) {
        return service.deleteFunction(request, ids);
    }
}
