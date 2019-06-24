package com.weiziplus.springboot.models;

import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @data 2019/5/9 11:58
 */
@Data
@Table("sys_role_function")
public class SysRoleFunction implements Serializable {
    @Id("id")
    private Long id;

    @Column("role_id")
    private Long roleId;

    @Column("function_id")
    private Long functionId;
}
