package com.weiziplus.springboot.core.pc.dictionary.mapper;

import com.weiziplus.springboot.common.models.DataDictionaryValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author wanglongwei
 * @date 2020/02/24 20/22
 */
@Mapper
public interface DataDictionaryIpManagerMapper {

    /**
     * 获取ip名单
     *
     * @param ipAddress
     * @param type
     * @return
     */
    List<DataDictionaryValue> getIpList(@Param("ipAddress") String ipAddress, @Param("type") String type);

    /**
     * 根据类型获取ip值的列表
     *
     * @param type
     * @return
     */
    @Select("" +
            "SELECT value " +
            "FROM data_dictionary_value " +
            "WHERE dictionary_code = #{type} ")
    Set<String> getIpValueList(@Param("type") String type);

    /**
     * 根据ip地址获取一条异常ip数据
     *
     * @param ipAddress
     * @return
     */
    @Select("" +
            "SELECT * " +
            "FROM data_dictionary_value " +
            "WHERE dictionary_code IN ('ipListAbnormal','ipListWhite','ipListBlack') AND value = #{ipAddress} " +
            "LIMIT 1")
    DataDictionaryValue getOneIpInfoByIpAddress(@Param("ipAddress") String ipAddress);

}
