package com.weiziplus.springboot.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.common.base.Column;
import com.weiziplus.springboot.common.base.Id;
import com.weiziplus.springboot.common.base.Table;
import lombok.Data;

import java.util.List;

/**
 * @author wanglongwei
 * @data 2019/5/9 11:55
 * @JsonInclude(JsonInclude.Include.NON_NULL) 将null的数据过滤掉
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("sys_function")
public class SysFunction {
    @Id("id")
    private Long id;

    @Column("parent_id")
    private Long parentId;

    @Column("name")
    private String name;

    @Column("path")
    private String path;

    @Column("title")
    private String title;

    @Column("type")
    private Integer type;

    @Column("icon")
    private String icon;

    @Column("sort")
    private Long sort;

    @Column("description")
    private String description;

    @Column("create_time")
    private String createTime;

    private List<SysFunction> children;
}
