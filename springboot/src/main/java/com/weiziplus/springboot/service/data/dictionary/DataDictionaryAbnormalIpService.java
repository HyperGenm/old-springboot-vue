package com.weiziplus.springboot.service.data.dictionary;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.mapper.data.dictionary.DataDictionaryAbnormalIpMapper;
import com.weiziplus.springboot.models.DataDictionaryValue;
import com.weiziplus.springboot.util.DateUtils;
import com.weiziplus.springboot.util.PageUtils;
import com.weiziplus.springboot.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wanglongwei
 * @data 2019/9/18 19:53
 */
@Service
public class DataDictionaryAbnormalIpService extends BaseService {

    @Autowired
    DataDictionaryAbnormalIpMapper mapper;

    /**
     * ip名单数据库中code为ipFilter
     */
    public static final String DICTIONARY_CODE = "abnormalIp";

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ResultUtils getPageList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageUtils pageUtil = PageUtils.pageInfo(mapper.getList());
        return ResultUtils.success(pageUtil);
    }

    /**
     * 新增异常数据
     *
     * @param ip
     */
    public void add(String ip) {
        DataDictionaryValue value = new DataDictionaryValue();
        value.setDictionaryCode(DataDictionaryAbnormalIpService.DICTIONARY_CODE);
        value.setName(ip);
        value.setValue(ip);
        //这里的order代表异常的次数
        value.setOrder(1);
        String nowDateTime = DateUtils.getNowDateTime();
        value.setRemark("最后一次异常时间" + nowDateTime);
        value.setCreateTime(nowDateTime);
        baseInsert(value);
    }
}
