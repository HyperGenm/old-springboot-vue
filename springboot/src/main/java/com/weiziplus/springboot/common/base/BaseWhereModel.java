package com.weiziplus.springboot.common.base;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * sql字段与条件和值
 *
 * @author wanglongwei
 * @date 2020/06/03 14/57
 */
@Getter
@Accessors(chain = true)
public class BaseWhereModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库字段
     */
    private String column;

    /**
     * where条件,默认等于=
     */
    private BaseWhereEnum where = BaseWhereEnum.EQUAL;

    /**
     * 值
     */
    private Object value;

    private BaseWhereModel() {

    }

    /**
     * 创建where
     *
     * @param column
     * @param where  默认为 等于=
     * @param value
     */
    public BaseWhereModel(String column, BaseWhereEnum where, Object value) {
        this.column = column;
        this.where = where;
        this.value = value;
    }

    /**
     * 创建where,默认为等于
     *
     * @param column
     * @param value
     */
    public BaseWhereModel(String column, Object value) {
        this.column = column;
        this.value = value;
    }

}