package com.weiziplus.springboot.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * 系统用户表
 * sys_user
 *
 * @author 16028
 * @date 2020-02-20 13:49:23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@BaseTable("sys_user")
@Accessors(chain = true)
@Alias("SysUser")
@ApiModel("系统用户表")
public class SysUser implements Serializable {
    /**
     * 系统用户表主键id，自增
     */
    @ApiModelProperty("系统用户表主键id，自增")
    @BaseId("id")
    private Long id;

    /**
     * 登录用户名
     */
    @ApiModelProperty("登录用户名")
    @BaseColumn("username")
    private String username;

    /**
     * 登录密码
     */
    @ApiModelProperty("登录密码")
    @BaseColumn("password")
    @JsonIgnore
    private String password;

    /**
     * 用户真实姓名
     */
    @ApiModelProperty("用户真实姓名")
    @BaseColumn("real_name")
    private String realName;

    /**
     * 系统角色表id
     */
    @ApiModelProperty("系统角色表id")
    @BaseColumn("role_id")
    private Integer roleId;

    /**
     * 是否允许登录;0:允许，1:禁止
     */
    @ApiModelProperty("是否允许登录;0:允许，1:禁止")
    @BaseColumn("allow_login")
    private Integer allowLogin;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    @BaseColumn("phone")
    private String phone;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    @BaseColumn("icon")
    private String icon;

    /**
     * 用户最后活跃ip地址
     */
    @ApiModelProperty("用户最后活跃ip地址")
    @BaseColumn("last_ip_address")
    private String lastIpAddress;

    /**
     * 用户最后活跃时间
     */
    @ApiModelProperty("用户最后活跃时间")
    @BaseColumn("last_active_time")
    private String lastActiveTime;

    /**
     * 用户创建时间
     */
    @ApiModelProperty("用户创建时间")
    @BaseColumn("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_USERNAME = "username";

    public static final String COLUMN_PASSWORD = "password";

    public static final String COLUMN_REAL_NAME = "real_name";

    public static final String COLUMN_ROLE_ID = "role_id";

    public static final String COLUMN_ALLOW_LOGIN = "allow_login";

    public static final String COLUMN_PHONE = "phone";

    public static final String COLUMN_ICON = "icon";

    public static final String COLUMN_LAST_IP_ADDRESS = "last_ip_address";

    public static final String COLUMN_LAST_ACTIVE_TIME = "last_active_time";

    public static final String COLUMN_CREATE_TIME = "create_time";
}