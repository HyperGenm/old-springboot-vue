package com.weiziplus.springboot.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户表
 * user
 * @author WeiziPlus
 * @date 2019-07-23 14:17:45
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("user")
public class User implements Serializable {
    /**
     * 用户表主键，自增
     */
    @Id("id")
    private Long id;

    /**
     * 用户名
     */
    @Column("username")
    private String username;

    /**
     * 密码
     */
    @Column("password")
    private String password;

    /**
     * 用户创建时间
     */
    @Column("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;
}