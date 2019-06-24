package com.weiziplus.springboot.mapper.user;

import com.weiziplus.springboot.models.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wanglongwei
 * @data 2019/5/10 17:05
 */
@Mapper
public interface UserMapper {
    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    User getUserInfoByUsername(@Param("username") String username);

    /**
     * 根据用户id获取用户信息
     *
     * @param userId
     * @return
     */
    User getUserInfoByUserId(@Param("userId") Long userId);

    /**
     * 新增用户
     *
     * @param username
     * @param password
     * @return
     */
    int addUser(@Param("username") String username, @Param("password") String password);
}
