package com.weiziplus.springboot.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.common.base.BaseColumn;
import com.weiziplus.springboot.common.base.BaseId;
import com.weiziplus.springboot.common.base.BaseTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * 系统权限表
 * sys_role_function
 * @author 16028
 * @date 2020-02-20 13:48:17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@BaseTable("sys_role_function")
@Accessors(chain = true)
@Alias("SysRoleFunction")
@ApiModel("系统权限表")
public class SysRoleFunction implements Serializable {
    /**
     * 角色功能表主键，自增
     */
    @ApiModelProperty("角色功能表主键，自增")
    @BaseId("id")
    private Long id;

    /**
     * 角色表id
     */
    @ApiModelProperty("角色表id")
    @BaseColumn("role_id")
    private Integer roleId;

    /**
     * 功能表id
     */
    @ApiModelProperty("功能表id")
    @BaseColumn("function_id")
    private Integer functionId;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_ROLE_ID = "role_id";

    public static final String COLUMN_FUNCTION_ID = "function_id";
}