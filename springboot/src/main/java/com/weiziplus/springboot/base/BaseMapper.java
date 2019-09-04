package com.weiziplus.springboot.base;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 基础mapper
 *
 * @author wanglongwei
 * @data 2019/5/20 10:44
 */
@Mapper
public interface BaseMapper {
    /**
     * 新增
     *
     * @param map
     * @return
     */
    @Insert("<script>" +
            "INSERT INTO `${TABLE_NAME}` \r\n ( " +
            "<foreach collection='COLUMNS' item='column' separator=','> " +
            "`${column}` " +
            "</foreach> " +
            ") \r\n VALUES \r\n ( \r\n " +
            "<foreach collection='VALUES' item='value' separator=','> " +
            "#{value} " +
            "</foreach>" +
            " \r\n )" +
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "KEY_ID")
    int insert(Map<String, Object> map);

    /**
     * 新增多个
     *
     * @param map
     * @return
     */
    @Insert("<script>" +
            "INSERT INTO `${TABLE_NAME}` \r\n( " +
            "<foreach collection='COLUMNS' item='column' separator=','> " +
            "`${column}` " +
            "</foreach> " +
            ") \r\n VALUES \r\n " +
            "<foreach collection='VALUES_LIST' item='values' separator=','> " +
            "( \r\n " +
            " <foreach collection='values' item='value' separator=','> " +
            " #{value} " +
            " </foreach> " +
            " \r\n )" +
            "</foreach>" +
            "</script>")
    int insertList(Map<String, Object> map);

    /**
     * 根据表名和id删除
     *
     * @param map
     * @return
     */
    @Delete("DELETE FROM `${TABLE_NAME}` \r\n " +
            "WHERE id = #{id}")
    int deleteById(Map<String, Object> map);

    /**
     * 根据表名和ids删除
     *
     * @param map
     * @return
     */
    @Delete("<script>" +
            "DELETE FROM `${TABLE_NAME}` \r\n" +
            "WHERE `id` IN ( \r\n " +
            "<foreach collection='ids' item='id' separator=','> " +
            "#{id} " +
            "</foreach>" +
            " \r\n )" +
            "</script>")
    int deleteByIds(Map<String, Object> map);

    /**
     * 修改数据
     *
     * @param map
     * @return
     */
    @Update("<script>" +
            "UPDATE `${TABLE_NAME}` \r\n " +
            "SET \r\n " +
            "<foreach collection='COLUMNS_VALUES' item='item' separator=','> " +
            "`${item.column}` = #{item.value} " +
            "</foreach> \r\n " +
            "WHERE `${KEY_ID}` = #{KEY_VALUE}" +
            "</script>")
    int update(Map<String, Object> map);

    /**
     * 根据id查询数据
     *
     * @param map
     * @return
     */
    @Select("SELECT * \r\n " +
            "FROM `${TABLE_NAME}` \r\n " +
            "WHERE `id` = #{id}")
    Map<String, Object> findById(Map<String, Object> map);

    /**
     * 获取所有数据
     *
     * @param map
     * @return
     */
    @Select("SELECT * \r\n " +
            "FROM `${TABLE_NAME}` \r\n " +
            "ORDER BY #{orderColumn} #{desc}")
    List<Map<String, Object>> findAll(Map<String, String> map);
}
