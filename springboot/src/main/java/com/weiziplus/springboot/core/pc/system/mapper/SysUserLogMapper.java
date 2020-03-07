package com.weiziplus.springboot.core.pc.system.mapper;

import com.weiziplus.springboot.core.pc.system.vo.SysLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2019/5/13 15:07
 */
@Mapper
public interface SysUserLogMapper {

    /**
     * 获取日志列表
     *
     * @param isSuperAdmin 当前是否是超级管理员,0：是
     * @param username
     * @param roleId
     * @param url
     * @param type
     * @param description
     * @param ipAddress
     * @param startTime
     * @param endTime
     * @return
     */
    List<SysLogVo> getList(@Param("isSuperAdmin") Integer isSuperAdmin,
                           @Param("username") String username, @Param("roleId") Long roleId,
                           @Param("url") String url, @Param("type") Integer type,
                           @Param("description") String description, @Param("ipAddress") String ipAddress,
                           @Param("startTime") String startTime, @Param("endTime") String endTime);
}
