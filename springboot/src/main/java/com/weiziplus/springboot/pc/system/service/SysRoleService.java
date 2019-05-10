package com.weiziplus.springboot.pc.system.service;

import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.models.SysFunction;
import com.weiziplus.springboot.common.models.SysRole;
import com.weiziplus.springboot.common.utils.ResponseBean;
import com.weiziplus.springboot.common.utils.ValidateUtil;
import com.weiziplus.springboot.pc.system.mapper.SysFunctionMapper;
import com.weiziplus.springboot.pc.system.mapper.SysRoleFunctionMapper;
import com.weiziplus.springboot.pc.system.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/10 8:39
 */
@Service
public class SysRoleService {
    @Autowired
    SysRoleMapper mapper;

    @Autowired
    SysFunctionMapper sysFunctionMapper;

    @Autowired
    SysRoleFunctionMapper sysRoleFunctionMapper;

    /**
     * 获取权限树形结构
     *
     * @return
     */
    public List<SysRole> getRoleTree() {
        List<SysRole> resultList = new ArrayList<>();
        List<SysRole> menuList = mapper.getRoleListByParentId(0L);
        for (SysRole sysRole : menuList) {
            sysRole.setChildren(findChildren(sysRole));
            resultList.add(sysRole);
        }
        return resultList;
    }

    /**
     * 获取子级权限树形结构
     *
     * @param sysRole
     * @return
     */
    private List<SysRole> findChildren(SysRole sysRole) {
        List<SysRole> childrenList = new ArrayList<>();
        List<SysRole> menuList = mapper.getRoleListByParentId(sysRole.getId());
        for (SysRole sys : menuList) {
            sys.setChildren(findChildren(sys));
            childrenList.add(sys);
        }
        return childrenList;
    }

    /**
     * 根据角色id获取功能id列表
     *
     * @param roleId
     * @return
     */
    public List<Long> getRoleFunList(Long roleId) {
        List<Long> resultList = new ArrayList<>();
        List<SysFunction> list = sysFunctionMapper.getFunListByRoleId(roleId);
        for (SysFunction sysFunction : list) {
            resultList.add(sysFunction.getId());
        }
        return resultList;
    }

    /**
     * 新增角色功能
     *
     * @param roleId
     * @param funIds
     * @return
     */
    public Map<String, Object> addRoleFun(Long roleId, Long[] funIds) {
        if (null == roleId || 0 >= roleId) {
            return ResponseBean.error("roleId不能为空");
        }
        sysRoleFunctionMapper.deleteByRoleId(roleId);
        if (null == funIds || 0 >= funIds.length) {
            return ResponseBean.success("success");
        }
        return ResponseBean.success(sysRoleFunctionMapper.addRoleFunction(roleId, funIds));
    }

    /**
     * 新增角色
     *
     * @param sysRole
     * @return
     */
    public Map<String, Object> addRole(SysRole sysRole) {
        if (ValidateUtil.notUsername(sysRole.getName())) {
            return ResponseBean.error("角色名不能包含特殊字符");
        }
        //因为超级管理员只有一个所以角色父级id从1开始
        if (null == sysRole.getParentId() || GlobalConfig.SUPER_ADMIN_ID > sysRole.getParentId()) {
            return ResponseBean.error("parentId不能为空");
        }
        SysRole role = mapper.getRoleInfoByName(sysRole.getName());
        if (null != role && null != role.getId()) {
            return ResponseBean.error("角色名已存在");
        }
        SysRole superRole = mapper.getInfoByRoleId(sysRole.getParentId());
        if (!GlobalConfig.IS_STOP.equals(superRole.getIsStop())) {
            return ResponseBean.error("操作失败，父级处于禁用状态");
        }
        return ResponseBean.success(mapper.addRole(sysRole));
    }

    /**
     * 修改角色
     *
     * @param sysRole
     * @return
     */
    public Map<String, Object> updateRole(SysRole sysRole) {
        if (ValidateUtil.notUsername(sysRole.getName())) {
            return ResponseBean.error("角色名不能包含特殊字符");
        }
        SysRole role = mapper.getRoleInfoByName(sysRole.getName());
        if (null != role && null != role.getId() && !role.getId().equals(sysRole.getId())) {
            return ResponseBean.error("角色名已存在");
        }
        if (!GlobalConfig.IS_STOP.equals(sysRole.getIsStop())) {
            return ResponseBean.error("操作失败，角色处于禁用状态");
        }
        SysRole superRole = mapper.getInfoByRoleId(sysRole.getParentId());
        if (!GlobalConfig.IS_STOP.equals(superRole.getIsStop())) {
            return ResponseBean.error("操作失败，父级处于禁用状态");
        }
        return ResponseBean.success(mapper.updateRole(sysRole));
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    public Map<String, Object> deleteRole(Long roleId) {
        List<SysRole> list = mapper.getRoleListByParentId(roleId);
        if (null != list && 0 < list.size()) {
            return ResponseBean.error("当前角色存在下级");
        }
        return ResponseBean.success(mapper.deleteRoleByRoleId(roleId));
    }

    /**
     * 改变角色状态
     *
     * @param roleId
     * @return
     */
    public Map<String, Object> changeRoleIsStop(Long roleId, Integer isStop) {
        if (null == roleId || 0 >= roleId) {
            return ResponseBean.error("id不能为空");
        }
        if (null == isStop) {
            return ResponseBean.error("状态不能为空");
        }
        mapper.changeRoleIsStopByIdAndIsStop(roleId, isStop);
        for (SysRole sysRole : mapper.getRoleListByParentId(roleId)) {
            findChildrenChangeIsStop(sysRole.getId(), isStop);
        }
        return ResponseBean.success();
    }

    /**
     * 修改子级角色状态
     *
     * @param roleId
     * @param isStop
     */
    private void findChildrenChangeIsStop(Long roleId, Integer isStop) {
        mapper.changeRoleIsStopByIdAndIsStop(roleId, isStop);
        for (SysRole sysRole : mapper.getRoleListByParentId(roleId)) {
            findChildrenChangeIsStop(sysRole.getId(), isStop);
        }
    }
}
