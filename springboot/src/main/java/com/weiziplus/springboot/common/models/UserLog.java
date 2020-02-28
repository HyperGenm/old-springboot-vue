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
 * 用户日志
 * user_log
 * @author 16028
 * @date 2020-02-26 20:23:38
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("user_log")
@Accessors(chain = true)
@Alias("UserLog")
@ApiModel("用户日志")
public class UserLog implements Serializable {
    /**
     */
    @Id("id")
    private Long id;

    /**
     * 用户表主键
     */
    @ApiModelProperty("用户表主键")
    @Column("user_id")
    private Long userId;

    /**
     * 操作描述
     */
    @ApiModelProperty("操作描述")
    @Column("description")
    private String description;

    /**
     * 当前请求的参数
     */
    @ApiModelProperty("当前请求的参数")
    @Column("param")
    private String param;

    /**
     * 请求的类型,1:查询,2:新增,3:修改,4:删除
     */
    @ApiModelProperty("请求的类型,1:查询,2:新增,3:修改,4:删除")
    @Column("type")
    private Integer type;

    /**
     * ip地址
     */
    @ApiModelProperty("ip地址")
    @Column("ip_address")
    private String ipAddress;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_USER_ID = "user_id";

    public static final String COLUMN_DESCRIPTION = "description";

    public static final String COLUMN_PARAM = "param";

    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_IP_ADDRESS = "ip_address";

    public static final String COLUMN_CREATE_TIME = "create_time";
}