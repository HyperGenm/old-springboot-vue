package com.weiziplus.springboot.filter;

import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.mapper.data.dictionary.DataDictionaryAbnormalIpMapper;
import com.weiziplus.springboot.models.DataDictionaryValue;
import com.weiziplus.springboot.service.data.dictionary.DataDictionaryAbnormalIpService;
import com.weiziplus.springboot.service.data.dictionary.DataDictionaryIpFilterService;
import com.weiziplus.springboot.util.DateUtils;
import com.weiziplus.springboot.util.HttpRequestUtils;
import com.weiziplus.springboot.util.ToolUtils;
import com.weiziplus.springboot.util.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author wanglongwei
 * @date 2019/8/5 9:25
 */
@Slf4j
@Component
public class IpFilter extends BaseService implements Filter {

    @Autowired
    DataDictionaryIpFilterService dataDictionaryIpFilterService;

    @Autowired
    DataDictionaryAbnormalIpMapper dataDictionaryAbnormalIpMapper;

    @Autowired
    DataDictionaryAbnormalIpService dataDictionaryAbnormalIpService;

    /**
     * 10秒内多少次请求，暂时封ip
     */
    @Value("${global.ip-filter-max-num:77}")
    private final Integer MAX_NUM = 77;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String ipAddress = HttpRequestUtils.getIpAddress(request);
        //如果是白名单，放过
        Set<String> ipWhiteList = dataDictionaryIpFilterService.getIpValueWhiteList();
        if (ipWhiteList.contains(ipAddress)) {
            chain.doFilter(req, res);
            return;
        }
        //如果在黑名单里面---直接403
        Set<String> ipBlackList = dataDictionaryIpFilterService.getIpValueBlackList();
        if (ipBlackList.contains(ipAddress)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        //查看ip是否被临时封号
        String warnRedisKey = "ip:filter:warn:" + ipAddress;
        Object warnObject = RedisUtils.get(warnRedisKey);
        if (null != warnObject) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String redisKey = "ip:filter:info:" + ipAddress;
        int number = 0;
        Object numberObject = RedisUtils.get(redisKey);
        if (null != numberObject) {
            number = (int) numberObject;
        }
        number += 1;
        //如果访问频率过快超出限制
        if (number >= MAX_NUM) {
            //暂时封号---3分钟后恢复
            RedisUtils.set(warnRedisKey, true, 3 * 60L);
            handleAbnormalIp(ipAddress);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (null != numberObject) {
            RedisUtils.setNotChangeTimeOut(redisKey, number);
        } else {
            RedisUtils.set(redisKey, number, 10L);
        }
        chain.doFilter(req, res);
    }

    /**
     * 处理异常ip
     *
     * @param ip
     */
    private void handleAbnormalIp(String ip) {
        DataDictionaryValue oneInfoByIp = dataDictionaryAbnormalIpMapper.getOneInfoByIp(ip);
        String nowDateTime = DateUtils.getNowDateTime();
        if (null == oneInfoByIp) {
            dataDictionaryAbnormalIpService.add(ip);
            return;
        }
        Integer oldNumber = oneInfoByIp.getOrder();
        //异常次数加一
        oneInfoByIp.setOrder(oldNumber + 1);
        oneInfoByIp.setRemark("最后一次异常时间" + nowDateTime);
        baseUpdate(oneInfoByIp);
        //异常最大次数---超过将自动拉黑
        int maxNumber = 10;
        if (maxNumber <= oldNumber) {
            dataDictionaryIpFilterService.autoAddBlack(ip);
        }
    }
}
