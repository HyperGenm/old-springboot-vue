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
 * 数据字典值
 * data_dictionary_value
 *
 * @author 16028
 * @date 2020-02-20 13:44:25
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@BaseTable("data_dictionary_value")
@Accessors(chain = true)
@Alias("DataDictionaryValue")
@ApiModel("数据字典值")
public class DataDictionaryValue implements Serializable {
    /**
     * 自增
     */
    @ApiModelProperty("自增")
    @BaseId("id")
    private Integer id;

    /**
     * 字典编号
     */
    @ApiModelProperty("字典编号")
    @BaseColumn("dictionary_code")
    private String dictionaryCode;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    @BaseColumn("name")
    private String name;

    /**
     * 值
     */
    @ApiModelProperty("值")
    @BaseColumn("value")
    private String value;

    /**
     * 含义自定
     * 1:ipListAbnormal---异常次数
     */
    @ApiModelProperty("含义自定   1:ipListAbnormal---异常次数")
    @BaseColumn("num")
    private Integer num;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @BaseColumn("remark")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @BaseColumn("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_DICTIONARY_CODE = "dictionary_code";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_VALUE = "value";

    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_ORDER = "order";

    public static final String COLUMN_REMARK = "remark";

    public static final String COLUMN_CREATE_TIME = "create_time";
}