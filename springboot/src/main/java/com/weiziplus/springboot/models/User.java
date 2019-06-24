package com.weiziplus.springboot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @data 2019/5/10 17:00
 */
@Data
@Table("w_user")
public class User implements Serializable {
    @Id("id")
    private Long id;

    @Column("username")
    private String username;

    @Column("password")
    @JsonIgnore
    private String password;

    @Column("create_time")
    private String createTime;
}
