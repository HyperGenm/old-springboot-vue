package com.weiziplus.springboot.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author wanglongwei
 * @data 2019/5/9 11:55
 * @JsonInclude(JsonInclude.Include.NON_NULL) 将null的数据过滤掉
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SysFunction {
    private Long id;
    private Long parentId;
    private String name;
    private String title;
    private Integer type;
    private String icon;
    private Long sort;
    private String description;
    private String createTime;
    private List<SysFunction> children;
}
