package com.weiziplus.springboot.core.pc.system.mapper;

import com.weiziplus.springboot.common.models.SysError;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/03/21 15/21
 */
@Mapper
public interface SysErrorMapper {

    /**
     * 获取列表
     *
     * @param createTime
     * @return
     */
    List<SysError> getList(@Param("createTime") String createTime);

}
