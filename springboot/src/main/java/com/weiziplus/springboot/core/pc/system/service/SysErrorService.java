package com.weiziplus.springboot.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.models.SysError;
import com.weiziplus.springboot.common.util.PageUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.core.pc.system.mapper.SysErrorMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/03/21 15/13
 */
@Slf4j
@Service
public class SysErrorService extends BaseService {

    @Autowired
    SysErrorMapper mapper;

    /**
     * 系统异常
     *
     * @param pageNum
     * @param pageSize
     * @param createTime
     * @return
     */
    public ResultUtils<PageUtils<List<SysError>>> getPageList(Integer pageNum, Integer pageSize, String createTime) {
        if (0 >= pageNum || 0 >= pageSize) {
            return ResultUtils.error("pageNum,pageSize错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<List<SysError>> pageUtil = PageUtils.pageInfo(mapper.getList(createTime));
        return ResultUtils.success(pageUtil);
    }
}
