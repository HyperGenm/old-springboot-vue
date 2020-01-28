package com.weiziplus.springboot.core.api.user.mapper;

import com.weiziplus.springboot.common.models.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2019/5/10 17:05
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

    /**
     * 获取用户列表
     *
     * @return
     */
    List<User> getList();
}
