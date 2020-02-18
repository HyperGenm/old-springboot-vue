package com.weiziplus.springboot.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.common.base.Column;
import com.weiziplus.springboot.common.base.Id;
import com.weiziplus.springboot.common.base.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

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
@Accessors(chain = true)
@Alias("SysLog")
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
     * 当前请求的参数
     */
    @Column("param")
    private String param;

    /**
     * 请求的类型,1:查询,2:新增,3:修改,4:删除
     */
    @Column("type")
    private Integer type;

    /**
     * 1:查询
     */
    public static final Integer TYPE_SELECT = 1;

    /**
     * 2:新增
     */
    public static final Integer TYPE_INSERT = 2;

    /**
     * 3:修改
     */
    public static final Integer TYPE_UPDATE = 3;

    /**
     * 4:删除
     */
    public static final Integer TYPE_DELETE = 4;

    /**
     * 用户最后活跃ip地址
     */
    @Column("ip_address")
    private String ipAddress;

    /**
     * 创建时间
     */
    @Column("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;
}