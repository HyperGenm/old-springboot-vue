package com.weiziplus.springboot.service.system;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.models.SysAbnormalIp;
import com.weiziplus.springboot.utils.PageUtil;
import com.weiziplus.springboot.utils.ResultUtil;
import org.springframework.stereotype.Service;

/**
 * @author wanglongwei
 * @data 2019/8/5 14:36
 */
@Service
public class SysAbnormalIpService extends BaseService {

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ResultUtil getPageList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageUtil pageUtil = PageUtil.pageInfo(baseFindAllByClass(SysAbnormalIp.class));
        return ResultUtil.success(pageUtil);
    }
}
