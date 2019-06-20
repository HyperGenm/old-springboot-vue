package com.weiziplus.springboot.pc.system.controller;

import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.interceptor.SystemLog;
import com.weiziplus.springboot.common.models.SysRole;
import com.weiziplus.springboot.common.utils.ResultUtil;
import com.weiziplus.springboot.pc.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/10 8:40
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc/sysRole")
public class SysRoleController {

    @Autowired
    SysRoleService service;

    /**
     * 获取权限树形列表
     *
     * @return
     */
    @GetMapping("/getRoleTree")
    @SystemLog(description = "查看角色树")
    public Map getRoleTree() {
        return ResultUtil.success(service.getRoleTree());
    }

    /**
     * 获取权限列表
     *
     * @return
     */
    @GetMapping("/getRoleList")
    @SystemLog(description = "查看角色列表")
    public Map getRoleList() {
        return ResultUtil.success(service.getRoleList());
    }

    /**
     * 根据角色id获取功能列表
     *
     * @param roleId
     * @return
     */
    @GetMapping("/getRoleFunList")
    public Map getRoleFunList(Long roleId) {
        return ResultUtil.success(service.getRoleFunList(roleId));
    }

    /**
     * 新增角色功能
     *
     * @param roleId
     * @param funIds
     * @return
     */
    @PostMapping("/addRoleFun")
    @SystemLog(description = "新增角色功能")
    public Map addRoleFun(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "funIds", defaultValue = "") Long[] funIds) {
        return service.addRoleFun(roleId, funIds);
    }

    /**
     * 新增角色
     *
     * @param sysRole
     * @return
     */
    @PostMapping("/addRole")
    @SystemLog(description = "新增角色")
    public Map addRole(SysRole sysRole) {
        return service.addRole(sysRole);
    }

    /**
     * 修改角色
     *
     * @param sysRole
     * @return
     */
    @PostMapping("/updateRole")
    @SystemLog(description = "修改角色")
    public Map updateRole(SysRole sysRole) {
        return service.updateRole(sysRole);
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @PostMapping("/deleteRole")
    @SystemLog(description = "删除角色")
    public Map deleteRole(Long roleId) {
        return service.deleteRole(roleId);
    }

    /**
     * 改变角色状态
     *
     * @param roleId
     * @return
     */
    @PostMapping("/changeRoleIsStop")
    @SystemLog(description = "改变角色状态")
    public Map changeRoleIsStop(Long roleId, Integer isStop) {
        return service.changeRoleIsStop(roleId, isStop);
    }
}
