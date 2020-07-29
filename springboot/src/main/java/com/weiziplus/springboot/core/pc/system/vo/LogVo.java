package com.weiziplus.springboot.core.pc.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.springboot.common.base.BaseId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @date 2020/02/28 20/49
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("用户日志")
public class LogVo implements Serializable {
    /**
     */
    @BaseId("id")
    private Long id;

    /**
     * 用户表主键
     */
    @ApiModelProperty("用户表主键")
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    /**
     * 请求的url
     */
    @ApiModelProperty("请求的url")
    private String url;

    /**
     * 当前请求的参数
     */
    @ApiModelProperty("当前请求的参数")
    private String param;

    /**
     * 请求的类型,1:查询,2:新增,3:修改,4:删除
     */
    @ApiModelProperty("请求的类型,1:查询,2:新增,3:修改,4:删除")
    private Integer type;

    /**
     * 操作描述
     */
    @ApiModelProperty("操作描述")
    private String description;

    /**
     * ip地址
     */
    @ApiModelProperty("ip地址")
    private String ipAddress;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createTime;

}