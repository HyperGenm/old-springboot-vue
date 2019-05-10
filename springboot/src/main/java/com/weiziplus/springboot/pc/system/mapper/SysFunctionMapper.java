package com.weiziplus.springboot.pc.system.mapper;

import com.weiziplus.springboot.common.models.SysFunction;
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
     * 根据角色id和parent_id获取菜单列表
     *
     * @param roleId
     * @param parentId
     * @return
     */
    List<SysFunction> getMenuListByRoleIdAndParentId(@Param("roleId") Long roleId, @Param("parentId") Long parentId);

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
     * 根据父级id获取功能列表
     *
     * @param parentId
     * @return
     */
    List<SysFunction> getFunListByParentId(@Param("parentId") Long parentId);

    /**
     * 根据name获取功能信息
     *
     * @param name
     * @return
     */
    SysFunction getFunInfoByName(@Param("name") String name);

    /**
     * 新增功能
     *
     * @param sysFunction
     * @return
     */
    int addFunction(SysFunction sysFunction);

    /**
     * 修改功能
     *
     * @param sysFunction
     * @return
     */
    int updateFunction(SysFunction sysFunction);

    /**
     * 根据id删除功能
     *
     * @param ids
     * @return
     */
    int deleteFunctionByIds(@Param("ids") Long[] ids);

    /**
     * 根据角色id获取功能列表
     *
     * @param roleId
     * @return
     */
    List<SysFunction> getFunListByRoleId(@Param("roleId") Long roleId);
}
