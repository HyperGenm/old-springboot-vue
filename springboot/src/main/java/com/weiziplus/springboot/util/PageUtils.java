package com.weiziplus.springboot.util;

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
public class PageUtils implements Serializable {

    private Integer pageNum;

    private Integer pageSize;

    private Long total;

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
}
