package com.weiziplus.springboot.common.models;

import lombok.Data;

/**
 * @author wanglongwei
 * @data 2019/5/13 15:04
 */
@Data
public class SysLog {
    private Long id;
    private Long userId;
    private String description;
    private String createTime;
}
