package com.weiziplus.springboot.service.system;

import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.config.GlobalConfig;
import com.weiziplus.springboot.mapper.system.SysFunctionMapper;
import com.weiziplus.springboot.mapper.system.SysRoleFunctionMapper;
import com.weiziplus.springboot.mapper.system.SysRoleMapper;
import com.weiziplus.springboot.mapper.system.SysUserMapper;
import com.weiziplus.springboot.models.SysFunction;
import com.weiziplus.springboot.models.SysRole;
import com.weiziplus.springboot.util.DateUtils;
import com.weiziplus.springboot.util.ResultUtils;
import com.weiziplus.springboot.util.ValidateUtils;
import com.weiziplus.springboot.util.token.AdminTokenUtils;
import com.weiziplus.springboot.util.token.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglongwei
 * @data 2019/5/10 8:39
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "system:sysRoleService")
public class SysRoleService extends BaseService {

    @Autowired
    SysRoleMapper mapper;

    @Autowired
    SysFunctionMapper sysFunctionMapper;

    @Autowired
    SysRoleFunctionMapper sysRoleFunctionMapper;

    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * 获取角色树形结构
     *
     * @return
     */
    @Cacheable
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
     * 获取子级角色树形结构
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
     * 获取角色列表
     *
     * @return
     */
    @Cacheable
    public List<SysRole> getRoleList() {
        return mapper.getRoleList();
    }

    /**
     * 根据角色id获取功能id列表
     *
     * @param roleId
     * @return
     */
    public List<Long> getRoleFunList(Long roleId) {
        List<Long> resultList = new ArrayList<>();
        if (null == roleId || 0 > roleId) {
            return resultList;
        }
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
    @Transactional(rollbackFor = Exception.class)
    public ResultUtils addRoleFun(Long roleId, Long[] funIds) {
        if (null == roleId || 0 >= roleId) {
            return ResultUtils.error("roleId不能为空");
        }
        if (GlobalConfig.SUPER_ADMIN_ROLE_ID.equals(roleId)) {
            boolean haveRoleId = false;
            for (Long id : funIds) {
                if (null == id || 0 > id) {
                    return ResultUtils.error("ids错误");
                }
                if (GlobalConfig.SYS_FUNCTION_ROLE_ID.equals(id)) {
                    haveRoleId = true;
                    break;
                }
            }
            if (!haveRoleId) {
                return ResultUtils.error("超级管理员:角色管理权限一定要添加啊(*/ω＼*)");
            }
        }
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try {
            sysRoleFunctionMapper.deleteByRoleId(roleId);
            if (null == funIds || 0 >= funIds.length) {
                return ResultUtils.success();
            }
            sysRoleFunctionMapper.addRoleFunction(roleId, funIds);
        } catch (Exception e) {
            log.warn("系统用户角色更新失败---" + e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return ResultUtils.error("系统错误，请重试");
        }
        return ResultUtils.success();
    }

    /**
     * 新增角色
     *
     * @param sysRole
     * @return
     */
    @CacheEvict(allEntries = true)
    public ResultUtils addRole(SysRole sysRole) {
        if (ValidateUtils.notUsername(sysRole.getName())) {
            return ResultUtils.error("角色名不能包含特殊字符");
        }
        //因为超级管理员只有一个所以角色父级id从1开始
        if (null == sysRole.getParentId() || GlobalConfig.SUPER_ADMIN_ID > sysRole.getParentId()) {
            return ResultUtils.error("parentId不能为空");
        }
        SysRole role = mapper.getRoleInfoByName(sysRole.getName());
        if (null != role && null != role.getId()) {
            return ResultUtils.error("角色名已存在");
        }
        SysRole superRole = mapper.getInfoByRoleId(sysRole.getParentId());
        if (!GlobalConfig.IS_STOP.equals(superRole.getIsStop())) {
            return ResultUtils.error("操作失败，父级处于禁用状态");
        }
        sysRole.setCreateTime(DateUtils.getNowDateTime());
        return ResultUtils.success(baseInsert(sysRole));
    }

    /**
     * 修改角色
     *
     * @param sysRole
     * @return
     */
    @CacheEvict(allEntries = true)
    public ResultUtils updateRole(HttpServletRequest request, SysRole sysRole) {
        if (ValidateUtils.notUsername(sysRole.getName())) {
            return ResultUtils.error("角色名不能包含特殊字符");
        }
        if (GlobalConfig.SUPER_ADMIN_ROLE_ID.equals(sysRole.getId())) {
            Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
            if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
                sysUserMapper.suspendSysUser(nowUserId);
                AdminTokenUtils.deleteToken(nowUserId);
                return ResultUtils.errorSuspend();
            } else {
                return ResultUtils.error("不能修改超级管理员角色");
            }
        }
        SysRole role = mapper.getRoleInfoByName(sysRole.getName());
        if (null != role && null != role.getId() && !role.getId().equals(sysRole.getId())) {
            return ResultUtils.error("角色名已存在");
        }
        if (!GlobalConfig.IS_STOP.equals(sysRole.getIsStop())) {
            return ResultUtils.error("操作失败，角色处于禁用状态");
        }
        SysRole superRole = mapper.getInfoByRoleId(sysRole.getParentId());
        if (!GlobalConfig.IS_STOP.equals(superRole.getIsStop())) {
            return ResultUtils.error("操作失败，父级处于禁用状态");
        }
        return ResultUtils.success(baseUpdate(sysRole));
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @CacheEvict(allEntries = true)
    public ResultUtils deleteRole(HttpServletRequest request, Long roleId) {
        if (null == roleId || 0 >= roleId) {
            return ResultUtils.error("roleId错误");
        }
        if (GlobalConfig.SUPER_ADMIN_ROLE_ID.equals(roleId)) {
            Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
            if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
                sysUserMapper.suspendSysUser(nowUserId);
                AdminTokenUtils.deleteToken(nowUserId);
                return ResultUtils.errorSuspend();
            } else {
                return ResultUtils.error("不能删除超级管理员角色");
            }
        }
        List<SysRole> list = mapper.getRoleListByParentId(roleId);
        if (null != list && 0 < list.size()) {
            return ResultUtils.error("当前角色存在下级");
        }
        return ResultUtils.success(baseDeleteByClassAndId(SysRole.class, roleId));
    }

    /**
     * 改变角色状态
     *
     * @param roleId
     * @return
     */
    @CacheEvict(allEntries = true)
    public ResultUtils changeRoleIsStop(HttpServletRequest request, Long roleId, Integer isStop) {
        if (null == roleId || 0 >= roleId) {
            return ResultUtils.error("id不能为空");
        }
        if (GlobalConfig.SUPER_ADMIN_ROLE_ID.equals(roleId)) {
            Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
            if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
                sysUserMapper.suspendSysUser(nowUserId);
                AdminTokenUtils.deleteToken(nowUserId);
                return ResultUtils.errorSuspend();
            } else {
                return ResultUtils.error("不能操作超级管理员");
            }
        }
        if (null == isStop) {
            return ResultUtils.error("状态不能为空");
        }
        //判断是否启用
        if (GlobalConfig.IS_STOP.equals(isStop)) {
            SysRole role = mapper.getInfoByRoleId(roleId);
            SysRole superRole = mapper.getInfoByRoleId(role.getParentId());
            if (!GlobalConfig.IS_STOP.equals(superRole.getIsStop())) {
                return ResultUtils.error("父级当前处于禁用状态");
            }
        }
        mapper.changeRoleIsStopByIdAndIsStop(roleId, isStop);
        for (SysRole sysRole : mapper.getRoleListByParentId(roleId)) {
            findChildrenChangeIsStop(sysRole.getId(), isStop);
        }
        return ResultUtils.success();
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
