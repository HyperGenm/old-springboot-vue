package com.weiziplus.springboot.service.system;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.models.SysFunction;
import com.weiziplus.springboot.util.*;
import com.weiziplus.springboot.mapper.system.SysFunctionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglongwei
 * @data 2019/5/9 15:18
 */
@Service
@CacheConfig(cacheNames = "system:sysFunctionService")
public class SysFunctionService extends BaseService {

    @Autowired
    SysFunctionMapper mapper;

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
    @Cacheable
    public List<SysFunction> getFunTree() {
        List<SysFunction> resultList = new ArrayList<>();
        SysFunction sysFunction = mapper.getMinParentIdFunInfo();
        if (null == sysFunction) {
            return resultList;
        }
        List<SysFunction> menuList = mapper.getFunList();
        for (SysFunction sysFun : menuList) {
            if (!sysFunction.getParentId().equals(sysFun.getParentId())) {
                continue;
            }
            sysFun.setChildren(getChildrenFun(menuList, sysFun.getId()));
            resultList.add(sysFun);
        }
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
    @Cacheable
    public List<SysFunction> getAllFunctionTreeNotButton() {
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
    @Cacheable
    public ResultUtils getFunctionListByParentId(Long parentId, Integer pageNum, Integer pageSize) {
        if (null == parentId || 0 > parentId) {
            return ResultUtils.error("parentId错误");
        }
        if (0 >= pageNum || 0 >= pageSize) {
            return ResultUtils.error("pageNum,pageSize格式错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils pageUtil = PageUtils.pageInfo(mapper.getFunListByParentId(parentId));
        return ResultUtils.success(pageUtil);
    }

    /**
     * 新增功能
     *
     * @param sysFunction
     * @return
     */
    @CacheEvict(allEntries = true)
    public ResultUtils addFunction(SysFunction sysFunction) {
        if (ValidateUtils.notEnglishNumberUnderLine(sysFunction.getName())) {
            return ResultUtils.error("name为英文开头，英文、数字和下划线且最少两位");
        }
        if (null == sysFunction.getParentId() || 0 > sysFunction.getParentId()) {
            return ResultUtils.error("parentId不能为空");
        }
        if (ToolUtils.isBlank(sysFunction.getTitle())) {
            return ResultUtils.error("标题不能为空");
        }
        SysFunction sysFun = mapper.getFunInfoByName(sysFunction.getName());
        if (null != sysFun) {
            return ResultUtils.error("name已存在");
        }
        sysFunction.setCreateTime(DateUtils.getNowDateTime());
        return ResultUtils.success(baseInsert(sysFunction));
    }

    /**
     * 修改功能
     *
     * @param sysFunction
     * @return
     */
    @CacheEvict(allEntries = true)
    public ResultUtils updateFunction(SysFunction sysFunction) {
        if (ToolUtils.isBlank(sysFunction.getTitle())) {
            return ResultUtils.error("标题不能为空");
        }
        SysFunction sysFun = mapper.getFunInfoByName(sysFunction.getName());
        if (null != sysFun && !sysFun.getId().equals(sysFunction.getId())) {
            return ResultUtils.error("name已存在");
        }
        return ResultUtils.success(baseUpdate(sysFunction));
    }

    /**
     * 根据ids删除功能
     *
     * @param ids
     * @return
     */
    @CacheEvict(allEntries = true)
    public ResultUtils deleteFunction(Long[] ids) {
        if (null == ids) {
            return ResultUtils.error("ids不能为空");
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
        return ResultUtils.success(baseDeleteByClassAndIds(SysFunction.class, ids));
    }
}
