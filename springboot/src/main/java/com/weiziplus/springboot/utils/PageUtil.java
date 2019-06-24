package com.weiziplus.springboot.utils;

import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页返回结果处理
 *
 * @author wanglongwei
 * @data 2019/5/7 17:06
 */
public class PageUtil {
    public static Map<String, Object> pageInfo(List list) {
        Map<String, Object> map = new HashMap<>(4);
        PageInfo pageInfo = new PageInfo(list);
        map.put("pageNum", pageInfo.getPageNum());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("total", pageInfo.getTotal());
        map.put("list", pageInfo.getList());
        return map;
    }
}
