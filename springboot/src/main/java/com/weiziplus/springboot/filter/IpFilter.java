package com.weiziplus.springboot.filter;

import com.weiziplus.springboot.mapper.system.SysAbnormalIpMapper;
import com.weiziplus.springboot.models.SysAbnormalIp;
import com.weiziplus.springboot.service.data.dictionary.DataDictionaryIpFilterService;
import com.weiziplus.springboot.utils.HttpRequestUtil;
import com.weiziplus.springboot.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author wanglongwei
 * @data 2019/8/5 9:25
 */
@Slf4j
@Component
public class IpFilter implements Filter {

    @Autowired
    DataDictionaryIpFilterService dataDictionaryIpFilterService;

    @Autowired
    SysAbnormalIpMapper sysAbnormalIpMapper;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String ipAddress = HttpRequestUtil.getIpAddress(request);
        //如果是白名单，放过
        List<String> ipWhiteList = dataDictionaryIpFilterService.getIpValueWhiteList();
        if (ipWhiteList.contains(ipAddress)) {
            chain.doFilter(req, res);
            return;
        }
        //如果在黑名单里面---直接403
        List<String> ipBlackList = dataDictionaryIpFilterService.getIpValueBlackList();
        if (ipBlackList.contains(ipAddress)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        //查看ip是否被临时封号
        String warnRedisKey = "ip:filter:warn:" + ipAddress;
        Object warnObject = RedisUtil.get(warnRedisKey);
        if (null != warnObject) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String redisKey = "ip:filter:info:" + ipAddress;
        int number = 0;
        Object numberObject = RedisUtil.get(redisKey);
        if (null != numberObject) {
            number = (int) numberObject;
        }
        number += 1;
        int maxNumber = 27;
        //如果访问频率过快超出限制
        if (number >= maxNumber) {
            RedisUtil.set(warnRedisKey, true, 60 * 60L);
            handleAbnormalIp(ipAddress);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (null != numberObject) {
            RedisUtil.setNotChangeTimeOut(redisKey, number);
        } else {
            RedisUtil.set(redisKey, number, 5L);
        }
        chain.doFilter(req, res);
    }

    /**
     * 处理异常ip
     *
     * @param ip
     */
    private void handleAbnormalIp(String ip) {
        SysAbnormalIp oneInfoByIp = sysAbnormalIpMapper.getOneInfoByIp(ip);
        if (null == oneInfoByIp) {
            sysAbnormalIpMapper.addAbnormalIpAndRemark(ip, "访问频率过快");
            return;
        }
        sysAbnormalIpMapper.updateAbnormalIpAndRemark(ip, "访问频率过快");
        //异常最大次数
        int maxNumber = 5;
        if (maxNumber <= oneInfoByIp.getNumber()) {
            dataDictionaryIpFilterService.autoAddBlack(ip);
        }
    }
}
