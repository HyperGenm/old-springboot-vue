package com.weiziplus.springboot.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.base.Column;
import com.weiziplus.springboot.base.Id;
import com.weiziplus.springboot.base.Table;
import java.io.Serializable;
import lombok.Data;

/**
 * 异常ip记录
 * sys_abnormal_ip
 * @author WeiziPlus
 * @date 2019-08-05 14:20:29
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table("sys_abnormal_ip")
public class SysAbnormalIp implements Serializable {
    /**
     * 自增
     */
    @Id("id")
    private Long id;

    /**
     * ip地址
     */
    @Column("ip")
    private String ip;

    /**
     * 次数
     */
    @Column("number")
    private Integer number;

    /**
     * 最近违规时间
     */
    @Column("lase_time")
    private String laseTime;

    /**
     * 创建时间
     */
    @Column("create_time")
    private String createTime;

    private static final long serialVersionUID = 1L;
}