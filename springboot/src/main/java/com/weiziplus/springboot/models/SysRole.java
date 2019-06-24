package com.weiziplus.springboot.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wanglongwei
 * @data 2019/5/9 11:57
 * @JsonInclude(JsonInclude.Include.NON_NULL) 将null的数据过滤掉
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("sys_role")
public class SysRole implements Serializable {
    @Id("id")
    private Long id;

    @Column("parent_id")
    private Long parentId;

    @Column("name")
    private String name;

    @Column("is_stop")
    private Integer isStop;

    @Column("sort")
    private Long sort;

    @Column("description")
    private String description;

    @Column("create_time")
    private String createTime;
    private List<SysRole> children;
}
