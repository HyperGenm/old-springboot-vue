package com.weiziplus.springboot.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 系统功能表
 * sys_function
 *
 * @author WeiziPlus
 * @date 2019-07-23 09:46:32
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("sys_function")
public class SysFunction implements Serializable {
    /**
     * 系统功能表主键，自增
     */
    @Id("id")
    private Long id;

    /**
     * 上级id
     */
    @Column("parent_id")
    private Long parentId;

    /**
     * 功能唯一标识
     */
    @Column("name")
    private String name;

    /**
     * 功能路径
     */
    @Column("path")
    private String path;

    /**
     * 功能标题
     */
    @Column("title")
    private String title;

    /**
     * 当前功能对应的api列表
     */
    @Column("contain_api")
    private String containApi;

    /**
     * 功能类型;0:菜单,1:按钮
     */
    @Column("type")
    private Integer type;

    /**
     * 功能图标
     */
    @Column("icon")
    private String icon;

    /**
     * 功能排序，数字越小越靠前
     */
    @Column("sort")
    private Integer sort;

    /**
     * 功能描述
     */
    @Column("description")
    private String description;

    /**
     * 功能创建时间
     */
    @Column("create_time")
    private String createTime;

    private List<SysFunction> children;

    private static final long serialVersionUID = 1L;
}