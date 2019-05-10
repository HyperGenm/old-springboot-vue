package com.weiziplus.springboot.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author wanglongwei
 * @data 2019/5/9 11:26
 */
@Data
public class SysUser {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String realName;
    private Integer allowLogin;
    private String lastActiveTime;
    private String createTime;
}
