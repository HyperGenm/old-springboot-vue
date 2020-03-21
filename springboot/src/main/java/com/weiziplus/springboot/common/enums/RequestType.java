package com.weiziplus.springboot.common.enums;

import lombok.Getter;

/**
 * 请求类型
 *
 * @author wanglongwei
 * @date 2020/03/12 13/56
 */
@Getter
public enum RequestType {

    /**
     * 请求方式
     */
    SELECT("查询", 1),
    INSERT("新增", 2),
    UPDATE("修改", 3),
    DELETE("删除", 4);

    private String name;

    private Integer value;

    RequestType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 根据value获取name
     *
     * @param value
     * @return
     */
    public static String getNameByValue(Integer value) {
        for (RequestType type : RequestType.values()) {
            if (type.getValue().equals(value)) {
                return type.getName();
            }
        }
        return null;
    }

    /**
     * 根据name获取value
     *
     * @param name
     * @return
     */
    public static Integer getValueByName(String name) {
        for (RequestType type : RequestType.values()) {
            if (type.getName().equals(name)) {
                return type.getValue();
            }
        }
        return null;
    }

    /**
     * 判断是否存在某个值
     *
     * @param value
     * @return
     */
    public static boolean contains(Integer value) {
        for (RequestType type : RequestType.values()) {
            if (type.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

}
