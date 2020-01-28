package com.weiziplus.springboot.core.pc.dictionary.mapper;

import com.weiziplus.springboot.common.models.DataDictionaryValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author wanglongwei
 * @date 2019/8/5 9:51
 */
@Mapper
public interface DataDictionaryIpFilterMapper {

    /**
     * 获取ip名单列表
     *
     * @param type
     * @return
     */
    Set<String> getIpValueList(@Param("type") Integer type);

    /**
     * 根据ip获取一条数据
     *
     * @param ip
     * @return
     */
    DataDictionaryValue getOneInfoByIp(@Param("ip") String ip);

    /**
     * 获取ip名单列表
     *
     * @param type
     * @return
     */
    List<DataDictionaryValue> getIpList(@Param("type") Integer type);

    /**
     * ip名单根据id删除
     *
     * @param id
     * @return
     */
    int deleteIp(@Param("id") Long id);
}
