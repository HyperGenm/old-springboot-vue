package com.weiziplus.springboot.service.data.dictionary;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.config.GlobalConfig;
import com.weiziplus.springboot.mapper.data.dictionary.DataDictionaryIpFilterMapper;
import com.weiziplus.springboot.mapper.system.SysUserMapper;
import com.weiziplus.springboot.models.DataDictionaryValue;
import com.weiziplus.springboot.util.PageUtils;
import com.weiziplus.springboot.util.ResultUtils;
import com.weiziplus.springboot.util.ToolUtils;
import com.weiziplus.springboot.util.redis.RedisUtils;
import com.weiziplus.springboot.util.token.AdminTokenUtils;
import com.weiziplus.springboot.util.token.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wanglongwei
 * @data 2019/8/5 9:53
 */
@Service
public class DataDictionaryIpFilterService {

    @Autowired
    DataDictionaryIpFilterMapper mapper;

    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * ipFilter基础redis的key
     */
    private static final String BASE_REDIS_KEY = "service:data:dictionary:DataDictionaryIpFilterService:";

    /**
     * 获取ip白名单列表
     *
     * @return
     */
    public List<String> getIpValueWhiteList() {
        String redisKey = BASE_REDIS_KEY + "getIpValueWhiteList";
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return JSONArray.parseArray(JSON.toJSONString(object), String.class);
        }
        List<String> ipWhiteList = mapper.getIpValueWhiteList();
        RedisUtils.set(redisKey, ipWhiteList, 60 * 60 * 24L);
        return ipWhiteList;
    }

    /**
     * 获取ip黑名单列表
     *
     * @return
     */
    public List<String> getIpValueBlackList() {
        String redisKey = BASE_REDIS_KEY + "getIpValueBlackList";
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return JSONArray.parseArray(JSON.toJSONString(object), String.class);
        }
        List<String> ipBlackList = mapper.getIpValueBlackList();
        RedisUtils.set(redisKey, ipBlackList, 60 * 60 * 24L);
        return ipBlackList;
    }

    /**
     * 获取ip名单分页列表
     *
     * @return
     */
    public ResultUtils getPageList(Integer pageNum, Integer pageSize, String type) {
        String redisKey = BASE_REDIS_KEY + "getPageListBlack:type_" + type + "pageSize_" + pageSize + "pageNum_" + pageNum;
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ResultUtils.success(object);
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils pageUtil = PageUtils.pageInfo(mapper.getIpList(type));
        RedisUtils.set(redisKey, pageUtil);
        return ResultUtils.success(pageUtil);
    }

    /**
     * 新增ip名单
     *
     * @param request
     * @param ip
     * @return
     */
    public ResultUtils add(HttpServletRequest request, String ip, String type) {
        if (ToolUtils.isBlank(ip)) {
            return ResultUtils.error("ip不能为空");
        }
        if (ToolUtils.isBlank(type)) {
            return ResultUtils.error("type不能为空");
        }
        Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            sysUserMapper.suspendSysUser(nowUserId);
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
        }
        DataDictionaryValue oneInfoByIp = mapper.getOneInfoByIp(ip);
        if (null != oneInfoByIp && null != oneInfoByIp.getId()) {
            return ResultUtils.error("ip已存在");
        }
        mapper.addIp(ip, type);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 名单删除ip
     *
     * @param request
     * @param id
     * @return
     */
    public ResultUtils delete(HttpServletRequest request, Long id, String type) {
        //127.0.0.1的id为1
        if (null == id || 1 >= id) {
            return ResultUtils.error("id为空");
        }
        if (ToolUtils.isBlank(type)) {
            return ResultUtils.error("type不能为空");
        }
        Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            sysUserMapper.suspendSysUser(nowUserId);
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
        }
        mapper.deleteIp(id, type);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 自动将ip添加到黑名单
     *
     * @param ip
     */
    public void autoAddBlack(String ip) {
        DataDictionaryValue oneInfoByIp = mapper.getOneInfoByIp(ip);
        if (null != oneInfoByIp && null != oneInfoByIp.getId()) {
            return;
        }
        mapper.addIp(ip, "black");
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
    }
}
