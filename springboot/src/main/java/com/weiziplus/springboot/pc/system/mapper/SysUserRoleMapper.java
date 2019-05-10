package com.weiziplus.springboot.pc.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wanglongwei
 * @data 2019/5/10 9:15
 */
@Mapper
public interface SysUserRoleMapper {

    /**
     * 根据用户id和角色id删除用户角色信息
     *
     * @param userId
     * @param roleId
     * @return
     */
    int deleteUserRoleByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);


    /**
     * 根据用户id和角色id新增用户角色信息
     *
     * @param userId
     * @param roleId
     * @return
     */
    int addUserRoleByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
