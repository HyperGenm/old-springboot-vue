package com.weiziplus.springboot.service.system;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.models.SysAbnormalIp;
import com.weiziplus.springboot.util.PageUtils;
import com.weiziplus.springboot.util.ResultUtils;
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
    public ResultUtils getPageList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageUtils pageUtil = PageUtils.pageInfo(baseFindAllByClass(SysAbnormalIp.class));
        return ResultUtils.success(pageUtil);
    }
}
