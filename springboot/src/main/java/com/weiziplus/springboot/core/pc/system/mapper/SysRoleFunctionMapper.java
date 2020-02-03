package com.weiziplus.springboot.core.pc.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wanglongwei
 * @date 2019/5/10 8:54
 */
@Mapper
public interface SysRoleFunctionMapper {
    /**
     * 根据权限id删除权限的功能
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(@Param("roleId") Integer roleId);

    /**
     * 新增权限功能
     *
     * @param roleId
     * @param funIds
     * @return
     */
    int addRoleFunction(@Param("roleId") Integer roleId, @Param("funIds") Integer[] funIds);
}
