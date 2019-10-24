package com.weiziplus.springboot.mapper.system;

import com.weiziplus.springboot.models.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2019/5/9 14:04
 */
@Mapper
public interface SysRoleMapper {
    /**
     * 根据用户id获取用户角色信息
     *
     * @param userId
     * @return
     */
    SysRole getInfoByUserId(@Param("userId") Long userId);

    /**
     * 根据父级id获取角色列表
     *
     * @param parentId
     * @return
     */
    List<SysRole> getRoleListByParentId(@Param("parentId") Long parentId);

    /**
     * 获取parent_id最小的功能信息
     *
     * @return
     */
    @Select("SELECT * " +
            "FROM sys_role " +
            "ORDER BY parent_id ASC " +
            "LIMIT 1")
    SysRole getMinParentIdRoleInfo();

    /**
     * 根据name获取角色信息
     *
     * @param name
     * @return
     */
    SysRole getRoleInfoByName(@Param("name") String name);

    /**
     * 根据id和isStop修改角色状态
     *
     * @param id
     * @param isStop
     * @return
     */
    int changeRoleIsStopByIdAndIsStop(@Param("id") Long id, @Param("isStop") Integer isStop);

    /**
     * 根据角色id获取角色信息
     *
     * @param id
     * @return
     */
    SysRole getInfoByRoleId(@Param("id") Long id);

    /**
     * 获取所有角色列表
     *
     * @return
     */
    List<SysRole> getRoleList();
}
