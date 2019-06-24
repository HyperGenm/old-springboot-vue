package com.weiziplus.springboot.mapper.system;

import com.weiziplus.springboot.models.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/9 11:24
 */
@Mapper
public interface SysUserMapper {
    /**
     * 根据登录名获取用户信息
     *
     * @param username
     * @return
     */
    SysUser getInfoByUsername(@Param("username") String username);

    /**
     * 获取系统用户列表
     *
     * @param userName
     * @param allowLogin
     * @param createTime
     * @return
     */
    List<Map<String, Object>> getUserList(@Param("userName") String userName, @Param("allowLogin") Integer allowLogin, @Param("createTime") String createTime);

    /**
     * 根据用户名获取用户信息
     *
     * @param userName
     * @return
     */
    SysUser getUserInfoByName(@Param("userName") String userName);

    /**
     * 根据用户id和密码重置密码
     *
     * @param userId
     * @param password
     * @return
     */
    int resetUserPassword(@Param("userId") Long userId, @Param("password") String password);

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    SysUser getInfoById(@Param("id") Long id);

    /**
     * 根据id更新用户最后活跃时间
     *
     * @param id
     * @return
     */
    int updateLastActiveTimeById(@Param("id") Long id);

    /**
     * 更新用户角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    int updateRoleIdByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
