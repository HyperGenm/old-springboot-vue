package com.weiziplus.springboot.common.filter;

import com.alibaba.fastjson.JSON;
import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.util.HttpRequestUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.common.util.redis.RedisUtils;
import com.weiziplus.springboot.core.pc.dictionary.service.DataDictionaryIpManagerService;
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
    DataDictionaryIpManagerService dataDictionaryIpManagerService;

    /**
     * ipFilter基础redis的key
     */
    private static final String BASE_REDIS_KEY = createOnlyRedisKeyPrefix();

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
        //获取白名单列表
        Set<String> ipValueListWhite = dataDictionaryIpManagerService.getIpValueListWhite();
        //如果当前ip是白名单---直接放过
        if (ipValueListWhite.contains(ipAddress)) {
            chain.doFilter(req, res);
            return;
        }
        //获取ip拦截规则
        String ipRole = dataDictionaryIpManagerService.getIpRoleValue();
        //如果只允许白名单
        if (DataDictionaryIpManagerService.IP_ROLE_VALUE_WHITE.equals(ipRole)) {
            //当前ip不是白名单，返回403状态码
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print(JSON.toJSONString(ResultUtils.errorRole("access denied")));
            return;
        }
        //如果只禁止黑名单
        if (DataDictionaryIpManagerService.IP_ROLE_VALUE_BLACK.equals(ipRole)) {
            //查询黑名单列表
            Set<String> ipValueListBlack = dataDictionaryIpManagerService.getIpValueListBlack();
            //如果当前ip是黑名单，直接返回403状态码
            if (ipValueListBlack.contains(ipAddress)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().print(JSON.toJSONString(ResultUtils.errorRole("access denied")));
                return;
            }
        }
        /////////将访问频率过快的ip设置为异常ip
        //查看ip是否被临时封号
        String warnRedisKey = BASE_REDIS_KEY + "ipWarn:" + ipAddress;
        Object warnObject = RedisUtils.get(warnRedisKey);
        //如果ip异常，暂时返回403状态码
        if (null != warnObject) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print(JSON.toJSONString(ResultUtils.errorRole("access denied")));
            return;
        }
        //将当前时间访问ip次数存到redis
        String redisKey = BASE_REDIS_KEY + "ipInfo:" + ipAddress;
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
            dataDictionaryIpManagerService.handleAbnormalIp(ipAddress);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print(JSON.toJSONString(ResultUtils.errorRole("access denied")));
            return;
        }
        if (null != numberObject) {
            RedisUtils.setNotChangeTimeOut(redisKey, number);
        } else {
            RedisUtils.set(redisKey, number, 10L);
        }
        chain.doFilter(req, res);
    }

}
