package com.weiziplus.springboot.core.pc.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @date 2020/02/18 09/27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
public class SysLogVo implements Serializable {

    @ApiModelProperty("系统日志表主键，自增")
    private Long id;

    @ApiModelProperty("用户表id")
    private Long userId;

    @ApiModelProperty("操作描述")
    private String description;

    @ApiModelProperty("用户最后活跃地址")
    private String ipAddress;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户真实姓名")
    private String realName;

    @ApiModelProperty("用户角色")
    private String roleName;

    private static final long serialVersionUID = 1L;
}