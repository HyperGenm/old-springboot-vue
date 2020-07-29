package com.weiziplus.springboot.common.base;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author wanglongwei
 * @date 2020/05/27 16/21
 */
@Mapper
public interface BaseMapper {

    /**
     * 新增
     *
     * @param map
     * @return
     */
    int insert(Map<String, Object> map);

    /**
     * 新增,返回自增主键
     *
     * @param map
     * @return
     */
    int insertGetAutoIncrementPrimaryKey(Map<String, Object> map);

    /**
     * 新增多个
     *
     * @param map
     * @return
     */
    int insertList(Map<String, Object> map);

    /**
     * 根据表名和id删除
     *
     * @param map
     * @return
     */
    int deleteById(Map<String, Object> map);

    /**
     * 根据表名和ids删除
     *
     * @param map
     * @return
     */
    int deleteByIds(Map<String, Object> map);

    /**
     * 修改数据
     *
     * @param map
     * @return
     */
    int update(Map<String, Object> map);

    /**
     * 根据id查询数据
     *
     * @param map
     * @return
     */
    Map<String, Object> findById(Map<String, Object> map);

    /**
     * 根据id查询数据
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> findByIds(Map<String, Object> map);

    /**
     * 根据表名和字段以及值获取一条数据
     *
     * @param map
     * @return
     */
    Map<String, Object> findOneDataByColumn(Map<String, Object> map);

    /**
     * 根据表名和字段以及值map获取一条数据
     *
     * @param map
     * @return
     */
    Map<String, Object> findOneDataByColumnMap(Map<String, Object> map);

    /**
     * 根据表名和baseWhere数组获取一条数据
     *
     * @param map
     * @return
     */
    Map<String, Object> findOneDataByTableNameAndBaseWhereList(Map<String, Object> map);

    /**
     * 根据表名和字段以及值获取列表
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> findListByColumn(Map<String, Object> map);

    /**
     * 根据表名和字段以及值map获取一条数据
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> findListByColumnMap(Map<String, Object> map);

    /**
     * 根据表名和baseWhere数组获取数组
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> findListByTableNameAndBaseWhereList(Map<String, Object> map);

    /**
     * 获取所有数据
     *
     * @param tableName
     * @return
     */
    List<Map<String, Object>> findAll(@Param("TABLE_NAME") String tableName);

    /**
     * 获取所有数据按照某个字段降序排列
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> findAllOrderByColumnDesc(Map<String, String> map);

    /**
     * 获取所有数据按照某个字段升序排列
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> findAllOrderByColumnAsc(Map<String, String> map);

}
