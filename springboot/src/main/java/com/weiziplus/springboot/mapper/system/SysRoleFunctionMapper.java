package com.weiziplus.springboot.mapper.system;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wanglongwei
 * @data 2019/5/10 8:54
 */
@Mapper
public interface SysRoleFunctionMapper {
    /**
     * 根据权限id删除权限的功能
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 新增权限功能
     *
     * @param roleId
     * @param funIds
     * @return
     */
    int addRoleFunction(@Param("roleId") Long roleId, @Param("funIds") Long[] funIds);
}
