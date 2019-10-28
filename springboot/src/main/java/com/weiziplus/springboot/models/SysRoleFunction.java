package com.weiziplus.springboot.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * 系统权限表
 * sys_role_function
 *
 * @author WeiziPlus
 * @date 2019-07-23 09:46:32
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("sys_role_function")
@Accessors(chain = true)
@Alias("SysRoleFunction")
public class SysRoleFunction implements Serializable {
    /**
     * 角色功能表主键，自增
     */
    @Id("id")
    private Long id;

    /**
     * 角色表id
     */
    @Column("role_id")
    private Long roleId;

    /**
     * 功能表id
     */
    @Column("function_id")
    private Long functionId;

    private static final long serialVersionUID = 1L;
}