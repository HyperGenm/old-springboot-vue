package com.weiziplus.springboot.common.models;

import com.weiziplus.springboot.common.base.Column;
import com.weiziplus.springboot.common.base.Id;
import com.weiziplus.springboot.common.base.Table;
import lombok.Data;

/**
 * @author wanglongwei
 * @data 2019/5/9 11:58
 */
@Data
@Table("sys_role_function")
public class SysRoleFunction {
    @Id("id")
    private Long id;

    @Column("role_id")
    private Long roleId;

    @Column("function_id")
    private Long functionId;
}
