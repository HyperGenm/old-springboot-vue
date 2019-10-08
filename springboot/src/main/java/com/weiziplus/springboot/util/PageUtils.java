package com.weiziplus.springboot.util;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回结果处理
 *
 * @author wanglongwei
 * @date 2019/5/7 17:06
 */
@Getter
@Setter
@ApiModel("分页")
public class PageUtils implements Serializable {

    @ApiModelProperty("当前页码")
    private Integer pageNum;

    @ApiModelProperty("每页数量")
    private Integer pageSize;

    @ApiModelProperty("总条数")
    private Long total;

    @ApiModelProperty("数据")
    private List list;

    public static PageUtils pageInfo(List list) {
        PageInfo pageInfo = new PageInfo(list);
        PageUtils pageUtil = new PageUtils();
        pageUtil.setPageNum(pageInfo.getPageNum());
        pageUtil.setPageSize(pageInfo.getSize());
        pageUtil.setTotal(pageInfo.getTotal());
        pageUtil.setList(pageInfo.getList());
        return pageUtil;
    }

    private static final long serialVersionUID = 1L;
}
