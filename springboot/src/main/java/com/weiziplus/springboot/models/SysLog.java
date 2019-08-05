package com.weiziplus.springboot.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统日志表
 * sys_log
 *
 * @author WeiziPlus
 * @date 2019-07-23 09:46:32
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("sys_log")
public class SysLog implements Serializable {
    /**
     * 系统日志表主键，自增
     */
    @Id("id")
    private Long id;

    /**
     * 用户表id
     */
    @Column("user_id")
    private Long userId;

    /**
     * 操作描述
     */
    @Column("description")
    private String description;

    /**
     * 用户最后活跃ip地址
     */
    @Column("last_ip_address")
    private String lastIpAddress;

    /**
     * 创建时间
     */
    @Column("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;
}