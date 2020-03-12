package com.weiziplus.springboot.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.common.base.Column;
import com.weiziplus.springboot.common.base.Id;
import com.weiziplus.springboot.common.base.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.List;

/**
 * 系统角色表
 * sys_role
 *
 * @author 16028
 * @date 2020-02-20 13:47:35
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("sys_role")
@Accessors(chain = true)
@Alias("SysRole")
@ApiModel("系统角色表")
public class SysRole implements Serializable {
    /**
     * 系统角色表主键，自增
     */
    @ApiModelProperty("系统角色表主键，自增")
    @Id("id")
    private Integer id;

    /**
     * 上级角色id
     */
    @ApiModelProperty("上级角色id")
    @Column("parent_id")
    private Integer parentId;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    @Column("name")
    private String name;

    /**
     * 是否启用;0:启用,1:禁用
     */
    @ApiModelProperty("是否启用;0:启用,1:禁用")
    @Column("is_stop")
    private Integer isStop;

    /**
     * 排序，数字越小越靠前
     */
    @ApiModelProperty("排序，数字越小越靠前")
    @Column("sort")
    private Integer sort;

    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    @Column("description")
    private String description;

    /**
     * 角色创建时间
     */
    @ApiModelProperty("角色创建时间")
    @Column("create_time")
    private String createTime;

    private List<SysRole> children;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_PARENT_ID = "parent_id";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_IS_STOP = "is_stop";

    public static final String COLUMN_SORT = "sort";

    public static final String COLUMN_DESCRIPTION = "description";

    public static final String COLUMN_CREATE_TIME = "create_time";
}