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
 * 数据字典表
 * data_dictionary
 * @author 16028
 * @date 2019-10-14 21:18:58
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("data_dictionary")
@ApiModel("数据字典表")
@Alias("DataDictionary")
@Accessors(chain = true)
public class DataDictionary implements Serializable {
    /**
     * 自增
     */
    @ApiModelProperty("自增")
    @Id("id")
    private Integer id;

    /**
     * 字典标识
     */
    @ApiModelProperty("字典标识")
    @Column("code")
    private String code;

    /**
     * 字典名字
     */
    @ApiModelProperty("字典名字")
    @Column("name")
    private String name;

    /**
     * 字典备注
     */
    @ApiModelProperty("字典备注")
    @Column("remark")
    private String remark;

    /**
     * 字典创建时间
     */
    @ApiModelProperty("字典创建时间")
    @Column("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;
}