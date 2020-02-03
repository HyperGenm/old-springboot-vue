package com.weiziplus.springboot.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.common.base.Column;
import com.weiziplus.springboot.common.base.Id;
import com.weiziplus.springboot.common.base.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * 数据字典值
 * data_dictionary_value
 *
 * @author 16028
 * @date 2019-10-14 21:18:58
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("data_dictionary_value")
@ApiModel("数据字典值")
@Alias("DataDictionaryValue")
@Accessors(chain = true)
public class DataDictionaryValue implements Serializable {
    /**
     * 自增
     */
    @ApiModelProperty("自增")
    @Id("id")
    private Long id;

    /**
     * 字典编号
     */
    @ApiModelProperty("字典编号")
    @Column("dictionary_code")
    private String dictionaryCode;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    @Column("name")
    private String name;

    /**
     * 值
     */
    @ApiModelProperty("值")
    @Column("value")
    private String value;

    /**
     * 类型(自定义)：
     * ipFilter：ip名单---0：白名单，1：黑名单
     */
    @ApiModelProperty("类型(自定义)： ipFilter：ip名单---0：白名单，1：黑名单")
    @Column("type")
    private Integer type;

    /**
     * ipFilter白名单
     */
    public static Integer TYPE_IP_FILTER_WHITE = 0;

    /**
     * ipFilter黑名单
     */
    public static Integer TYPE_IP_FILTER_BLACK = 1;

    /**
     * 排序(自定义,默认为排序)
     * abnormalIp:异常ip---异常出错次数
     */
    @ApiModelProperty("排序(自定义,默认为排序) abnormalIp:异常ip---异常出错次数")
    @Column("order")
    private Integer order;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @Column("remark")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;
}