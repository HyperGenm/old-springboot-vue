package com.weiziplus.springboot.core.pc.dictionary.mapper;

import com.weiziplus.springboot.common.models.DataDictionaryValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2019/8/5 9:51
 */
@Mapper
public interface DataDictionaryAbnormalIpMapper {

    /**
     * 根据ip获取一条数据
     *
     * @param ip
     * @return
     */
    DataDictionaryValue getOneInfoByIp(@Param("ip") String ip);

    /**
     * 获取列表数据
     *
     * @return
     */
    List<DataDictionaryValue> getList();
}
