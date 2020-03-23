package com.weiziplus.springboot.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * 用户表
 * user
 * @author 16028
 * @date 2020-02-20 13:33:27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("t_user")
@Accessors(chain = true)
@Alias("TUser")
@ApiModel("用户表")
public class User implements Serializable {
    /**
     * 用户表主键，自增
     */
    @ApiModelProperty("用户表主键，自增")
    @Id("id")
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    @Column("username")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @Column("password")
    @JsonIgnore
    private String password;

    /**
     * 用户最后活跃ip地址
     */
    @ApiModelProperty("用户最后活跃ip地址")
    @Column("last_ip_address")
    private String lastIpAddress;

    /**
     * 用户最后活跃时间
     */
    @ApiModelProperty("用户最后活跃时间")
    @Column("last_active_time")
    private String lastActiveTime;

    /**
     * 用户创建时间
     */
    @ApiModelProperty("用户创建时间")
    @Column("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_USERNAME = "username";

    public static final String COLUMN_PASSWORD = "password";

    public static final String COLUMN_LAST_IP_ADDRESS = "last_ip_address";

    public static final String COLUMN_LAST_ACTIVE_TIME = "last_active_time";

    public static final String COLUMN_CREATE_TIME = "create_time";
}