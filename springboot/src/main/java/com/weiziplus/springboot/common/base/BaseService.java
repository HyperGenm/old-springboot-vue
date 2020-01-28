package com.weiziplus.springboot.common.base;

import com.weiziplus.springboot.common.util.ToolUtils;
import com.weiziplus.springboot.common.util.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 基础service,封装了常用的增删改查
 *
 * @author wanglongwei
 * @date 2019/5/20 10:44
 */
@Slf4j
public class BaseService {

    @Autowired
    BaseMapper mapper;

    /**
     * BaseService基础redis的key
     */
    private static final String BASE_REDIS_KEY = "base:BaseService:";

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
        String key = createRedisKey(BASE_REDIS_KEY + "getTableName:", nowClass.getName());
        Object redisObject = RedisUtils.get(key);
        if (null != redisObject) {
            return String.valueOf(redisObject);
        }
        Table table = (Table) nowClass.getAnnotation(Table.class);
        String value = table.value();
        RedisUtils.set(key, value, 60L * 60 * 3);
        return value;
    }

    /**
     * 根据实体对象获取数据库表名
     *
     * @param object
     * @return
     */
    private String getTableName(Object object) {
        if (null == object || null == object.getClass()) {
            throw new RuntimeException("将实体对象转为map===Object为null");
        }
        //获取实体类对应数据库的表名
        if (null == object.getClass().getAnnotation(Table.class)) {
            throw new RuntimeException("当前实体类没有设置@Table注解==========" + object.getClass());
        }
        String key = createRedisKey(BASE_REDIS_KEY + "getTableName:", object.getClass().getName());
        Object redisObject = RedisUtils.get(key);
        if (null != redisObject) {
            return String.valueOf(redisObject);
        }
        String value = object.getClass().getAnnotation(Table.class).value();
        RedisUtils.set(key, value, 60L * 60 * 3);
        return value;
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
        String key = createRedisKey(BASE_REDIS_KEY + "getPrimaryKey:", nowClass.getName());
        Object redisObject = RedisUtils.get(key);
        if (null != redisObject) {
            return String.valueOf(redisObject);
        }
        Field[] fields = nowClass.getDeclaredFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(Id.class)) {
                String value = field.getAnnotation(Id.class).value();
                RedisUtils.set(key, value, 60L * 60 * 3);
                return value;
            }
        }
        throw new RuntimeException("当前实体类没有设置主键==========" + nowClass);
    }

    /**
     * 根据实体类对象插入数据
     *
     * @param object
     * @return
     */
    private Map<String, Object> handleTableInsert(Object object) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("TABLE_NAME", getTableName(object));

        //存放表的字段
        List<String> columns = new ArrayList<>();
        //存放表的字段值
        List<Object> values = new ArrayList<>();
        //获取实体类所有变量
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            //查看变量是否有注解
            if (null == field.getAnnotation(Column.class) && null == field.getAnnotation(Id.class)) {
                continue;
            }
            //获取属性值
            Object value = getValueByObjectAndField(object, field);
            //如果值为null，则不处理
            if (null == value) {
                continue;
            }
            if (null != field.getAnnotation(Id.class)) {
                //添加表的主键
                columns.add(field.getAnnotation(Id.class).value());
                result.put("KEY_ID", field.getAnnotation(Id.class).value());
            } else {
                //添加表的普通字段
                columns.add(field.getAnnotation(Column.class).value());
            }
            values.add(value);
        }
        if (columns.size() != values.size()) {
            throw new RuntimeException("实体类反射字段和值的数量不一致" + object.getClass());
        }
        result.put("COLUMNS", columns);
        result.put("VALUES", values);
        return result;
    }

    /**
     * 根据实体类数组插入数据
     *
     * @param list
     * @return
     */
    private <T> Map<String, Object> handleTableListInsert(List<T> list) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("TABLE_NAME", getTableName(list.get(0)));

        //存放表的字段
        List<String> columns = new ArrayList<>();
        //存放字段的值
        List<List<Object>> valuesList = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            //存放表的字段值
            List<Object> values = new ArrayList<>();
            //获取实体类所有变量
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                //查看变量是否有注解
                if (null == field.getAnnotation(Column.class) && null == field.getAnnotation(Id.class)) {
                    continue;
                }
                //获取属性值
                Object value = getValueByObjectAndField(object, field);
                values.add(value);
                //第一次循环将字段添加到数组里面
                if (i > 0) {
                    continue;
                }
                if (null != field.getAnnotation(Id.class)) {
                    //添加表的主键
                    columns.add(field.getAnnotation(Id.class).value());
                } else {
                    //添加表的普通字段
                    columns.add(field.getAnnotation(Column.class).value());
                }
            }
            valuesList.add(values);
        }
        result.put("COLUMNS", columns);
        result.put("VALUES_LIST", valuesList);
        return result;
    }

    /**
     * 根据实体类对象更新数据
     *
     * @param object
     * @return
     */
    private Map<String, Object> handleTableUpdate(Object object) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("TABLE_NAME", getTableName(object));

        //存放表的字段和字段值
        List<Map<String, Object>> columnValues = new ArrayList<>();
        //获取实体类所有变量
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            //查看变量是否有注解
            if (null == field.getAnnotation(Column.class) && null == field.getAnnotation(Id.class)) {
                continue;
            }
            //获取属性值
            Object value = getValueByObjectAndField(object, field);
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
            columnValues.add(new HashMap<String, Object>(2) {{
                put("column", field.getAnnotation(Column.class).value());
                put("value", value);
            }});
        }
        //如果没有需要更新的字段
        if (0 >= columnValues.size()) {
            return null;
        }
        result.put("COLUMNS_VALUES", columnValues);
        return result;
    }

    /**
     * 根据对象和属性获取属性值
     *
     * @param object
     * @param field
     * @return
     */
    private Object getValueByObjectAndField(Object object, Field field) {
        if (null == object || null == field) {
            return null;
        }
        Object value = null;
        try {
            field.setAccessible(true);
            value = field.get(object);
            field.setAccessible(false);
        } catch (Exception e) {
            throw new RuntimeException("实体类get方法出错==========" + object.getClass() + "---" + e);
        }
        return value;
    }

    /**
     * 新增
     *
     * @param object
     * @return
     */
    protected int baseInsert(Object object) {
        return baseInsert(object, false);
    }

    /**
     * 新增
     *
     * @param object
     * @param getAutoIncrementPrimaryKey 自增主键赋值给实体变量
     * @return
     */
    protected int baseInsert(Object object, boolean getAutoIncrementPrimaryKey) {
        if (null == object) {
            return 0;
        }
        Map<String, Object> map = handleTableInsert(object);
        int insert = mapper.insert(map);
        if (!getAutoIncrementPrimaryKey) {
            return insert;
        }
        //获取实体类所有变量
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            //获取表的主键
            if (null == field || null == field.getAnnotation(Id.class)) {
                continue;
            }
            try {
                String typeName = field.getGenericType().getTypeName();
                String keyId = ToolUtils.valueOfString(map.get("KEY_ID"));
                field.setAccessible(true);
                if (Long.class.getTypeName().equals(typeName)) {
                    field.set(object, ToolUtils.valueOfLong(keyId));
                } else if (String.class.getTypeName().equals(typeName)) {
                    field.set(object, keyId);
                } else if (Integer.class.getTypeName().equals(typeName)) {
                    field.set(object, ToolUtils.valueOfInteger(keyId));
                }
                field.setAccessible(false);
                break;
            } catch (IllegalAccessException e) {
                log.warn("baseInsert插入数据错误，详情:" + e);
            }
        }
        return insert;
    }

    /**
     * 新增多个
     *
     * @param list
     * @return
     */
    protected <T> int baseInsertList(List<T> list) {
        if (null == list || 0 >= list.size()) {
            return 0;
        }
        return mapper.insertList(handleTableListInsert(list));
    }

    /**
     * 删除
     *
     * @param nowClass
     * @param id
     * @return
     */
    protected int baseDeleteByClassAndId(Class nowClass, Long id) {
        if (null == nowClass || null == id) {
            return 0;
        }
        return mapper.deleteById(new HashMap<String, Object>(2) {{
            put("TABLE_NAME", getTableName(nowClass));
            put("id", id);
        }});
    }

    /**
     * 删除
     *
     * @param nowClass
     * @param id
     * @return
     */
    protected int baseDeleteByClassAndId(Class nowClass, Integer id) {
        return baseDeleteByClassAndId(nowClass, Long.valueOf(id));
    }

    /**
     * 删除
     *
     * @param nowClass
     * @param id
     * @return
     */
    protected int baseDeleteByClassAndId(Class nowClass, String id) {
        if (null == nowClass || null == id || ToolUtils.isBlank(id)) {
            return 0;
        }
        return mapper.deleteById(new HashMap<String, Object>(2) {{
            put("TABLE_NAME", getTableName(nowClass));
            put("id", id);
        }});
    }

    /**
     * 删除多个
     *
     * @param nowClass
     * @param ids
     * @return
     */
    protected int baseDeleteByClassAndIds(Class nowClass, Long[] ids) {
        if (null == nowClass || null == ids || 0 >= ids.length) {
            return 0;
        }
        return mapper.deleteByIds(new HashMap<String, Object>(2) {{
            put("TABLE_NAME", getTableName(nowClass));
            put("ids", ids);
        }});
    }

    /**
     * 删除多个
     *
     * @param nowClass
     * @param ids
     * @return
     */
    protected int baseDeleteByClassAndIds(Class nowClass, Integer[] ids) {
        if (null == nowClass || null == ids || 0 >= ids.length) {
            return 0;
        }
        List<Long> longList = new ArrayList<>(ids.length);
        for (Integer id : ids) {
            longList.add(Long.valueOf(id));
        }
        Long[] longs = new Long[]{};
        return baseDeleteByClassAndIds(nowClass, longList.toArray(longs));
    }

    /**
     * 删除多个
     *
     * @param nowClass
     * @param ids
     * @return
     */
    protected int baseDeleteByClassAndIds(Class nowClass, String[] ids) {
        if (null == nowClass || null == ids || 0 >= ids.length) {
            return 0;
        }
        return mapper.deleteByIds(new HashMap<String, Object>(2) {{
            put("TABLE_NAME", getTableName(nowClass));
            put("ids", ids);
        }});
    }

    /**
     * 修改数据
     *
     * @param object
     * @return
     */
    protected int baseUpdate(Object object) {
        Map<String, Object> stringObjectMap = handleTableUpdate(object);
        //没有需要更新的字段
        if (null == stringObjectMap) {
            return 0;
        }
        return mapper.update(stringObjectMap);
    }

    /**
     * 根据id查询
     *
     * @param nowClass
     * @param id
     * @return
     */
    protected Map<String, Object> baseFindByClassAndId(Class nowClass, Long id) {
        if (null == nowClass || null == id) {
            return null;
        }
        return mapper.findById(new HashMap<String, Object>(2) {{
            put("TABLE_NAME", getTableName(nowClass));
            put("id", id);
        }});
    }

    /**
     * 根据id查询
     *
     * @param nowClass
     * @param id
     * @return
     */
    protected Map<String, Object> baseFindByClassAndId(Class nowClass, Integer id) {
        return baseFindByClassAndId(nowClass, Long.valueOf(id));
    }

    /**
     * 根据id查询
     *
     * @param nowClass
     * @param id
     * @return
     */
    protected Map<String, Object> baseFindByClassAndId(Class nowClass, String id) {
        if (null == nowClass || null == id || ToolUtils.isBlank(id)) {
            return null;
        }
        return mapper.findById(new HashMap<String, Object>(2) {{
            put("TABLE_NAME", getTableName(nowClass));
            put("id", id);
        }});
    }

    /**
     * 降序
     */
    private final static String DESC = "DESC";

    /**
     * 升序
     */
    private final static String ASC = "ASC";

    /**
     * 获取所有数据---不指定排序字段，默认主键降序
     *
     * @param nowClass
     * @return
     */
    protected List<Map<String, Object>> baseFindAllByClass(Class nowClass) {
        return mapper.findAll(new HashMap<String, String>(3) {{
            put("TABLE_NAME", getTableName(nowClass));
            put("orderColumn", getPrimaryKey(nowClass));
            put("desc", DESC);
        }});
    }

    /**
     * 获取所有数据
     *
     * @param nowClass
     * @return
     */
    protected List<Map<String, Object>> baseFindAllByClass(Class nowClass, String orderColumn, String desc) {
        if (ToolUtils.isBlank(orderColumn)) {
            throw new RuntimeException("获取所有数据baseFindAllByClass，排序字段错误，class:" + nowClass);
        }
        orderColumn = orderColumn.trim();
        //空格
        String blankSpace = " ";
        if (orderColumn.contains(blankSpace)) {
            throw new RuntimeException("获取所有数据baseFindAllByClass，排序字段错误,内部不能包含空格,class:" + nowClass);
        }
        if (!DESC.equalsIgnoreCase(desc) && !ASC.equalsIgnoreCase(desc)) {
            throw new RuntimeException("获取所有数据baseFindAllByClass，升序、降序字段错误,class:" + nowClass);
        }
        Map<String, String> param = new HashMap<>(3);
        param.put("TABLE_NAME", getTableName(nowClass));
        param.put("orderColumn", orderColumn);
        param.put("desc", desc.toUpperCase());
        return mapper.findAll(param);
    }

    /**
     * 根据唯一前缀和方法参数创建唯一redis的key
     *
     * @param onlyPrefix
     * @param objects
     * @return
     */
    protected String createRedisKey(String onlyPrefix, Object... objects) {
        StringBuffer stringBuffer = new StringBuffer(ToolUtils.getLocalUserDirMd5());
        stringBuffer.append(":");
        stringBuffer.append(onlyPrefix);
        for (Object object : objects) {
            stringBuffer.append("_&&&_").append(object);
        }
        return ToolUtils.valueOfString(stringBuffer);
    }
}
