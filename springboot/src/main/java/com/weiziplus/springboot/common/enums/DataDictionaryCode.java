package com.weiziplus.springboot.common.enums;

import lombok.Getter;

/**
 * 字典表code枚举
 *
 * @author wanglongwei
 * @date 2020/03/12 14/03
 */
@Getter
public enum DataDictionaryCode {

    /**
     * 字典表code枚举
     */
    IP_ROLE("ip规则", "ipRole"),
    IP_LIST_WHITE("ip白名单", "ipListWhite"),
    IP_LIST_BLACK("ip黑名单", "ipListBlack"),
    IP_LIST_ABNORMAL("异常ip", "ipListAbnormal");

    private String name;

    private String code;

    DataDictionaryCode(String name, String code) {
        this.name = name;
        this.code = code;
    }

    /**
     * 根据code获取name
     *
     * @param code
     * @return
     */
    public static String getNameByCode(String code) {
        for (DataDictionaryCode value : DataDictionaryCode.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    /**
     * 根据name获取code
     *
     * @param name
     * @return
     */
    public static String getCodeByName(String name) {
        for (DataDictionaryCode value : DataDictionaryCode.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }

    /**
     * 判断是否存在某个值
     *
     * @param code
     * @return
     */
    public static boolean contains(String code) {
        for (DataDictionaryCode dictionaryCode : DataDictionaryCode.values()) {
            if (dictionaryCode.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
