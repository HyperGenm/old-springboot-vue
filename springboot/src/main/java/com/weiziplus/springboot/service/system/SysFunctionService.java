package com.weiziplus.springboot.service.system;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.config.GlobalConfig;
import com.weiziplus.springboot.mapper.system.SysFunctionMapper;
import com.weiziplus.springboot.mapper.system.SysUserMapper;
import com.weiziplus.springboot.models.SysFunction;
import com.weiziplus.springboot.models.SysRole;
import com.weiziplus.springboot.util.*;
import com.weiziplus.springboot.util.redis.RedisUtils;
import com.weiziplus.springboot.util.token.AdminTokenUtils;
import com.weiziplus.springboot.util.token.JwtTokenUtils;
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
    private static final String BASE_REDIS_KEY = "service:system:SysFunctionService:";

    /**
     * 根据role获取方法列表的redis的key
     */
    public static final String ROLE_FUNCTION_LIST_REDIS_KEY = BASE_REDIS_KEY + "getFunctionListByRoleId:";

    /**
     * 根据角色id获取功能树形列表
     *
     * @param roleId
     * @return
     */
    public List<SysFunction> getMenuTreeByRoleId(Long roleId) {
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
    private List<SysFunction> getChildrenByMenuListAndParentId(List<SysFunction> menuList, Long parentId) {
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
    public List<SysFunction> getFunTree() {
        String key = createRedisKey(BASE_REDIS_KEY + "getFunTree");
        Object object = RedisUtils.get(key);
        if (null != object) {
            return ToolUtils.objectOfList(object, SysFunction.class);
        }
        List<SysFunction> resultList = new ArrayList<>();
        SysFunction sysFunction = mapper.getMinParentIdFunInfo();
        if (null == sysFunction) {
            return resultList;
        }
        List<SysFunction> menuList = mapper.getAllFunList();
        for (SysFunction sysFun : menuList) {
            if (!sysFunction.getParentId().equals(sysFun.getParentId())) {
                continue;
            }
            sysFun.setChildren(getChildrenFun(menuList, sysFun.getId()));
            resultList.add(sysFun);
        }
        RedisUtils.set(key, resultList);
        return resultList;
    }

    /**
     * 遍历子级功能列表
     *
     * @param menuList
     * @param parentId
     * @return
     */
    private List<SysFunction> getChildrenFun(List<SysFunction> menuList, Long parentId) {
        List<SysFunction> resultList = new ArrayList<>();
        for (SysFunction sysFunction : menuList) {
            if (!parentId.equals(sysFunction.getParentId())) {
                continue;
            }
            sysFunction.setChildren(getChildrenFun(menuList, sysFunction.getId()));
            resultList.add(sysFunction);
        }
        return resultList;
    }

    /**
     * 获取所有功能列表树形结构---不包含按钮
     *
     * @return
     */
    public List<SysFunction> getAllFunctionTreeNotButton() {
        String key = createRedisKey(BASE_REDIS_KEY + "getAllFunctionTreeNotButton");
        Object object = RedisUtils.get(key);
        if (null != object) {
            return ToolUtils.objectOfList(object, SysFunction.class);
        }
        List<SysFunction> resultList = new ArrayList<>();
        SysFunction sysFunction = mapper.getMinParentIdFunInfo();
        if (null == sysFunction) {
            return resultList;
        }
        List<SysFunction> menuList = mapper.getFunNotButtonList();
        for (SysFunction sysFun : menuList) {
            if (!sysFun.getParentId().equals(sysFunction.getParentId())) {
                continue;
            }
            sysFun.setChildren(getChildrenFunctionNotButton(menuList, sysFun.getId()));
            resultList.add(sysFun);
        }
        RedisUtils.set(key, resultList);
        return resultList;
    }

    /**
     * 遍历子级功能列表---不包含按钮
     *
     * @param menuList
     * @param parentId
     * @return
     */
    private List<SysFunction> getChildrenFunctionNotButton(List<SysFunction> menuList, Long parentId) {
        List<SysFunction> resultList = new ArrayList<>();
        for (SysFunction sysFunction : menuList) {
            if (!parentId.equals(sysFunction.getParentId())) {
                continue;
            }
            sysFunction.setChildren(getChildrenFunctionNotButton(menuList, sysFunction.getId()));
            resultList.add(sysFunction);
        }
        return resultList;
    }

    /**
     * 根据父级id获取子级列表
     *
     * @param parentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ResultUtils getFunctionListByParentId(Long parentId, Integer pageNum, Integer pageSize) {
        if (null == parentId || 0 > parentId) {
            return ResultUtils.error("parentId错误");
        }
        if (0 >= pageNum || 0 >= pageSize) {
            return ResultUtils.error("pageNum,pageSize格式错误");
        }
        String key = createRedisKey(BASE_REDIS_KEY + "getFunctionListByParentId", parentId, pageNum, pageSize);
        Object object = RedisUtils.get(key);
        if (null != object) {
            return ResultUtils.success(object);
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils pageUtil = PageUtils.pageInfo(mapper.getFunListByParentId(parentId));
        RedisUtils.set(key, pageUtil);
        return ResultUtils.success(pageUtil);
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
            String[] split = containApi.replaceAll("[^(a-zA-Z/)]*", "")
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
    private List<SysFunction> getFunctionListByRoleId(Long roleId) {
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
    public List<Long> getRoleFunList(Long roleId) {
        List<Long> resultList = new ArrayList<>();
        if (null == roleId || 0 > roleId) {
            return resultList;
        }
        for (SysFunction sysFunction : getFunctionListByRoleId(roleId)) {
            resultList.add(sysFunction.getId());
        }
        return resultList;
    }

    /**
     * 根据roleId获取拥有的api
     *
     * @param roleId
     * @return
     */
    public Set<String> getFunContainApiByRoleId(Long roleId) {
        if (null == roleId || 0 > roleId) {
            return null;
        }
        String key = createRedisKey(ROLE_FUNCTION_LIST_REDIS_KEY + "getFunContainApiByRoleId:", roleId);
        Object object = RedisUtils.get(key);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Map<String, Object> map = baseFindByClassAndId(SysRole.class, roleId);
        if (null == map) {
            return null;
        }
        //是否启用;0:启用,1:禁用'
        Object isStopObject = map.get("isStop");
        String stopStatus = "1";
        if (null == isStopObject || stopStatus.equals(ToolUtils.valueOfString(isStopObject))) {
            return null;
        }
        Set<String> resultList = new HashSet<>();
        for (SysFunction sysFunction : mapper.getFunListByRoleId(roleId)) {
            String containApi = sysFunction.getContainApi();
            if (StringUtil.isBlank(containApi)) {
                continue;
            }
            String[] split = containApi.replaceAll("[^(a-zA-Z/)]*", "")
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
        if (ValidateUtils.notEnglishNumberUnderline(sysFunction.getName())) {
            return ResultUtils.error("name为英文开头，英文、数字和下划线且最少两位");
        }
        if (null == sysFunction.getParentId() || 0 > sysFunction.getParentId()) {
            return ResultUtils.error("parentId不能为空");
        }
        if (ToolUtils.isBlank(sysFunction.getTitle())) {
            return ResultUtils.error("标题不能为空");
        }
        //如果非超级管理员  新增功能 进行封号处理
        Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            sysUserMapper.suspendSysUser(nowUserId);
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
        }
        SysFunction sysFun = mapper.getFunInfoByName(sysFunction.getName());
        if (null != sysFun) {
            return ResultUtils.error("name已存在");
        }
        if (!ToolUtils.isBlank(sysFunction.getContainApi())) {
            sysFunction.setContainApi(sysFunction.getContainApi()
                    .replaceAll("[^(a-zA-Z/)]*", "")
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
        SysFunction sysFun = mapper.getFunInfoByName(sysFunction.getName());
        if (null != sysFun && !sysFun.getId().equals(sysFunction.getId())) {
            return ResultUtils.error("name已存在");
        }
        if (!ToolUtils.isBlank(sysFunction.getContainApi())) {
            //如果非超级管理员 修改功能对应api 进行封号处理
            Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
            if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
                sysUserMapper.suspendSysUser(nowUserId);
                AdminTokenUtils.deleteToken(nowUserId);
                return ResultUtils.errorSuspend();
            }
            sysFunction.setContainApi(sysFunction.getContainApi()
                    .replaceAll("[^(a-zA-Z/)]*", "")
                    .replace("，", ","));
        } else {
            //如果非超级管理员 清空功能对应api 取消本次操作
            sysFunction.setContainApi(null);
        }
        if (!ToolUtils.isBlank(sysFunction.getPath())) {
            //如果非超级管理员 修改功能路径 进行封号处理
            Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
            if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
                sysUserMapper.suspendSysUser(nowUserId);
                AdminTokenUtils.deleteToken(nowUserId);
                return ResultUtils.errorSuspend();
            }
        } else {
            return ResultUtils.error("路径不能为空");
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
    public ResultUtils deleteFunction(HttpServletRequest request, Long[] ids) {
        if (null == ids) {
            return ResultUtils.error("ids不能为空");
        }
        //如果非超级管理员 删除功能 进行封号处理
        Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            sysUserMapper.suspendSysUser(nowUserId);
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
        }
        for (Long id : ids) {
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
