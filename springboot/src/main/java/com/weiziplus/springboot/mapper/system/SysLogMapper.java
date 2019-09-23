package com.weiziplus.springboot.mapper.system;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wanglongwei
 * @date 2019/5/13 15:07
 */
@Mapper
public interface SysLogMapper {

    /**
     * 获取日志列表
     *
     * @param isSuperAdmin 当前是否是超级管理员,0：是
     * @param username
     * @param roleId
     * @param description
     * @param ipAddress
     * @param createTime
     * @return
     */
    List<Map<String, Object>> getList(@Param("isSuperAdmin") Integer isSuperAdmin,
                                      @Param("username") String username, @Param("roleId") Long roleId,
                                      @Param("description") String description, @Param("ipAddress") String ipAddress,
                                      @Param("createTime") String createTime);
}
