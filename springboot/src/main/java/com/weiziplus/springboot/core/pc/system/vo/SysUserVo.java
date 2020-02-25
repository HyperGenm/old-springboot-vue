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
public class SysUserVo implements Serializable {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("是否允许登录;0:允许，1:禁止")
    private Integer allowLogin;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("最后活跃时间")
    private String laseActiveTime;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("用户权限")
    private String roleName;

    @ApiModelProperty("最后活跃ip地址")
    private String laseIpAddress;

    @ApiModelProperty("头像")
    private String icon;

    private static final long serialVersionUID = 1L;
}