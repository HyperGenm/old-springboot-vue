package com.weiziplus.springboot.mapper.system;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wanglongwei
 * @data 2019/5/13 15:07
 */
@Mapper
public interface SysLogMapper {
    /**
     * 添加系统日志
     *
     * @param userId
     * @param description
     * @param ip
     * @return
     */
    int addSysLog(@Param("userId") Long userId, @Param("description") String description, @Param("ip") String ip);

    /**
     * 获取日志列表
     *
     * @param username
     * @param roleId
     * @param description
     * @param ipAddress
     * @param createTime
     * @return
     */
    List<Map<String, Object>> getLogList(@Param("username") String username, @Param("roleId") Long roleId,
                                         @Param("description") String description, @Param("ipAddress") String ipAddress,
                                         @Param("createTime") String createTime);
}
