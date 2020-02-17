package com.weiziplus.springboot.core.pc.system.service;

import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.core.pc.system.mapper.SysFunctionMapper;
import com.weiziplus.springboot.core.pc.system.mapper.SysRoleFunctionMapper;
import com.weiziplus.springboot.core.pc.system.mapper.SysRoleMapper;
import com.weiziplus.springboot.core.pc.system.mapper.SysUserMapper;
import com.weiziplus.springboot.common.models.SysRole;
import com.weiziplus.springboot.common.util.*;
import com.weiziplus.springboot.common.util.redis.RedisUtils;
import com.weiziplus.springboot.common.util.token.AdminTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2019/5/10 8:39
 */
@Slf4j
@Service
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
     * 系统功能表中角色管理id为3
     */
    private static final Integer SYS_FUNCTION_ROLE_ID = 3;

    /**
     * SysRole基础redis的key
     */
    private static final String BASE_REDIS_KEY = "pc:system:service:SysRoleService:";

    /**
     * 根据父级id和role列表获取子级列表
     *
     * @param parentId
     * @param roleList
     * @return
     */
    private List<SysRole> getChildrenListByParentIdAndRoleList(Integer parentId, List<SysRole> roleList) {
        List<SysRole> resultList = new ArrayList<>();
        for (SysRole sysRole : roleList) {
            if (!parentId.equals(sysRole.getParentId())) {
                continue;
            }
            sysRole.setChildren(getChildrenListByParentIdAndRoleList(sysRole.getId(), roleList));
            resultList.add(sysRole);
        }
        return resultList;
    }

    /**
     * 获取角色树形结构
     *
     * @return
     */
    public ResultUtils getAllRoleTreePageList() {
        String key = createRedisKey(BASE_REDIS_KEY + "getAllRoleTreePageList");
        Object object = RedisUtils.get(key);
        if (null != object) {
            return ResultUtils.success(object);
        }
        List<SysRole> resultList = new ArrayList<>();
        SysRole minParentIdRoleInfo = mapper.getMinParentIdRoleInfo();
        List<SysRole> roleList = mapper.getRoleList();
        for (SysRole sysRole : roleList) {
            if (!minParentIdRoleInfo.getParentId().equals(sysRole.getParentId())) {
                continue;
            }
            sysRole.setChildren(getChildrenListByParentIdAndRoleList(sysRole.getId(), roleList));
            resultList.add(sysRole);
        }
        PageUtils pageUtil = PageUtils.pageInfo(resultList);
        RedisUtils.set(key, pageUtil);
        return ResultUtils.success(pageUtil);
    }

    /**
     * 获取角色树形结构
     *
     * @return
     */
    public ResultUtils<List<SysRole>> getRoleTree() {
        String key = createRedisKey(BASE_REDIS_KEY + "getRoleTree");
        Object object = RedisUtils.get(key);
        if (null != object) {
            return ResultUtils.success(ToolUtils.objectOfList(object, SysRole.class));
        }
        List<SysRole> resultList = new ArrayList<>();
        //默认根节点为0
        List<SysRole> menuList = mapper.getRoleListByParentId(0);
        for (SysRole sysRole : menuList) {
            sysRole.setChildren(findChildren(sysRole));
            resultList.add(sysRole);
        }
        RedisUtils.set(key, resultList);
        return ResultUtils.success(resultList);
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
    public ResultUtils<List<SysRole>> getRoleList() {
        String key = createRedisKey(BASE_REDIS_KEY + "getRoleList");
        Object object = RedisUtils.get(key);
        if (null != object) {
            return ResultUtils.success(ToolUtils.objectOfList(object, SysRole.class));
        }
        List<SysRole> roleList = mapper.getRoleList();
        RedisUtils.set(key, roleList);
        return ResultUtils.success(roleList);
    }

    /**
     * 新增角色功能
     *
     * @param roleId
     * @param funIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultUtils addRoleFun(Integer roleId, Integer[] funIds) {
        if (null == roleId || 0 >= roleId) {
            return ResultUtils.error("roleId不能为空");
        }
        if (GlobalConfig.SUPER_ADMIN_ROLE_ID.equals(roleId)) {
            boolean haveRoleId = false;
            for (Integer id : funIds) {
                if (null == id || 0 > id) {
                    return ResultUtils.error("ids错误");
                }
                if (SYS_FUNCTION_ROLE_ID.equals(id)) {
                    haveRoleId = true;
                    break;
                }
            }
            if (!haveRoleId) {
                return ResultUtils.error("超级管理员:角色管理权限一定要添加啊(*/ω＼*)");
            }
        }
        RedisUtils.setExpireDeleteLikeKey(SysFunctionService.ROLE_FUNCTION_LIST_REDIS_KEY);
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
        RedisUtils.deleteLikeKey(SysFunctionService.ROLE_FUNCTION_LIST_REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 新增角色
     *
     * @param sysRole
     * @return
     */
    public ResultUtils addRole(SysRole sysRole) {
        //中英文开头、数字、下划线
        if (ValidateUtils.notChinaEnglishNumberUnderline(sysRole.getName())) {
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
        if (SysRole.IS_STOP_DISABLE.equals(superRole.getIsStop())) {
            return ResultUtils.error("操作失败，父级处于禁用状态");
        }
        sysRole.setCreateTime(DateUtils.getNowDateTime());
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        baseInsert(sysRole);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 修改角色
     *
     * @param sysRole
     * @return
     */
    public ResultUtils updateRole(HttpServletRequest request, SysRole sysRole) {
        //中英文开头、数字、下划线
        if (ValidateUtils.notChinaEnglishNumberUnderline(sysRole.getName())) {
            return ResultUtils.error("角色名不能包含特殊字符");
        }
        if (GlobalConfig.SUPER_ADMIN_ROLE_ID.equals(sysRole.getId())) {
            Long nowUserId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
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
        sysRole.setIsStop(null);
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        baseUpdate(sysRole);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    public ResultUtils deleteRole(HttpServletRequest request, Integer roleId) {
        if (null == roleId || 0 >= roleId) {
            return ResultUtils.error("roleId错误");
        }
        if (GlobalConfig.SUPER_ADMIN_ROLE_ID.equals(roleId)) {
            Long nowUserId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
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
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        RedisUtils.setExpireDeleteLikeKey(SysFunctionService.ROLE_FUNCTION_LIST_REDIS_KEY);
        baseDeleteByClassAndId(SysRole.class, roleId);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        RedisUtils.deleteLikeKey(SysFunctionService.ROLE_FUNCTION_LIST_REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 改变角色状态
     *
     * @param roleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultUtils changeRoleIsStop(HttpServletRequest request, Integer roleId, Integer isStop) {
        if (null == roleId || 0 >= roleId) {
            return ResultUtils.error("id不能为空");
        }
        if (GlobalConfig.SUPER_ADMIN_ROLE_ID.equals(roleId)) {
            Long nowUserId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
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
        if (SysRole.IS_STOP_ENABLE.equals(isStop)) {
            SysRole role = mapper.getInfoByRoleId(roleId);
            SysRole superRole = mapper.getInfoByRoleId(role.getParentId());
            if (SysRole.IS_STOP_DISABLE.equals(superRole.getIsStop())) {
                return ResultUtils.error("父级当前处于禁用状态");
            }
        }
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        RedisUtils.setExpireDeleteLikeKey(SysFunctionService.ROLE_FUNCTION_LIST_REDIS_KEY);
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try {
            mapper.changeRoleIsStopByIdAndIsStop(roleId, isStop);
            for (SysRole sysRole : mapper.getRoleListByParentId(roleId)) {
                findChildrenChangeIsStop(sysRole.getId(), isStop);
            }
        } catch (Exception e) {
            log.warn("改变角色状态失败，详情:" + e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return ResultUtils.error("系统错误，请重试");
        }
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        RedisUtils.deleteLikeKey(SysFunctionService.ROLE_FUNCTION_LIST_REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 修改子级角色状态
     *
     * @param roleId
     * @param isStop
     */
    private void findChildrenChangeIsStop(Integer roleId, Integer isStop) throws Exception {
        mapper.changeRoleIsStopByIdAndIsStop(roleId, isStop);
        for (SysRole sysRole : mapper.getRoleListByParentId(roleId)) {
            findChildrenChangeIsStop(sysRole.getId(), isStop);
        }
    }
}
