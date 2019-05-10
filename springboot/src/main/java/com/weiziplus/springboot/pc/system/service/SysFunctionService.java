package com.weiziplus.springboot.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.pc.system.mapper.SysFunctionMapper;
import com.weiziplus.springboot.common.models.SysFunction;
import com.weiziplus.springboot.common.utils.PageBean;
import com.weiziplus.springboot.common.utils.ResponseBean;
import com.weiziplus.springboot.common.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/9 15:18
 */
@Service
public class SysFunctionService {

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
     * 根据父级id获取子级列表
     *
     * @param parentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Map<String, Object> getFunctionListByParentId(Long parentId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return PageBean.pageInfo(mapper.getFunListByParentId(parentId));
    }

    /**
     * 新增功能
     *
     * @param sysFunction
     * @return
     */
    public Map<String, Object> addFunction(SysFunction sysFunction) {
        if (ValidateUtil.notEnglishNumberUnderLine(sysFunction.getName())) {
            return ResponseBean.error("name为英文开头，英文和数字");
        }
        if (null == sysFunction.getParentId() || 0 > sysFunction.getParentId()) {
            return ResponseBean.error("parentId不能为空");
        }
        SysFunction sysFun = mapper.getFunInfoByName(sysFunction.getName());
        if (null != sysFun) {
            return ResponseBean.error("name已存在");
        }
        return ResponseBean.success(mapper.addFunction(sysFunction));
    }

    /**
     * 修改功能
     *
     * @param sysFunction
     * @return
     */
    public Map<String, Object> updateFunction(SysFunction sysFunction) {
        return ResponseBean.success(mapper.updateFunction(sysFunction));
    }

    /**
     * 根据ids删除功能
     *
     * @param ids
     * @return
     */
    public Map<String, Object> deleteFunction(Long[] ids) {
        for (Long id : ids) {
            List<SysFunction> list = mapper.getFunListByParentId(id);
            if (null != list && 0 < list.size()) {
                return ResponseBean.error("目录下面含有子级目录");
            }
        }
        return ResponseBean.success(mapper.deleteFunctionByIds(ids));
    }
}
