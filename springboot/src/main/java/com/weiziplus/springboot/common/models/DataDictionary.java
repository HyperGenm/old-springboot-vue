package com.weiziplus.springboot.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.common.base.BaseColumn;
import com.weiziplus.springboot.common.base.BaseId;
import com.weiziplus.springboot.common.base.BaseTable;
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
 * @date 2020-02-20 13:41:26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@BaseTable("data_dictionary")
@Accessors(chain = true)
@Alias("DataDictionary")
@ApiModel("数据字典表")
public class DataDictionary implements Serializable {
    /**
     * 自增
     */
    @ApiModelProperty("自增")
    @BaseId("id")
    private Integer id;

    /**
     * 字典标识
     */
    @ApiModelProperty("字典标识")
    @BaseColumn("code")
    private String code;

    /**
     * 字典名字
     */
    @ApiModelProperty("字典名字")
    @BaseColumn("name")
    private String name;

    /**
     * 字典备注
     */
    @ApiModelProperty("字典备注")
    @BaseColumn("remark")
    private String remark;

    /**
     * 字典创建时间
     */
    @ApiModelProperty("字典创建时间")
    @BaseColumn("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_CODE = "code";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_REMARK = "remark";

    public static final String COLUMN_CREATE_TIME = "create_time";
}