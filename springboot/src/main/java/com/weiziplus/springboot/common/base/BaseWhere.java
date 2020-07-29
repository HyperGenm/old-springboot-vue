package com.weiziplus.springboot.common.base;

import com.weiziplus.springboot.common.util.ToolUtils;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件工具
 *
 * @author wanglongwei
 * @date 2020/07/28 16/35
 */
@Getter
@Accessors(chain = true)
public class BaseWhere<T> extends BaseService implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 实体类的class
     */
    private Class<T> modelClass;

    /**
     * 查询的条件
     */
    private List<BaseWhereModel> baseWhereModels;

    /**
     * 排序
     */
    private String orderBy;

    /**
     * 实例化
     */
    private BaseWhere() {

    }

    /**
     * 实例化
     *
     * @param clazz
     */
    public BaseWhere(Class<T> clazz) {
        this.modelClass = clazz;
    }

    /**
     * 添加查询条件
     *
     * @param baseWhereModel
     * @return
     */
    public BaseWhere<T> where(BaseWhereModel baseWhereModel) {
        List<BaseWhereModel> oldList = this.getBaseWhereModels();
        if (null == oldList) {
            oldList = new ArrayList<>(ToolUtils.initialCapacity(1));
        }
        oldList.add(baseWhereModel);
        this.baseWhereModels = oldList;
        return this;
    }

    /**
     * 添加查询条件数组
     *
     * @param baseWhereModelList
     * @return
     */
    public BaseWhere<T> where(List<BaseWhereModel> baseWhereModelList) {
        List<BaseWhereModel> oldList = this.getBaseWhereModels();
        if (null == oldList) {
            oldList = new ArrayList<>(ToolUtils.initialCapacity(1));
        }
        oldList.addAll(baseWhereModelList);
        this.baseWhereModels = oldList;
        return this;
    }

    /**
     * 倒叙
     *
     * @param columns
     * @return
     */
    public BaseWhere<T> descOrderBy(String... columns) {
        if (null == columns || 0 >= columns.length) {
            return this;
        }
        String oldOrderBy = this.getOrderBy();
        if (null == oldOrderBy) {
            oldOrderBy = "";
        }
        StringBuilder orderBy = new StringBuilder(oldOrderBy);
        for (String column : columns) {
            //判断实体类是否包含该字段
            if (!classIsContainsColumn(this.modelClass, column)) {
                throw new RuntimeException("当前实体类" + this.modelClass + "找不到该字段" + column + ";请使用实体类的静态常量");
            }
            orderBy.append(column).append(" DESC, ");
        }
        this.orderBy = orderBy.toString();
        return this;
    }

    /**
     * 正序
     *
     * @param columns
     * @return
     */
    public BaseWhere<T> ascOrderBy(String... columns) {
        if (null == columns || 0 >= columns.length) {
            return this;
        }
        String oldOrderBy = this.getOrderBy();
        if (null == oldOrderBy) {
            oldOrderBy = "";
        }
        StringBuilder orderBy = new StringBuilder(oldOrderBy);
        for (String column : columns) {
            //判断实体类是否包含该字段
            if (!classIsContainsColumn(this.modelClass, column)) {
                throw new RuntimeException("当前实体类" + this.modelClass + "找不到该字段" + column + ";请使用实体类的静态常量");
            }
            orderBy.append(column).append(" ASC, ");
        }
        this.orderBy = orderBy.toString();
        return this;
    }

}
