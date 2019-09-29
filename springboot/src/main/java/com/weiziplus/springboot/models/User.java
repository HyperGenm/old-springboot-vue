package com.weiziplus.springboot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户表
 * user
 * @author Administrator
 * @date 2019-09-29 09:16:40
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("user")
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
    @JsonIgnore
    @ApiModelProperty("密码")
    @Column("password")
    private String password;

    /**
     * 用户创建时间
     */
    @ApiModelProperty("用户创建时间")
    @Column("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;
}