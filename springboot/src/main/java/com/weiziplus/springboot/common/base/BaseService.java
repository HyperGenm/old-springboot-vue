package com.weiziplus.springboot.common.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.util.Md5Utils;
import com.weiziplus.springboot.common.util.ToolUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wanglongwei
 * @date 2020/05/27 16/21
 */
@Slf4j
public class BaseService {

    @Autowired
    BaseMapper mapper;

    /**
     * 表的主键对应实体类的字段
     */
    private static Map<String, String> TABLE_PRIMARY_KEY_FIELD_MAP = new HashMap<>();

    /**
     * 实体类是否存在某个字段
     */
    private static Map<String, Boolean> CLASS_IS_CONTAINS_COLUMN_MAP = new HashMap<>();

    @Getter
    public enum OrderByType {

        /**
         * 排序的类型
         */
        ASC("升序", "ASC"),
        DESC("降序", "DESC");
        private String name;
        private String value;

        OrderByType(String name, String value) {
            this.name = name;
            this.value = value;
        }

        /**
         * 是否存在
         *
         * @param value
         * @return
         */
        public static boolean contains(String value) {
            if (null == value) {
                return false;
            }
            for (OrderByType orderByType : OrderByType.values()) {
                if (orderByType.getValue().equalsIgnoreCase(value)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 根据当前class创建唯一的redis的key的前缀
     *
     * @return
     */
    protected static String createOnlyRedisKeyPrefix() {
        //获取项目路径
        String property = System.getProperty("user.dir");
        //获取调用该方法的class
        String name = new Exception().getStackTrace()[1].getClassName();
        //如果是生产环境
        if (GlobalConfig.isSpringProfilesPro()) {
            return Md5Utils.encode16(property + name) + ":";
        }
        return Md5Utils.encode16(property) + name + ":";
    }

    /**
     * 根据唯一前缀和方法参数创建唯一redis的key
     *
     * @param onlyPrefix
     * @param objects
     * @return
     */
    protected String createRedisKey(String onlyPrefix, Object... objects) {
        StringBuffer stringBuffer = new StringBuffer(onlyPrefix);
        for (Object object : objects) {
            stringBuffer.append(object).append("_&_");
        }
        return ToolUtils.valueOfString(stringBuffer);
    }

    /**
     * 根据实体类class获取数据库表名
     *
     * @param nowClass
     * @return
     */
    private String getTableName(Class nowClass) {
        if (null == nowClass.getAnnotation(BaseTable.class)) {
            throw new RuntimeException("当前实体类没有设置@BaseTable注解==========" + nowClass);
        }
        BaseTable table = (BaseTable) nowClass.getAnnotation(BaseTable.class);
        return table.value();
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
        return getTableName(object.getClass());
    }

    /**
     * 根据实体类class获取数据库表主键
     *
     * @param nowClass
     * @return
     */
    private String getPrimaryKey(Class nowClass) {
        if (null == nowClass.getAnnotation(BaseTable.class)) {
            throw new RuntimeException("当前实体类没有设置@Table注解==========" + nowClass);
        }
        String className = nowClass.getName();
        if (GlobalConfig.isSpringProfilesPro()) {
            className = Md5Utils.encode16(className);
        }
        String primaryKeyFiled = TABLE_PRIMARY_KEY_FIELD_MAP.get(className);
        if (null != primaryKeyFiled && 0 < primaryKeyFiled.length()) {
            return primaryKeyFiled;
        }
        Field[] fields = nowClass.getDeclaredFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(BaseId.class)) {
                String value = field.getAnnotation(BaseId.class).value();
                TABLE_PRIMARY_KEY_FIELD_MAP.put(className, value);
                return value;
            }
        }
        throw new RuntimeException("当前实体类没有设置主键==========" + nowClass);
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
     * 根据实体类对象插入数据
     *
     * @param object
     * @return
     */
    private Map<String, Object> handleTableInsert(Object object) {
        Map<String, Object> result = new HashMap<>(ToolUtils.initialCapacity(3));
        result.put("TABLE_NAME", getTableName(object));

        //存放表的字段
        List<String> columns = new ArrayList<>();
        //存放表的字段值
        List<Object> values = new ArrayList<>();
        //获取实体类所有变量
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            //查看变量是否有注解
            if (null == field.getAnnotation(BaseColumn.class) && null == field.getAnnotation(BaseId.class)) {
                continue;
            }
            //获取属性值
            Object value = getValueByObjectAndField(object, field);
            //如果值为null，则不处理
            if (null == value) {
                continue;
            }
            if (null != field.getAnnotation(BaseId.class)) {
                //添加表的主键
                columns.add(field.getAnnotation(BaseId.class).value());
                result.put("KEY_ID", field.getAnnotation(BaseId.class).value());
            } else {
                //添加表的普通字段
                columns.add(field.getAnnotation(BaseColumn.class).value());
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
    private Map<String, Object> handleTableListInsert(List<?> list) {
        Map<String, Object> result = new HashMap<>(ToolUtils.initialCapacity(3));
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
                if (null == field.getAnnotation(BaseColumn.class) && null == field.getAnnotation(BaseId.class)) {
                    continue;
                }
                //获取属性值
                Object value = getValueByObjectAndField(object, field);
                values.add(value);
                //第一次循环将字段添加到数组里面
                if (i > 0) {
                    continue;
                }
                if (null != field.getAnnotation(BaseId.class)) {
                    //添加表的主键
                    columns.add(field.getAnnotation(BaseId.class).value());
                } else {
                    //添加表的普通字段
                    columns.add(field.getAnnotation(BaseColumn.class).value());
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
        Map<String, Object> result = new HashMap<>(ToolUtils.initialCapacity(3));
        result.put("TABLE_NAME", getTableName(object));

        //存放表的字段和字段值
        List<Map<String, Object>> columnValues = new ArrayList<>();
        //获取实体类所有变量
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            //查看变量是否有注解
            if (null == field.getAnnotation(BaseColumn.class) && null == field.getAnnotation(BaseId.class)) {
                continue;
            }
            //获取属性值
            Object value = getValueByObjectAndField(object, field);
            //获取表的主键
            if (null != field.getAnnotation(BaseId.class)) {
                result.put("KEY_ID", field.getAnnotation(BaseId.class).value());
                result.put("KEY_VALUE", value);
                continue;
            }
            //如果值为null，则不处理
            if (null == value) {
                continue;
            }
            columnValues.add(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
                put("column", field.getAnnotation(BaseColumn.class).value());
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
        //如果需要返回自增主键
        if (!getAutoIncrementPrimaryKey) {
            return mapper.insert(map);
        }
        int num = mapper.insertGetAutoIncrementPrimaryKey(map);
        //获取实体类所有变量
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            //获取表的主键
            if (null == field || null == field.getAnnotation(BaseId.class)) {
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
                } else if (Double.class.getTypeName().equals(typeName)) {
                    field.set(object, ToolUtils.valueOfDouble(keyId));
                } else {
                    field.set(object, keyId);
                }
                field.setAccessible(false);
                break;
            } catch (IllegalAccessException e) {
                log.warn("baseInsert插入数据错误，详情:" + e);
            }
        }
        return num;
    }

    /**
     * 新增多个
     *
     * @param list
     * @return
     */
    protected int baseInsertList(List<?> list) {
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
        return mapper.deleteById(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
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
        return mapper.deleteById(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
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
    protected <T> int baseDeleteByClassAndIds(Class nowClass, T[] ids) {
        if (null == nowClass || null == ids || 0 >= ids.length) {
            return 0;
        }
        return mapper.deleteByIds(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
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
    protected int baseDeleteByClassAndIds(Class nowClass, List<?> ids) {
        if (null == nowClass || null == ids || 0 >= ids.size()) {
            return 0;
        }
        return mapper.deleteByIds(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
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
     * @param clazz
     * @param id
     * @return
     */
    protected <T> T baseFindByClassAndId(Class<T> clazz, Long id) {
        if (null == clazz || null == id) {
            return null;
        }
        Map<String, Object> byId = mapper.findById(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
            put("TABLE_NAME", getTableName(clazz));
            put("id", id);
        }});
        if (null == byId) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(byId, SerializerFeature.WriteDateUseDateFormat), clazz);
    }

    /**
     * 根据id查询
     *
     * @param clazz
     * @param id
     * @return
     */
    protected <T> T baseFindByClassAndId(Class<T> clazz, Integer id) {
        return baseFindByClassAndId(clazz, Long.valueOf(id));
    }

    /**
     * 根据id查询
     *
     * @param clazz
     * @param id
     * @return
     */
    protected <T> T baseFindByClassAndId(Class<T> clazz, String id) {
        if (null == clazz || ToolUtils.isBlank(id)) {
            return null;
        }
        Map<String, Object> byId = mapper.findById(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
            put("TABLE_NAME", getTableName(clazz));
            put("id", id);
        }});
        if (null == byId) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(byId, SerializerFeature.WriteDateUseDateFormat), clazz);
    }

    /**
     * 根据id查询
     *
     * @param clazz
     * @param ids
     * @return
     */
    protected <T> List<T> baseFindByClassAndIds(Class<T> clazz, Long[] ids) {
        if (null == clazz || null == ids || 0 >= ids.length) {
            return null;
        }
        List<Map<String, Object>> byIds = mapper.findByIds(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
            put("TABLE_NAME", getTableName(clazz));
            put("ids", ids);
        }});
        if (null == byIds) {
            return null;
        }
        return JSONArray.parseArray(JSON.toJSONString(byIds, SerializerFeature.WriteDateUseDateFormat), clazz);
    }

    /**
     * 根据id查询
     *
     * @param clazz
     * @param ids
     * @return
     */
    protected <T> List<T> baseFindByClassAndIds(Class<T> clazz, Integer[] ids) {
        if (null == clazz || null == ids || 0 >= ids.length) {
            return null;
        }
        List<Map<String, Object>> byIds = mapper.findByIds(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
            put("TABLE_NAME", getTableName(clazz));
            put("ids", ids);
        }});
        if (null == byIds) {
            return null;
        }
        return JSONArray.parseArray(JSON.toJSONString(byIds, SerializerFeature.WriteDateUseDateFormat), clazz);
    }

    /**
     * 根据id查询
     *
     * @param clazz
     * @param ids
     * @return
     */
    protected <T> List<T> baseFindByClassAndIds(Class<T> clazz, String[] ids) {
        if (null == clazz || null == ids || 0 >= ids.length) {
            return null;
        }
        List<Map<String, Object>> byIds = mapper.findByIds(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
            put("TABLE_NAME", getTableName(clazz));
            put("ids", ids);
        }});
        if (null == byIds) {
            return null;
        }
        return JSONArray.parseArray(JSON.toJSONString(byIds, SerializerFeature.WriteDateUseDateFormat), clazz);
    }

    /**
     * 根据ids查询
     *
     * @param clazz
     * @param ids
     * @return
     */
    protected <T> List<T> baseFindByClassAndIds(Class<T> clazz, List<?> ids) {
        if (null == clazz || null == ids || 0 >= ids.size()) {
            return null;
        }
        List<Map<String, Object>> byIds = mapper.findByIds(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
            put("TABLE_NAME", getTableName(clazz));
            put("ids", ids);
        }});
        if (null == byIds) {
            return null;
        }
        return JSONArray.parseArray(JSON.toJSONString(byIds, SerializerFeature.WriteDateUseDateFormat), clazz);
    }

    /**
     * 判断实体类是否包含某个字段
     *
     * @param clazz
     * @param column
     * @return
     */
    protected boolean classIsContainsColumn(Class clazz, String column) {
        if (null == clazz || null == column) {
            throw new RuntimeException("判断实体类是否包含某个字段出错，column不能为空==========");
        }
        if (null == clazz.getAnnotation(BaseTable.class)) {
            throw new RuntimeException("当前实体类没有设置@Table注解==========" + clazz);
        }
        String key;
        if (GlobalConfig.isSpringProfilesPro()) {
            key = createRedisKey(Md5Utils.encode16(clazz.getName()), column);
        } else {
            key = createRedisKey(clazz.getName(), column);
        }
        Boolean isContains = CLASS_IS_CONTAINS_COLUMN_MAP.get(key);
        if (null != isContains) {
            return isContains;
        }
        //此处对应自动生成的实体类常量，COLUMN_ + 数据库字段全部大写
        String prefix = "COLUMN_";
        try {
            clazz.getDeclaredField(prefix + column.toUpperCase());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("根据实体类、字段、值获取实体类出错---请使用类常量==========" + clazz.getName() + "---" + e);
        }
        CLASS_IS_CONTAINS_COLUMN_MAP.put(key, true);
        return true;
    }

    /**
     * 查询一条数据
     *
     * @param baseWhere
     * @param <T>
     * @return
     */
    protected <T> T baseFindOneData(BaseWhere<T> baseWhere) {
        if (null == baseWhere) {
            throw new RuntimeException("BaseService--baseFindOneData---BaseWhere为空");
        }
        Class<T> modelClass = baseWhere.getModelClass();
        String tableName = getTableName(modelClass);
        if (null == baseWhere.getBaseWhereModels()
                || 0 >= baseWhere.getBaseWhereModels().size()) {
            throw new RuntimeException("BaseService--baseFindOneData---查询条件不能为空");
        }
        List<BaseWhereModel> baseWhereModelList = baseWhere.getBaseWhereModels();
        List<Map<String, Object>> whereList = new ArrayList<>(ToolUtils.initialCapacity(baseWhereModelList.size()));
        for (BaseWhereModel baseWhereModel : baseWhereModelList) {
            //判断实体类是否包含该字段
            if (!classIsContainsColumn(modelClass, baseWhereModel.getColumn())) {
                throw new RuntimeException("当前实体类" + modelClass + "找不到该字段" + baseWhereModel.getColumn() + ";请使用实体类的静态常量");
            }
            whereList.add(new HashMap<String, Object>(ToolUtils.initialCapacity(3)) {{
                put("COLUMN", baseWhereModel.getColumn());
                put("WHERE", baseWhereModel.getWhere().getValue());
                put("VALUE", baseWhereModel.getValue());
            }});
        }
        String orderBy = baseWhere.getOrderBy();
        if (null != orderBy && 0 < orderBy.trim().length()) {
            //如果有排序条件，去掉最后一个 ,
            orderBy = orderBy.substring(0, orderBy.lastIndexOf(","));
        }
        String finalOrderBy = orderBy;
        Map<String, Object> byId = mapper.findOneDataByTableNameAndBaseWhereList(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
            put("TABLE_NAME", tableName);
            put("BASE_WHERE_LIST", whereList);
            put("ORDER_BY", finalOrderBy);
        }});
        if (null == byId) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(byId, SerializerFeature.WriteDateUseDateFormat), modelClass);
    }

    /**
     * 查询列表
     *
     * @param baseWhere
     * @param <T>
     * @return
     */
    protected <T> List<T> baseFindList(BaseWhere<T> baseWhere) {
        if (null == baseWhere) {
            throw new RuntimeException("BaseService--baseFindOneData---BaseWhere为空");
        }
        Class<T> modelClass = baseWhere.getModelClass();
        String tableName = getTableName(modelClass);
        if (null == baseWhere.getBaseWhereModels()
                || 0 >= baseWhere.getBaseWhereModels().size()) {
            throw new RuntimeException("BaseService--baseFindOneData---查询条件不能为空");
        }
        List<BaseWhereModel> baseWhereModelList = baseWhere.getBaseWhereModels();
        List<Map<String, Object>> whereList = new ArrayList<>(ToolUtils.initialCapacity(baseWhereModelList.size()));
        for (BaseWhereModel baseWhereModel : baseWhereModelList) {
            //判断实体类是否包含该字段
            if (!classIsContainsColumn(modelClass, baseWhereModel.getColumn())) {
                throw new RuntimeException("当前实体类" + modelClass + "找不到该字段" + baseWhereModel.getColumn() + ";请使用实体类的静态常量");
            }
            whereList.add(new HashMap<String, Object>(ToolUtils.initialCapacity(3)) {{
                put("COLUMN", baseWhereModel.getColumn());
                put("WHERE", baseWhereModel.getWhere().getValue());
                put("VALUE", baseWhereModel.getValue());
            }});
        }
        String orderBy = baseWhere.getOrderBy();
        if (null != orderBy && 0 < orderBy.trim().length()) {
            //如果有排序条件，去掉最后一个 ,
            orderBy = orderBy.substring(0, orderBy.lastIndexOf(","));
        }
        String finalOrderBy = orderBy;
        List<Map<String, Object>> byIds = mapper.findListByTableNameAndBaseWhereList(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
            put("TABLE_NAME", tableName);
            put("BASE_WHERE_LIST", whereList);
            put("ORDER_BY", finalOrderBy);
        }});
        if (null == byIds) {
            return null;
        }
        return JSONArray.parseArray(JSON.toJSONString(byIds, SerializerFeature.WriteDateUseDateFormat), modelClass);
    }

    /**
     * 根据实体类和字段和值获取一条数据
     *
     * @param clazz
     * @param column
     * @param object
     * @param <T>
     * @return
     */
    protected <T> T baseFindOneDataByClassAndColumnAndValue(Class<T> clazz, String column, Object object) {
        if (null == clazz || null == column || 0 >= column.length() || null == object) {
            return null;
        }
        String tableName = getTableName(clazz);
        //判断实体类是否包含该字段
        if (!classIsContainsColumn(clazz, column)) {
            throw new RuntimeException("当前实体类" + clazz + "找不到该字段" + column + ";请使用实体类的静态常量");
        }
        Map<String, Object> byId = mapper.findOneDataByColumn(new HashMap<String, Object>(ToolUtils.initialCapacity(3)) {{
            put("TABLE_NAME", tableName);
            put("COLUMN", column);
            put("value", object);
        }});
        if (null == byId) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(byId, SerializerFeature.WriteDateUseDateFormat), clazz);
    }

    /**
     * 根据实体类和字段和值获取列表
     *
     * @param clazz
     * @param column
     * @param object
     * @param <T>
     * @return
     */
    protected <T> List<T> baseFindListByClassAndColumnAndValue(Class<T> clazz, String column, Object object) {
        if (null == clazz || ToolUtils.isBlank(column) || null == object) {
            return null;
        }
        String tableName = getTableName(clazz);
        //判断实体类是否包含该字段
        if (!classIsContainsColumn(clazz, column)) {
            throw new RuntimeException("当前实体类" + clazz + "找不到该字段" + column + ";请使用实体类的静态常量");
        }
        List<Map<String, Object>> listByColumn = mapper.findListByColumn(new HashMap<String, Object>(ToolUtils.initialCapacity(3)) {{
            put("TABLE_NAME", tableName);
            put("COLUMN", column);
            put("value", object);
        }});
        if (null == listByColumn) {
            return null;
        }
        return JSONArray.parseArray(JSON.toJSONString(listByColumn, SerializerFeature.WriteDateUseDateFormat), clazz);
    }

    /**
     * 获取所有数据
     *
     * @param clazz
     * @return
     */
    protected <T> List<T> baseFindAllByClass(Class<T> clazz) {
        List<Map<String, Object>> all = mapper.findAll(getTableName(clazz));
        if (null == all) {
            return null;
        }
        return JSONObject.parseArray(JSON.toJSONString(all, SerializerFeature.WriteDateUseDateFormat), clazz);
    }

    /**
     * 获取所有数据按照某个字段升序排列
     *
     * @param clazz
     * @return
     */
    protected <T> List<T> baseFindAllByClassOrderByColumnAsc(Class<T> clazz, String column) {
        String tableName = getTableName(clazz);
        //判断实体类是否包含该字段
        if (!classIsContainsColumn(clazz, column)) {
            return null;
        }
        List<Map<String, Object>> allOrderByColumnAsc = mapper.findAllOrderByColumnAsc(new HashMap<String, String>(ToolUtils.initialCapacity(2)) {{
            put("TABLE_NAME", tableName);
            put("COLUMN", column);
        }});
        if (null == allOrderByColumnAsc) {
            return null;
        }
        return JSONObject.parseArray(JSON.toJSONString(allOrderByColumnAsc, SerializerFeature.WriteDateUseDateFormat), clazz);
    }

}