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
 * 系统功能表
 * sys_function
 *
 * @author 16028
 * @date 2020-02-20 13:45:12
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("sys_function")
@Accessors(chain = true)
@Alias("SysFunction")
@ApiModel("系统功能表")
public class SysFunction implements Serializable {
    /**
     * 系统功能表主键，自增
     */
    @ApiModelProperty("系统功能表主键，自增")
    @Id("id")
    private Integer id;

    /**
     * 上级id
     */
    @ApiModelProperty("上级id")
    @Column("parent_id")
    private Integer parentId;

    /**
     * 功能唯一标识
     */
    @ApiModelProperty("功能唯一标识")
    @Column("name")
    private String name;

    /**
     * 功能路径
     */
    @ApiModelProperty("功能路径")
    @Column("path")
    private String path;

    /**
     * 功能标题
     */
    @ApiModelProperty("功能标题")
    @Column("title")
    private String title;

    /**
     * 当前功能对应的api列表，多个用,隔开
     */
    @ApiModelProperty("当前功能对应的api列表，多个用,隔开")
    @Column("contain_api")
    private String containApi;

    /**
     * 功能类型;0:菜单,1:按钮
     */
    @ApiModelProperty("功能类型;0:菜单,1:按钮")
    @Column("type")
    private Integer type;

    /**
     * 功能图标
     */
    @ApiModelProperty("功能图标")
    @Column("icon")
    private String icon;

    /**
     * 功能排序，数字越小越靠前
     */
    @ApiModelProperty("功能排序，数字越小越靠前")
    @Column("sort")
    private Integer sort;

    /**
     * 功能描述
     */
    @ApiModelProperty("功能描述")
    @Column("description")
    private String description;

    /**
     * 功能创建时间
     */
    @ApiModelProperty("功能创建时间")
    @Column("create_time")
    private String createTime;

    private List<SysFunction> children;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_PARENT_ID = "parent_id";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_PATH = "path";

    public static final String COLUMN_TITLE = "title";

    public static final String COLUMN_CONTAIN_API = "contain_api";

    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_ICON = "icon";

    public static final String COLUMN_SORT = "sort";

    public static final String COLUMN_DESCRIPTION = "description";

    public static final String COLUMN_CREATE_TIME = "create_time";
}