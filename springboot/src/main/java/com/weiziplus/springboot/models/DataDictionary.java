package com.weiziplus.springboot.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据字典表
 * data_dictionary
 * @author WeiziPlus
 * @date 2019-08-05 09:51:51
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("data_dictionary")
public class DataDictionary implements Serializable {
    /**
     * 自增
     */
    @Id("id")
    private Long id;

    /**
     * 字典标识
     */
    @Column("code")
    private String code;

    /**
     * 字典名字
     */
    @Column("name")
    private String name;

    /**
     * 字典备注
     */
    @Column("remark")
    private String remark;

    /**
     * 字典创建时间
     */
    @Column("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;
}