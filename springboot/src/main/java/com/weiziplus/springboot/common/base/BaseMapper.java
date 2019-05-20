package com.weiziplus.springboot.common.base;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
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
            "INSERT INTO ${TABLE_NAME} ( " +
            "<foreach collection='COLUMNS' item='column' separator=','> " +
            "${column} " +
            "</foreach> " +
            ") VALUES ( " +
            "<foreach collection='VALUES' item='value' separator=','> " +
            "#{value} " +
            "</foreach>" +
            ")" +
            "</script>")
    int insert(Map<String, Object> map);

    /**
     * 根据表名和id删除
     *
     * @param TABLE_NAME
     * @param id
     * @return
     */
    @Delete("DELETE FROM ${TABLE_NAME} " +
            "WHERE id = #{id}")
    int deleteById(@Param("TABLE_NAME") String TABLE_NAME, @Param("id") Long id);

    /**
     * 根据表名和ids删除
     *
     * @param TABLE_NAME
     * @param ids
     * @return
     */
    @Delete("<script>" +
            "DELETE FROM ${TABLE_NAME} " +
            "WHERE id IN (" +
            "<foreach collection='ids' item='id' separator=','> " +
            "#{id} " +
            "</foreach>" +
            ")" +
            "</script>")
    int deleteByIds(@Param("TABLE_NAME") String TABLE_NAME, @Param("ids") Long[] ids);

    /**
     * 修改数据
     *
     * @param map
     * @return
     */
    @Update("<script>" +
            "UPDATE ${TABLE_NAME} " +
            "SET " +
            "<foreach collection='COLUMNS_VALUES' item='item' index='index'> " +
            "${item.column} = #{item.value} " +
            "      <if test='index != VALUES.size() - 1'> " +
            "        , " +
            "      </if> " +
            "</foreach> " +
            "WHERE ${KEY_ID} = #{KEY_VALUE}" +
            "</script>")
    int update(Map<String, Object> map);

    /**
     * 根据id查询数据
     *
     * @param TABLE_NAME
     * @param id
     * @return
     */
    @Select("SELECT * " +
            "FROM ${TABLE_NAME} " +
            "WHERE id = #{id} " +
            "LIMIT 1")
    Map<String, Object> findById(@Param("TABLE_NAME") String TABLE_NAME, @Param("id") Long id);

    /**
     * 获取所有数据
     *
     * @param TABLE_NAME
     * @return
     */
    @Select("SELECT * " +
            "FROM ${TABLE_NAME} " +
            "ORDER BY ${KEY_ID}")
    List<Map<String, Object>> findAll(@Param("TABLE_NAME") String TABLE_NAME);
}
