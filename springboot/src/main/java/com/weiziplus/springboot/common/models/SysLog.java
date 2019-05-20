package com.weiziplus.springboot.common.models;

import com.weiziplus.springboot.common.base.Column;
import com.weiziplus.springboot.common.base.Id;
import com.weiziplus.springboot.common.base.Table;
import lombok.Data;

/**
 * @author wanglongwei
 * @data 2019/5/13 15:04
 */
@Data
@Table("sys_log")
public class SysLog {
    @Id("id")
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("description")
    private String description;

    @Column("create_time")
    private String createTime;
}
