package com.weiziplus.springboot.core.pc.system.controller;

import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.interceptor.SysUserLog;
import com.weiziplus.springboot.common.models.SysFunction;
import com.weiziplus.springboot.common.util.PageUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.core.pc.system.service.SysFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    @SysUserLog(description = "查看功能树形数据")
    public ResultUtils<PageUtils<List<SysFunction>>> getAllFunctionTreePageList() {
        return service.getAllFunctionTreePageList();
    }

    /**
     * 获取所有功能树形列表
     *
     * @return
     */
    @GetMapping("/getAllFunctionTree")
    @SysUserLog(description = "查看功能列表")
    public ResultUtils<List<SysFunction>> getAllFunctionTree() {
        return service.getFunTree();
    }

    /**
     * 获取所有功能树形列表
     *
     * @return
     */
    @GetMapping("/getAllFunctionTreeNotButton")
    @SysUserLog(description = "查看功能列表")
    public ResultUtils<List<SysFunction>> getAllFunctionTreeNotButton() {
        return service.getAllFunctionTreeNotButton();
    }

    /**
     * 根据角色id获取功能列表
     *
     * @param roleId
     * @return
     */
    @GetMapping("/getRoleFunList")
    public ResultUtils<List<Integer>> getRoleFunList(Integer roleId) {
        return service.getRoleFunList(roleId);
    }

    /**
     * 新增功能
     *
     * @param sysFunction
     * @return
     */
    @PostMapping("/addFunction")
    @SysUserLog(description = "新增功能", type = SysUserLog.TYPE_INSERT)
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
    @SysUserLog(description = "修改功能", type = SysUserLog.TYPE_UPDATE)
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
    @SysUserLog(description = "删除功能", type = SysUserLog.TYPE_DELETE)
    public ResultUtils deleteFunction(HttpServletRequest request, Integer[] ids) {
        return service.deleteFunction(request, ids);
    }
}