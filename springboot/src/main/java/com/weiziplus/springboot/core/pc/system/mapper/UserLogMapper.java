package com.weiziplus.springboot.core.pc.system.mapper;

import com.weiziplus.springboot.core.pc.system.vo.LogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2019/5/13 15:07
 */
@Mapper
public interface UserLogMapper {

    /**
     * 获取日志列表
     *
     * @param username
     * @param description
     * @param type
     * @param ipAddress
     * @param startTime
     * @param endTime
     * @return
     */
    List<LogVo> getList(@Param("username") String username, @Param("description") String description,
                        @Param("type") Integer type, @Param("ipAddress") String ipAddress,
                        @Param("startTime") String startTime, @Param("endTime") String endTime);
}
