package com.weiziplus.springboot.core.pc.system.controller;

import com.weiziplus.springboot.common.interceptor.AdminAuthToken;
import com.weiziplus.springboot.common.interceptor.SysUserLog;
import com.weiziplus.springboot.common.models.SysRole;
import com.weiziplus.springboot.common.util.PageUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.core.pc.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2019/5/10 8:40
 */
@RestController
@ApiIgnore
@AdminAuthToken
@RequestMapping("/pc/sysRole")
public class SysRoleController {

    @Autowired
    SysRoleService service;

    /**
     * 查看角色树数据
     *
     * @return
     */
    @GetMapping("/getAllRoleTreePageList")
    @SysUserLog(description = "查看角色树数据")
    public ResultUtils<PageUtils<SysRole>> getAllRoleTreePageList() {
        return service.getAllRoleTreePageList();
    }

    /**
     * 获取角色树形列表
     *
     * @return
     */
    @GetMapping("/getRoleTree")
    @SysUserLog(description = "查看角色树")
    public ResultUtils<List<SysRole>> getRoleTree() {
        return service.getRoleTree();
    }

    /**
     * 获取角色列表
     *
     * @return
     */
    @GetMapping("/getRoleList")
    @SysUserLog(description = "查看角色列表")
    public ResultUtils<List<SysRole>> getRoleList() {
        return service.getRoleList();
    }

    /**
     * 新增角色功能
     *
     * @param roleId
     * @param funIds
     * @return
     */
    @PostMapping("/addRoleFun")
    @SysUserLog(description = "新增角色功能", type = SysUserLog.TYPE_INSERT)
    public ResultUtils addRoleFun(
            @RequestParam(value = "roleId") Integer roleId,
            @RequestParam(value = "funIds", defaultValue = "") Integer[] funIds) {
        return service.addRoleFun(roleId, funIds);
    }

    /**
     * 新增角色
     *
     * @param sysRole
     * @return
     */
    @PostMapping("/addRole")
    @SysUserLog(description = "新增角色", type = SysUserLog.TYPE_INSERT)
    public ResultUtils addRole(SysRole sysRole) {
        return service.addRole(sysRole);
    }

    /**
     * 修改角色
     *
     * @param sysRole
     * @return
     */
    @PostMapping("/updateRole")
    @SysUserLog(description = "修改角色", type = SysUserLog.TYPE_UPDATE)
    public ResultUtils updateRole(HttpServletRequest request, SysRole sysRole) {
        return service.updateRole(request, sysRole);
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @PostMapping("/deleteRole")
    @SysUserLog(description = "删除角色", type = SysUserLog.TYPE_DELETE)
    public ResultUtils deleteRole(HttpServletRequest request, Integer roleId) {
        return service.deleteRole(request, roleId);
    }

    /**
     * 改变角色状态
     *
     * @param roleId
     * @return
     */
    @PostMapping("/changeRoleIsStop")
    @SysUserLog(description = "改变角色状态", type = SysUserLog.TYPE_UPDATE)
    public ResultUtils changeRoleIsStop(HttpServletRequest request, Integer roleId, Integer isStop) {
        return service.changeRoleIsStop(request, roleId, isStop);
    }
}
