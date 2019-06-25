package com.weiziplus.springboot.utils;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回结果处理
 *
 * @author wanglongwei
 * @data 2019/5/7 17:06
 */
@Getter
@Setter
public class PageUtil implements Serializable {

    private Integer pageNum;

    private Integer pageSize;

    private Long total;

    private List list;

    public static PageUtil pageInfo(List list) {
        PageInfo pageInfo = new PageInfo(list);
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageInfo.getPageNum());
        pageUtil.setPageSize(pageInfo.getSize());
        pageUtil.setTotal(pageInfo.getTotal());
        pageUtil.setList(pageInfo.getList());
        return pageUtil;
    }
}
