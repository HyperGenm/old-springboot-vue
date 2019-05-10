package com.weiziplus.springboot.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author wanglongwei
 * @data 2019/5/10 17:00
 */
@Data
public class User {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String createTime;
}
