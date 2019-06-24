package com.weiziplus.springboot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @data 2019/5/9 11:26
 */
@Data
@Table("sys_user")
public class SysUser implements Serializable {
    @Id("id")
    private Long id;

    @Column("username")
    private String username;

    @Column("password")
    @JsonIgnore
    private String password;

    @Column("real_name")
    private String realName;

    @Column("allow_login")
    private Integer allowLogin;

    @Column("last_active_time")
    private String lastActiveTime;

    @Column("create_time")
    private String createTime;
}
