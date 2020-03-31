package com.weiziplus.springboot.core.pc.system.mapper;

import com.weiziplus.springboot.common.models.SysRole;
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
    List<SysRole> getRoleListByParentId(@Param("parentId") Integer parentId);

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
     * 获取所有角色列表
     *
     * @return
     */
    @Select("SELECT * " +
            "FROM sys_role " +
            "ORDER BY sort ASC,parent_id ASC")
    List<SysRole> getRoleList();
}
