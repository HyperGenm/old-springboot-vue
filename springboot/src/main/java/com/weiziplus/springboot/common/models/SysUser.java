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
 * 系统用户表
 * sys_user
 * @author 16028
 * @date 2020-02-20 11:54:02
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("sys_user")
@Accessors(chain = true)
@Alias("SysUser")
@ApiModel("系统用户表")
public class SysUser implements Serializable {
    /**
     * 系统用户表主键id，自增
     */
    @ApiModelProperty("系统用户表主键id，自增")
    @Id("id")
    public static final String COLUMN_ID = "id" ;
    private Long id;

    /**
     * 登录用户名
     */
    @ApiModelProperty("登录用户名")
    @Column("username")
    public static final String COLUMN_USERNAME = "username" ;
    private String username;

    /**
     * 登录密码
     */
    @ApiModelProperty("登录密码")
    @Column("password")
    @JsonIgnore
    public static final String COLUMN_PASSWORD = "password" ;
    private String password;

    /**
     * 用户真实姓名
     */
    @ApiModelProperty("用户真实姓名")
    @Column("real_name")
    public static final String COLUMN_REAL_NAME = "real_name" ;
    private String realName;

    /**
     * 系统角色表id
     */
    @ApiModelProperty("系统角色表id")
    @Column("role_id")
    public static final String COLUMN_ROLE_ID = "role_id" ;
    private Integer roleId;

    /**
     * 是否允许登录;0:允许，1:禁止，2:封号中
     */
    @ApiModelProperty("是否允许登录;0:允许，1:禁止，2:封号中")
    @Column("allow_login")
    public static final String COLUMN_ALLOW_LOGIN = "allow_login" ;
    private Integer allowLogin;

    /**
     * 0:允许
     */
    public final static Integer ALLOW_LOGIN_ALLOW = 0;

    /**
     * 1:禁止
     */
    public final static Integer ALLOW_LOGIN_FORBID = 1;

    /**
     * 2:封号
     */
    public final static Integer ALLOW_LOGIN_DISABLE = 2;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    @Column("icon")
    public static final String COLUMN_ICON = "icon" ;
    private String icon;

    /**
     * 账户封号次数
     */
    @ApiModelProperty("账户封号次数")
    @Column("suspend_num")
    public static final String COLUMN_SUSPEND_NUM = "suspend_num" ;
    private Integer suspendNum;

    /**
     * 用户最后活跃ip地址
     */
    @ApiModelProperty("用户最后活跃ip地址")
    @Column("last_ip_address")
    public static final String COLUMN_LAST_IP_ADDRESS = "last_ip_address" ;
    private String lastIpAddress;

    /**
     * 用户最后活跃时间
     */
    @ApiModelProperty("用户最后活跃时间")
    @Column("last_active_time")
    public static final String COLUMN_LAST_ACTIVE_TIME = "last_active_time" ;
    private String lastActiveTime;

    /**
     * 用户创建时间
     */
    @ApiModelProperty("用户创建时间")
    @Column("create_time")
    public static final String COLUMN_CREATE_TIME = "create_time" ;
    private String createTime;

    private static final long serialVersionUID = 1L;
}