package com.weiziplus.springboot.mapper.data.dictionary;

import com.weiziplus.springboot.models.DataDictionaryValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wanglongwei
 * @data 2019/8/5 9:51
 */
@Mapper
public interface DataDictionaryIpFilterMapper {

    /**
     * 获取ip白名单列表
     *
     * @return
     */
    List<String> getIpValueWhiteList();

    /**
     * 获取ip黑名单列表
     *
     * @return
     */
    List<String> getIpValueBlackList();

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
    List<DataDictionaryValue> getIpList(@Param("type") String type);

    /**
     * 新增ip名单
     *
     * @param ip
     * @param type
     * @return
     */
    int addIp(@Param("ip") String ip, @Param("type") String type);

    /**
     * ip名单根据id删除
     *
     * @param id
     * @param type
     * @return
     */
    int deleteIp(@Param("id") Long id, @Param("type") String type);
}
