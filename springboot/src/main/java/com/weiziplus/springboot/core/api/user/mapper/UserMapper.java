package com.weiziplus.springboot.core.api.user.mapper;

import com.weiziplus.springboot.common.models.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     * 新增用户
     *
     * @param username
     * @param password
     * @return
     */
    int addUser(@Param("username") String username, @Param("password") String password);

}
