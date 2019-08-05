package com.weiziplus.springboot.base;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础service
 *
 * @author wanglongwei
 * @data 2019/5/20 10:44
 */
public class BaseService {

    @Autowired
    BaseMapper mapper;

    /**
     * 根据实体类class获取数据库表名
     *
     * @param nowClass
     * @return
     */
    private String getTableName(Class nowClass) {
        if (null == nowClass.getAnnotation(Table.class)) {
            throw new RuntimeException("当前实体类没有设置@Table注解==========" + nowClass);
        }
        Table table = (Table) nowClass.getAnnotation(Table.class);
        return table.value();
    }

    /**
     * 根据实体类class获取数据库表名
     *
     * @param nowClass
     * @return
     */
    private String getPrimaryKey(Class nowClass) {
        if (null == nowClass.getAnnotation(Table.class)) {
            throw new RuntimeException("当前实体类没有设置@Table注解==========" + nowClass);
        }
        Field[] fields = nowClass.getDeclaredFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(Id.class)) {
                return field.getAnnotation(Id.class).value();
            }
        }
        throw new RuntimeException("当前实体类没有设置主键==========" + nowClass);
    }

    /**
     * 将实体对象转为map
     *
     * @param object
     * @return
     */
    private Map<String, Object> handleTable(Object object) {
        if (null == object || null == object.getClass()) {
            throw new RuntimeException("将实体对象转为map===Object为null");
        }
        //获取实体类对应数据库的表名
        if (null == object.getClass().getAnnotation(Table.class)) {
            throw new RuntimeException("当前实体类没有设置@Table注解==========" + object.getClass());
        }
        Map<String, Object> result = new HashMap<>(3);
        result.put("TABLE_NAME", object.getClass().getAnnotation(Table.class).value());

        //存放表的字段
        List<String> columns = new ArrayList<>();
        //存放表的字段值
        List<Object> values = new ArrayList<>();
        //存放表的字段和字段值
        List<Map<String, Object>> columnValues = new ArrayList<>();
        //获取实体类所有变量
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            //查看变量是否有注解
            if (null == field.getAnnotation(Column.class) && null == field.getAnnotation(Id.class)) {
                continue;
            }
            //通过反射get方法获取变量值
            String getMethod = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            Object value;
            try {
                Method method = object.getClass().getMethod(getMethod);
                value = method.invoke(object);
            } catch (Exception e) {
                throw new RuntimeException("实体类get方法出错==========" + object.getClass() + "---" + e);
            }
            //获取表的主键
            if (null != field.getAnnotation(Id.class)) {
                result.put("KEY_ID", field.getAnnotation(Id.class).value());
                result.put("KEY_VALUE", value);
                continue;
            }
            //如果值为null，则不处理
            if (null == value) {
                continue;
            }
            columns.add(field.getAnnotation(Column.class).value());
            values.add(value);
            Map<String, Object> map = new HashMap<>(2);
            map.put("column", field.getAnnotation(Column.class).value());
            map.put("value", value);
            columnValues.add(map);
        }
        if (columns.size() != values.size()) {
            throw new RuntimeException("实体类反射字段和值的数量不一致" + object.getClass());
        }
        result.put("COLUMNS", columns);
        result.put("VALUES", values);
        result.put("COLUMNS_VALUES", columnValues);
        return result;
    }

    /**
     * 新增
     *
     * @param object
     * @return
     */
    public int baseInsert(Object object) {
        return mapper.insert(handleTable(object));
    }

    /**
     * 删除
     *
     * @param nowClass
     * @param id
     * @return
     */
    public int baseDeleteByClassAndId(Class nowClass, Long id) {
        if (null == nowClass || null == id) {
            return 0;
        }
        return mapper.deleteById(getTableName(nowClass), id);
    }

    /**
     * 删除多个
     *
     * @param nowClass
     * @param ids
     * @return
     */
    public int baseDeleteByClassAndIds(Class nowClass, Long[] ids) {
        if (null == nowClass || null == ids) {
            return 0;
        }
        return mapper.deleteByIds(getTableName(nowClass), ids);
    }

    /**
     * 修改数据
     *
     * @param object
     * @return
     */
    public int baseUpdate(Object object) {
        return mapper.update(handleTable(object));
    }

    /**
     * 根据id查询
     *
     * @param nowClass
     * @param id
     * @return
     */
    public Map<String, Object> baseFindByClassAndId(Class nowClass, Long id) {
        if (null == nowClass || null == id) {
            return null;
        }
        return mapper.findById(getTableName(nowClass), id);
    }

    /**
     * 获取所有数据
     *
     * @param nowClass
     * @return
     */
    public List<Map<String, Object>> baseFindAllByClass(Class nowClass) {
        return mapper.findAll(getTableName(nowClass), getPrimaryKey(nowClass));
    }
}
