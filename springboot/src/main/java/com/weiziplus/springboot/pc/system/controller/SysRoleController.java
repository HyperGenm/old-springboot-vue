package com.weiziplus.springboot.pc.system.controller;

import com.weiziplus.springboot.pc.system.service.SysRoleService;
import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.models.SysRole;
import com.weiziplus.springboot.common.utils.ResponseBean;
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
@RequestMapping("/pc")
public class SysRoleController {

    @Autowired
    SysRoleService service;

    /**
     * 获取权限树形列表
     *
     * @return
     */
    @GetMapping("/getRoleTree")
    public Map getRoleTree() {
        return ResponseBean.success(service.getRoleTree());
    }

    /**
     * 根据角色id获取功能列表
     *
     * @param roleId
     * @return
     */
    @GetMapping("/getRoleFunList")
    public Map getRoleFunList(Long roleId) {
        return ResponseBean.success(service.getRoleFunList(roleId));
    }

    /**
     * 新增角色功能
     *
     * @param roleId
     * @param funIds
     * @return
     */
    @PostMapping("/addRoleFun")
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
    public Map changeRoleIsStop(Long roleId, Integer isStop) {
        return service.changeRoleIsStop(roleId, isStop);
    }
}
