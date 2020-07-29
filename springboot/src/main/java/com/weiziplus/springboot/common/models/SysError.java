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
 * 系统异常
 * sys_error
 * @author 16028
 * @date 2020-03-21 14:48:26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@BaseTable("sys_error")
@Accessors(chain = true)
@Alias("SysError")
@ApiModel("系统异常")
public class SysError implements Serializable {
    /**
     */
    @BaseId("id")
    private Long id;

    /**
     * 类名
     */
    @ApiModelProperty("类名")
    @BaseColumn("class_name")
    private String className;

    /**
     * 方法名
     */
    @ApiModelProperty("方法名")
    @BaseColumn("method_name")
    private String methodName;

    /**
     * 第几行
     */
    @ApiModelProperty("第几行")
    @BaseColumn("line_number")
    private Integer lineNumber;

    /**
     * 详情
     */
    @ApiModelProperty("详情")
    @BaseColumn("content")
    private String content;

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

    public static final String COLUMN_CLASS_NAME = "class_name";

    public static final String COLUMN_METHOD_NAME = "method_name";

    public static final String COLUMN_LINE_NUMBER = "line_number";

    public static final String COLUMN_CONTENT = "content";

    public static final String COLUMN_REMARK = "remark";

    public static final String COLUMN_CREATE_TIME = "create_time";
}