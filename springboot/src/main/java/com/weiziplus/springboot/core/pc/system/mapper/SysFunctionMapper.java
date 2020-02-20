package com.weiziplus.springboot.core.pc.system.mapper;

import com.weiziplus.springboot.common.models.SysFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2019/5/9 12:00
 */
@Mapper
public interface SysFunctionMapper {

    /**
     * 根据角色id获取parent_id最小的功能信息---菜单
     *
     * @param roleId
     * @return
     */
    SysFunction getMinParentIdMenuFunInfoByRoleId(@Param("roleId") Integer roleId);

    /**
     * 根据角色id获取菜单列表
     *
     * @param roleId
     * @return
     */
    List<SysFunction> getMenuListByRoleId(@Param("roleId") Integer roleId);

    /**
     * 根据roleId获取按钮列表
     *
     * @param roleId
     * @return
     */
    List<SysFunction> getButtonListByRoleId(@Param("roleId") Integer roleId);

    /**
     * 获取parent_id最小的功能信息
     *
     * @return
     */
    @Select("SELECT * " +
            "FROM sys_function " +
            "ORDER BY parent_id ASC " +
            "LIMIT 1")
    SysFunction getMinParentIdFunInfo();

    /**
     * 获取功能列表
     *
     * @return
     */
    @Select("SELECT * " +
            "FROM sys_function " +
            "ORDER BY parent_id ASC,sort ASC")
    List<SysFunction> getAllFunList();

    /**
     * 根据父级id获取功能列表
     *
     * @param parentId
     * @return
     */
    List<SysFunction> getFunListByParentId(@Param("parentId") Integer parentId);

    /**
     * 根据父级id获取功能列表不包含按钮
     *
     * @return
     */
    @Select("SELECT * " +
            "FROM sys_function " +
            "WHERE `type` = 0 " +
            "ORDER BY parent_id ASC,sort ASC")
    List<SysFunction> getFunNotButtonList();

    /**
     * 根据角色id获取功能列表
     *
     * @param roleId
     * @return
     */
    List<SysFunction> getFunListByRoleId(@Param("roleId") Integer roleId);
}
