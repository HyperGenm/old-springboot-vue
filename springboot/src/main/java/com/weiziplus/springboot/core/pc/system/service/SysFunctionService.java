package com.weiziplus.springboot.core.pc.system.service;

import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.enums.FunctionType;
import com.weiziplus.springboot.common.enums.RoleIsStop;
import com.weiziplus.springboot.core.pc.system.mapper.SysFunctionMapper;
import com.weiziplus.springboot.core.pc.system.mapper.SysUserMapper;
import com.weiziplus.springboot.common.models.SysFunction;
import com.weiziplus.springboot.common.models.SysRole;
import com.weiziplus.springboot.common.util.*;
import com.weiziplus.springboot.common.util.redis.RedisUtils;
import com.weiziplus.springboot.common.util.token.AdminTokenUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author wanglongwei
 * @date 2019/5/9 15:18
 */
@Service
public class SysFunctionService extends BaseService {

    @Autowired
    SysFunctionMapper mapper;

    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * SysFunction基础redis的key
     */
    private static final String BASE_REDIS_KEY = createOnlyRedisKeyPrefix();

    /**
     * 根据role获取方法列表的redis的key
     */
    public static final String ROLE_FUNCTION_LIST_REDIS_KEY = BASE_REDIS_KEY + "getFunctionListByRoleId:";

    /**
     * 根据父级id和功能列表获取子级功能列表
     *
     * @param parentId
     * @param funList
     * @return
     */
    public List<SysFunction> getChildrenListByParentIdAndFunList(Integer parentId, List<SysFunction> funList) {
        List<SysFunction> newFunList = new ArrayList<>();
        for (SysFunction sysFunction : funList) {
            if (!parentId.equals(sysFunction.getParentId())) {
                continue;
            }
            sysFunction.setChildren(getChildrenListByParentIdAndFunList(sysFunction.getId(), funList));
            newFunList.add(sysFunction);
        }
        return newFunList;
    }

    /**
     * 获取所有功能树形分页列表
     *
     * @return
     */
    public ResultUtils<PageUtils<List<SysFunction>>> getAllFunctionTreePageList() {
        String key = createRedisKey(BASE_REDIS_KEY + "getAllFunctionTreePageList");
        Object object = RedisUtils.get(key);
        if (null != object) {
            PageUtils<List<SysFunction>> pageUtil = (PageUtils<List<SysFunction>>) object;
            return ResultUtils.success(pageUtil);
        }
        SysFunction baseSysFunction = mapper.getMinParentIdFunInfo();
        List<SysFunction> allFunList = mapper.getAllFunList();
        List<SysFunction> newFunList = new ArrayList<>();
        for (SysFunction sysFunction : allFunList) {
            if (!baseSysFunction.getParentId().equals(sysFunction.getParentId())) {
                continue;
            }
            sysFunction.setChildren(getChildrenListByParentIdAndFunList(sysFunction.getId(), allFunList));
            newFunList.add(sysFunction);
        }
        PageUtils<List<SysFunction>> pageUtil = PageUtils.pageInfo(newFunList);
        RedisUtils.set(key, pageUtil);
        return ResultUtils.success(pageUtil);
    }

    /**
     * 根据角色id获取功能树形列表
     *
     * @param roleId
     * @return
     */
    public List<SysFunction> getMenuTreeByRoleId(Integer roleId) {
        List<SysFunction> resultList = new ArrayList<>();
        if (null == roleId || 0 > roleId) {
            return resultList;
        }
        SysFunction sysFunction = mapper.getMinParentIdMenuFunInfoByRoleId(roleId);
        if (null == sysFunction) {
            return resultList;
        }
        List<SysFunction> menuList = mapper.getMenuListByRoleId(roleId);
        for (SysFunction sysFun : menuList) {
            if (!sysFunction.getParentId().equals(sysFun.getParentId())) {
                continue;
            }
            sysFun.setChildren(getChildrenByMenuListAndParentId(menuList, sysFun.getId()));
            resultList.add(sysFun);
        }
        return resultList;
    }

    /**
     * 根据功能菜单列表和parentId遍历子级列表
     *
     * @param menuList
     * @param parentId
     * @return
     */
    private List<SysFunction> getChildrenByMenuListAndParentId(List<SysFunction> menuList, Integer parentId) {
        List<SysFunction> resultList = new ArrayList<>();
        for (SysFunction sysFunction : menuList) {
            if (!parentId.equals(sysFunction.getParentId())) {
                continue;
            }
            sysFunction.setChildren(getChildrenByMenuListAndParentId(menuList, sysFunction.getId()));
            resultList.add(sysFunction);
        }
        return resultList;
    }

    /**
     * 获取所有功能列表树形结构
     *
     * @return
     */
    public ResultUtils<List<SysFunction>> getFunTree() {
        String key = createRedisKey(BASE_REDIS_KEY + "getFunTree");
        Object object = RedisUtils.get(key);
        if (null != object) {
            return ResultUtils.success(ToolUtils.objectOfList(object, SysFunction.class));
        }
        List<SysFunction> resultList = new ArrayList<>();
        SysFunction sysFunction = mapper.getMinParentIdFunInfo();
        if (null == sysFunction) {
            return ResultUtils.success(resultList);
        }
        List<SysFunction> menuList = mapper.getAllFunList();
        for (SysFunction sysFun : menuList) {
            if (!sysFunction.getParentId().equals(sysFun.getParentId())) {
                continue;
            }
            sysFun.setChildren(getChildrenListByParentIdAndFunList(sysFun.getId(), menuList));
            resultList.add(sysFun);
        }
        RedisUtils.set(key, resultList);
        return ResultUtils.success(resultList);
    }

    /**
     * 获取所有功能列表树形结构---不包含按钮
     *
     * @return
     */
    public ResultUtils<List<SysFunction>> getAllFunctionTreeNotButton() {
        String key = createRedisKey(BASE_REDIS_KEY + "getAllFunctionTreeNotButton");
        Object object = RedisUtils.get(key);
        if (null != object) {
            return ResultUtils.success(ToolUtils.objectOfList(object, SysFunction.class));
        }
        List<SysFunction> resultList = new ArrayList<>();
        SysFunction sysFunction = mapper.getMinParentIdFunInfo();
        if (null == sysFunction) {
            return ResultUtils.success(resultList);
        }
        List<SysFunction> menuList = mapper.getFunNotButtonList();
        for (SysFunction sysFun : menuList) {
            if (!sysFun.getParentId().equals(sysFunction.getParentId())) {
                continue;
            }
            sysFun.setChildren(getChildrenListByParentIdAndFunList(sysFun.getId(), menuList));
            resultList.add(sysFun);
        }
        RedisUtils.set(key, resultList);
        return ResultUtils.success(resultList);
    }

    /**
     * 获取所有功能包含的api
     *
     * @return
     */
    public Set<String> getAllFunContainApi() {
        String key = createRedisKey(BASE_REDIS_KEY + "getAllFunContainApi:");
        Object object = RedisUtils.get(key);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> result = new HashSet<>();
        for (SysFunction sysFunction : mapper.getAllFunList()) {
            String containApi = sysFunction.getContainApi();
            if (StringUtil.isBlank(containApi)) {
                continue;
            }
            String[] split = containApi.replaceAll("[^(a-zA-Z/，,)]*", "")
                    .replace("，", ",").split(",");
            result.addAll(Arrays.asList(split));
        }
        RedisUtils.set(key, result);
        return result;
    }

    /**
     * 根据roleId获取所有方法列表
     *
     * @param roleId
     * @return
     */
    private List<SysFunction> getFunctionListByRoleId(Integer roleId) {
        if (null == roleId || 0 > roleId) {
            return null;
        }
        String key = createRedisKey(ROLE_FUNCTION_LIST_REDIS_KEY + "getFunctionListByRoleId:", roleId);
        Object object = RedisUtils.get(key);
        if (null != object) {
            return ToolUtils.objectOfList(object, SysFunction.class);
        }
        List<SysFunction> list = mapper.getFunListByRoleId(roleId);
        RedisUtils.set(key, list);
        return list;
    }

    /**
     * 根据角色id获取功能id列表
     *
     * @param roleId
     * @return
     */
    public ResultUtils<List<Integer>> getRoleFunList(Integer roleId) {
        List<Integer> resultList = new ArrayList<>();
        if (null == roleId || 0 > roleId) {
            return ResultUtils.success(resultList);
        }
        for (SysFunction sysFunction : getFunctionListByRoleId(roleId)) {
            resultList.add(sysFunction.getId());
        }
        return ResultUtils.success(resultList);
    }

    /**
     * 根据roleId获取拥有的api
     *
     * @param roleId
     * @return
     */
    public Set<String> getFunContainApiByRoleId(Integer roleId) {
        if (null == roleId || 0 > roleId) {
            return null;
        }
        String key = createRedisKey(ROLE_FUNCTION_LIST_REDIS_KEY + "getFunContainApiByRoleId:", roleId);
        Object object = RedisUtils.get(key);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        SysRole sysRole = baseFindByClassAndId(SysRole.class, roleId);
        if (null == sysRole) {
            return null;
        }
        //当前角色禁用
        if (RoleIsStop.DISABLE.getValue().equals(sysRole.getIsStop())) {
            return null;
        }
        Set<String> resultList = new HashSet<>();
        for (SysFunction sysFunction : mapper.getFunListByRoleId(roleId)) {
            String containApi = sysFunction.getContainApi();
            if (StringUtil.isBlank(containApi)) {
                continue;
            }
            String[] split = containApi.replaceAll("[^(a-zA-Z/，,)]*", "")
                    .replace("，", ",").split(",");
            resultList.addAll(Arrays.asList(split));
        }
        RedisUtils.set(key, resultList);
        return resultList;
    }

    /**
     * 新增功能
     *
     * @param request
     * @param sysFunction
     * @return
     */
    public ResultUtils addFunction(HttpServletRequest request, SysFunction sysFunction) {
        //如果非超级管理员  新增功能 强制用户下线
        Long nowUserId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
        }
        if (ValidateUtils.notEnglishNumberUnderline(sysFunction.getName())) {
            return ResultUtils.error("name为英文开头，英文、数字和下划线且最少两位");
        }
        if (null == sysFunction.getParentId() || 0 > sysFunction.getParentId()) {
            return ResultUtils.error("parentId不能为空");
        }
        if (ToolUtils.isBlank(sysFunction.getTitle())) {
            return ResultUtils.error("标题不能为空");
        }
        //判断类型是否正确
        if (!FunctionType.contains(sysFunction.getType())) {
            return ResultUtils.error("类型错误");
        }
        SysFunction sysFun = baseFindOneDataByClassAndColumnAndValue(SysFunction.class, SysFunction.COLUMN_NAME, sysFunction.getName());
        if (null != sysFun) {
            return ResultUtils.error("name已存在");
        }
        if (!ToolUtils.isBlank(sysFunction.getContainApi())) {
            sysFunction.setContainApi(sysFunction.getContainApi()
                    .replaceAll("[^(a-zA-Z/，,)]*", "")
                    .replace("，", ","));
        }
        sysFunction.setCreateTime(DateUtils.getNowDateTime());
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        baseInsert(sysFunction);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 修改功能
     *
     * @param request
     * @param sysFunction
     * @return
     */
    public ResultUtils updateFunction(HttpServletRequest request, SysFunction sysFunction) {
        if (ToolUtils.isBlank(sysFunction.getTitle())) {
            return ResultUtils.error("标题不能为空");
        }
        if (ValidateUtils.notEnglishNumberUnderline(sysFunction.getName())) {
            return ResultUtils.error("name为英文开头，英文、数字和下划线且最少两位");
        }
        SysFunction sysFun = baseFindOneDataByClassAndColumnAndValue(SysFunction.class, SysFunction.COLUMN_NAME, sysFunction.getName());
        if (null != sysFun && !sysFun.getId().equals(sysFunction.getId())) {
            return ResultUtils.error("name已存在");
        }
        if (!ToolUtils.isBlank(sysFunction.getContainApi())) {
            sysFunction.setContainApi(sysFunction.getContainApi()
                    .replaceAll("[^(a-zA-Z/，,)]*", "")
                    .replace("，", ","));
        }
        //如果要修改为按钮
        if (FunctionType.BUTTON.getValue().equals(sysFunction.getType())) {
            List<SysFunction> list = mapper.getFunListByParentId(sysFunction.getId());
            if (null != list && 0 < list.size()) {
                return ResultUtils.error("有下级，不能修改为按钮");
            }
        }
        Long nowUserId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            //如果不是超级管理员，不能进行下面的操作
            sysFunction.setPath(null);
            sysFunction.setType(null);
            sysFunction.setParentId(null);
            sysFunction.setContainApi(null);
        }
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        baseUpdate(sysFunction);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 根据ids删除功能
     *
     * @param request
     * @param ids
     * @return
     */
    public ResultUtils deleteFunction(HttpServletRequest request, Integer[] ids) {
        //如果非超级管理员 删除功能 强制用户下线
        Long nowUserId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
        }
        if (null == ids) {
            return ResultUtils.error("ids不能为空");
        }
        for (Integer id : ids) {
            if (null == id || 0 > id) {
                return ResultUtils.error("ids错误");
            }
            List<SysFunction> list = mapper.getFunListByParentId(id);
            if (null != list && 0 < list.size()) {
                return ResultUtils.error("目录下面含有子级目录");
            }
        }
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        baseDeleteByClassAndIds(SysFunction.class, ids);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtils.success();
    }
}
