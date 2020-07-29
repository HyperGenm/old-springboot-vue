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
 * 用户日志
 * user_log
 * @author 16028
 * @date 2020-02-26 20:23:38
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@BaseTable("t_user_log")
@Accessors(chain = true)
@Alias("TUserLog")
@ApiModel("用户日志")
public class UserLog implements Serializable {
    /**
     */
    @BaseId("id")
    private Long id;

    /**
     * 用户表主键
     */
    @ApiModelProperty("用户表主键")
    @BaseColumn("user_id")
    private Long userId;

    /**
     * 请求的url
     */
    @ApiModelProperty("请求的url")
    @BaseColumn("url")
    private String url;

    /**
     * 当前请求的参数
     */
    @ApiModelProperty("当前请求的参数")
    @BaseColumn("param")
    private String param;

    /**
     * 请求的类型,1:查询,2:新增,3:修改,4:删除
     */
    @ApiModelProperty("请求的类型,1:查询,2:新增,3:修改,4:删除")
    @BaseColumn("type")
    private Integer type;

    /**
     * 操作描述
     */
    @ApiModelProperty("操作描述")
    @BaseColumn("description")
    private String description;

    /**
     * ip地址
     */
    @ApiModelProperty("ip地址")
    @BaseColumn("ip_address")
    private String ipAddress;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @BaseColumn("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_USER_ID = "user_id";

    public static final String COLUMN_URL = "url";

    public static final String COLUMN_PARAM = "param";

    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_DESCRIPTION = "description";

    public static final String COLUMN_IP_ADDRESS = "ip_address";

    public static final String COLUMN_CREATE_TIME = "create_time";
}