package com.weiziplus.springboot.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weiziplus.springboot.common.base.Column;
import com.weiziplus.springboot.common.base.Id;
import com.weiziplus.springboot.common.base.Table;
import lombok.Data;

/**
 * @author wanglongwei
 * @data 2019/5/10 17:00
 */
@Data
@Table("w_user")
public class User {
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
