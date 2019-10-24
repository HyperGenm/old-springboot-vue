package com.weiziplus.springboot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统用户表
 * sys_user
 *
 * @author WeiziPlus
 * @date 2019-07-23 09:46:32
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("sys_user")
@Accessors(chain = true)
public class SysUser implements Serializable {
    /**
     * 系统用户表主键id，自增
     */
    @Id("id")
    private Long id;

    /**
     * 登录用户名
     */
    @Column("username")
    private String username;

    /**
     * 登录密码
     */
    @Column("password")
    @JsonIgnore
    private String password;

    /**
     * 用户真实姓名
     */
    @Column("real_name")
    private String realName;

    /**
     * 系统角色表id
     */
    @Column("role_id")
    private Long roleId;

    /**
     * 是否允许登录;0:允许，1:禁止，2:封号
     */
    @Column("allow_login")
    private Integer allowLogin;

    /**
     * 用户头像
     */
    @Column("icon")
    private String icon;

    /**
     * 账户封号次数
     */
    @Column("suspend_num")
    private Integer suspendNum;

    /**
     * 用户最后活跃ip地址
     */
    @Column("last_ip_address")
    private String lastIpAddress;

    /**
     * 用户最后活跃时间
     */
    @Column("last_active_time")
    private String lastActiveTime;

    /**
     * 用户创建时间
     */
    @Column("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;
}