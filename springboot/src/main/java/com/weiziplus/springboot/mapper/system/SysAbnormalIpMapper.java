package com.weiziplus.springboot.mapper.system;

import com.weiziplus.springboot.models.SysAbnormalIp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wanglongwei
 * @data 2019/8/5 14:21
 */
@Mapper
public interface SysAbnormalIpMapper {

    /**
     * 新增异常ip
     *
     * @param ip
     * @return
     */
    int addAbnormalIp(@Param("ip") String ip);

    /**
     * 根据ip获取一条数据
     *
     * @param ip
     * @return
     */
    SysAbnormalIp getOneInfoByIp(@Param("ip") String ip);

    /**
     * 更新异常ip
     *
     * @param ip
     * @return
     */
    int updateAbnormalIp(@Param("ip") String ip);
}
