package com.weiziplus.springboot.controller.pc.system;

import com.weiziplus.springboot.interceptor.AdminAuthToken;
import com.weiziplus.springboot.interceptor.SystemLog;
import com.weiziplus.springboot.models.SysRole;
import com.weiziplus.springboot.service.system.SysRoleService;
import com.weiziplus.springboot.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

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
     * 获取角色树形列表
     *
     * @return
     */
    @GetMapping("/getRoleTree")
    @SystemLog(description = "查看角色树")
    public ResultUtils getRoleTree() {
        return ResultUtils.success(service.getRoleTree());
    }

    /**
     * 获取角色列表
     *
     * @return
     */
    @GetMapping("/getRoleList")
    @SystemLog(description = "查看角色列表")
    public ResultUtils getRoleList() {
        return ResultUtils.success(service.getRoleList());
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
    public ResultUtils addRoleFun(
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
    @SystemLog(description = "修改角色")
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
    @SystemLog(description = "删除角色")
    public ResultUtils deleteRole(HttpServletRequest request, Long roleId) {
        return service.deleteRole(request, roleId);
    }

    /**
     * 改变角色状态
     *
     * @param roleId
     * @return
     */
    @PostMapping("/changeRoleIsStop")
    @SystemLog(description = "改变角色状态")
    public ResultUtils changeRoleIsStop(HttpServletRequest request, Long roleId, Integer isStop) {
        return service.changeRoleIsStop(request, roleId, isStop);
    }
}
