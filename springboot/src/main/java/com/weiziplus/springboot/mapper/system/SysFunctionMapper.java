package com.weiziplus.springboot.mapper.system;

import com.weiziplus.springboot.models.SysFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @data 2019/5/9 12:00
 */
@Mapper
public interface SysFunctionMapper {

    /**
     * 根据角色id获取parent_id最小的功能信息---菜单
     *
     * @param roleId
     * @return
     */
    SysFunction getMinParentIdMenuFunInfoByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色id获取菜单列表
     *
     * @param roleId
     * @return
     */
    List<SysFunction> getMenuListByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据roleId获取按钮列表
     *
     * @param roleId
     * @return
     */
    List<SysFunction> getButtonListByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取parent_id最小的功能信息
     *
     * @return
     */
    SysFunction getMinParentIdFunInfo();

    /**
     * 获取功能列表
     *
     * @return
     */
    List<SysFunction> getALLFunList();

    /**
     * 根据父级id获取功能列表
     *
     * @param parentId
     * @return
     */
    List<SysFunction> getFunListByParentId(@Param("parentId") Long parentId);

    /**
     * 根据父级id获取功能列表不包含按钮
     *
     * @return
     */
    List<SysFunction> getFunNotButtonList();

    /**
     * 根据name获取功能信息
     *
     * @param name
     * @return
     */
    SysFunction getFunInfoByName(@Param("name") String name);

    /**
     * 根据角色id获取功能列表
     *
     * @param roleId
     * @return
     */
    List<SysFunction> getFunListByRoleId(@Param("roleId") Long roleId);
}
