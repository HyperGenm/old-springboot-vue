package com.weiziplus.springboot.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.List;

/**
 * 系统角色表
 * sys_role
 *
 * @author WeiziPlus
 * @date 2019-07-23 09:46:32
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("sys_role")
@Accessors(chain = true)
@Alias("SysRole")
public class SysRole implements Serializable {
    /**
     * 系统角色表主键，自增
     */
    @Id("id")
    private Long id;

    /**
     * 上级角色id
     */
    @Column("parent_id")
    private Long parentId;

    /**
     * 角色名称
     */
    @Column("name")
    private String name;

    /**
     * 是否启用;0:启用,1:禁用
     */
    @Column("is_stop")
    private Integer isStop;

    /**
     * 排序，数字越小越靠前
     */
    @Column("sort")
    private Integer sort;

    /**
     * 角色描述
     */
    @Column("description")
    private String description;

    /**
     * 角色创建时间
     */
    @Column("create_time")
    private String createTime;

    private List<SysRole> children;

    private static final long serialVersionUID = 1L;
}