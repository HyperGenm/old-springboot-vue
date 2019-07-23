package com.weiziplus.springboot.service.system;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.models.SysFunction;
import com.weiziplus.springboot.utils.DateUtil;
import com.weiziplus.springboot.utils.PageUtil;
import com.weiziplus.springboot.utils.ResultUtil;
import com.weiziplus.springboot.utils.ValidateUtil;
import com.weiziplus.springboot.mapper.system.SysFunctionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/9 15:18
 */
@Service
@CacheConfig(cacheNames = "pc:system:sysFunctionService")
public class SysFunctionService extends BaseService {

    @Autowired
    SysFunctionMapper mapper;

    /**
     * 根据角色id获取功能树形列表
     *
     * @param roleId
     * @return
     */
    @Cacheable(condition = "#roleId > 1")
    public List<SysFunction> getMenuTreeByRoleId(Long roleId) {
        List<SysFunction> resultList = new ArrayList<>();
        if (null == roleId || 0 > roleId) {
            return resultList;
        }
        SysFunction sysFunction = mapper.getMinParentIdMenuFunInfoByRoleId(roleId);
        if (null == sysFunction) {
            return resultList;
        }
        List<SysFunction> menuList = mapper.getMenuListByRoleIdAndParentId(roleId, sysFunction.getParentId());
        for (SysFunction sysFun : menuList) {
            sysFun.setChildren(findMenuChildren(roleId, sysFun));
            resultList.add(sysFun);
        }
        return resultList;
    }

    /**
     * 根据角色id递归子级菜单树形列表
     *
     * @param roleId
     * @param sysFunction
     * @return
     */
    private List<SysFunction> findMenuChildren(Long roleId, SysFunction sysFunction) {
        List<SysFunction> childrenList = new ArrayList<>();
        List<SysFunction> menuList = mapper.getMenuListByRoleIdAndParentId(roleId, sysFunction.getId());
        for (SysFunction sysFun : menuList) {
            sysFun.setChildren(findMenuChildren(roleId, sysFun));
            childrenList.add(sysFun);
        }
        return childrenList;
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
        List<SysFunction> menuList = mapper.getFunListByParentId(sysFunction.getParentId());
        for (SysFunction sysFun : menuList) {
            sysFun.setChildren(findChildren(sysFun));
            resultList.add(sysFun);
        }
        return resultList;
    }

    /**
     * 获取子级功能列表
     *
     * @param sysFunction
     * @return
     */
    private List<SysFunction> findChildren(SysFunction sysFunction) {
        List<SysFunction> childrenList = new ArrayList<>();
        List<SysFunction> menuList = mapper.getFunListByParentId(sysFunction.getId());
        for (SysFunction sysFun : menuList) {
            sysFun.setChildren(findChildren(sysFun));
            childrenList.add(sysFun);
        }
        return childrenList;
    }

    /**
     * 获取所有功能列表树形结构
     *
     * @return
     */
    @Cacheable
    public List<SysFunction> getAllFunctionTreeNotButton() {
        List<SysFunction> resultList = new ArrayList<>();
        SysFunction sysFunction = mapper.getMinParentIdFunInfo();
        List<SysFunction> menuList = mapper.getFunNotButtonListByParentId(sysFunction.getParentId());
        for (SysFunction sysFun : menuList) {
            sysFun.setChildren(findChildrenNotButton(sysFun));
            resultList.add(sysFun);
        }
        return resultList;
    }

    /**
     * 获取子级功能列表
     *
     * @param sysFunction
     * @return
     */
    private List<SysFunction> findChildrenNotButton(SysFunction sysFunction) {
        List<SysFunction> childrenList = new ArrayList<>();
        List<SysFunction> menuList = mapper.getFunNotButtonListByParentId(sysFunction.getId());
        for (SysFunction sysFun : menuList) {
            sysFun.setChildren(findChildrenNotButton(sysFun));
            childrenList.add(sysFun);
        }
        return childrenList;
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
    public ResultUtil getFunctionListByParentId(Long parentId, Integer pageNum, Integer pageSize) {
        if (null == parentId || 0 > parentId) {
            return ResultUtil.error("parentId错误");
        }
        if (0 >= pageNum || 0 >= pageSize) {
            return ResultUtil.error("pageNum,pageSize格式错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtil pageUtil = PageUtil.pageInfo(mapper.getFunListByParentId(parentId));
        return ResultUtil.success(pageUtil);
    }

    /**
     * 新增功能
     *
     * @param sysFunction
     * @return
     */
    @CacheEvict(allEntries = true)
    public ResultUtil addFunction(SysFunction sysFunction) {
        if (ValidateUtil.notEnglishNumberUnderLine(sysFunction.getName())) {
            return ResultUtil.error("name为英文开头，英文、数字和下划线且最少两位");
        }
        if (null == sysFunction.getParentId() || 0 > sysFunction.getParentId()) {
            return ResultUtil.error("parentId不能为空");
        }
        SysFunction sysFun = mapper.getFunInfoByName(sysFunction.getName());
        if (null != sysFun) {
            return ResultUtil.error("name已存在");
        }
        sysFunction.setCreateTime(DateUtil.getNowDate());
        return ResultUtil.success(baseInsert(sysFunction));
    }

    /**
     * 修改功能
     *
     * @param sysFunction
     * @return
     */
    @CacheEvict(allEntries = true)
    public ResultUtil updateFunction(SysFunction sysFunction) {
        return ResultUtil.success(baseUpdate(sysFunction));
    }

    /**
     * 根据ids删除功能
     *
     * @param ids
     * @return
     */
    @CacheEvict(allEntries = true)
    public ResultUtil deleteFunction(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error("ids不能为空");
        }
        for (Long id : ids) {
            if (null == id || 0 > id) {
                return ResultUtil.error("ids错误");
            }
            List<SysFunction> list = mapper.getFunListByParentId(id);
            if (null != list && 0 < list.size()) {
                return ResultUtil.error("目录下面含有子级目录");
            }
        }
        return ResultUtil.success(baseDeleteByClassAndIds(SysFunction.class, ids));
    }
}
