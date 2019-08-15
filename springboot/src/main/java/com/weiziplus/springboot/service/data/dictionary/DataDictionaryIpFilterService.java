package com.weiziplus.springboot.service.data.dictionary;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.config.GlobalConfig;
import com.weiziplus.springboot.mapper.data.dictionary.DataDictionaryIpFilterMapper;
import com.weiziplus.springboot.mapper.system.SysUserMapper;
import com.weiziplus.springboot.models.DataDictionaryValue;
import com.weiziplus.springboot.utils.PageUtil;
import com.weiziplus.springboot.utils.ResultUtil;
import com.weiziplus.springboot.utils.StringUtil;
import com.weiziplus.springboot.utils.redis.RedisUtil;
import com.weiziplus.springboot.utils.token.AdminTokenUtil;
import com.weiziplus.springboot.utils.token.JwtTokenUtil;
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
    private final String BASE_REDIS_KEY = "service:data:dictionary:DataDictionaryIpFilterService:";

    /**
     * 获取ip白名单列表
     *
     * @return
     */
    public List<String> getIpValueWhiteList() {
        String redisKey = BASE_REDIS_KEY + "getIpValueWhiteList";
        Object object = RedisUtil.get(redisKey);
        if (null != object) {
            return (List<String>) object;
        }
        List<String> ipWhiteList = mapper.getIpValueWhiteList();
        RedisUtil.set(redisKey, ipWhiteList, 60 * 60 * 24L);
        return ipWhiteList;
    }

    /**
     * 获取ip黑名单列表
     *
     * @return
     */
    public List<String> getIpValueBlackList() {
        String redisKey = BASE_REDIS_KEY + "getIpValueBlackList";
        Object object = RedisUtil.get(redisKey);
        if (null != object) {
            return (List<String>) object;
        }
        List<String> ipBlackList = mapper.getIpValueBlackList();
        RedisUtil.set(redisKey, ipBlackList, 60 * 60 * 24L);
        return ipBlackList;
    }

    /**
     * 获取ip名单分页列表
     *
     * @return
     */
    public ResultUtil getPageList(Integer pageNum, Integer pageSize, String type) {
        String redisKey = BASE_REDIS_KEY + "getPageListBlack:type_" + type + "pageSize_" + pageSize + "pageNum_" + pageNum;
        Object object = RedisUtil.get(redisKey);
        if (null != object) {
            return ResultUtil.success(object);
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtil pageUtil = PageUtil.pageInfo(mapper.getIpList(type));
        RedisUtil.set(redisKey, pageUtil);
        return ResultUtil.success(pageUtil);
    }

    /**
     * 新增ip名单
     *
     * @param request
     * @param ip
     * @return
     */
    public ResultUtil add(HttpServletRequest request, String ip, String type) {
        if (StringUtil.isBlank(ip)) {
            return ResultUtil.error("ip不能为空");
        }
        if (StringUtil.isBlank(type)) {
            return ResultUtil.error("type不能为空");
        }
        Long nowUserId = JwtTokenUtil.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            sysUserMapper.suspendSysUser(nowUserId);
            AdminTokenUtil.deleteToken(nowUserId);
            return ResultUtil.errorSuspend();
        }
        DataDictionaryValue oneInfoByIp = mapper.getOneInfoByIp(ip);
        if (null != oneInfoByIp && null != oneInfoByIp.getId()) {
            return ResultUtil.error("ip已存在");
        }
        mapper.addIp(ip, type);
        RedisUtil.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtil.success();
    }

    /**
     * 名单删除ip
     *
     * @param request
     * @param id
     * @return
     */
    public ResultUtil delete(HttpServletRequest request, Long id, String type) {
        //127.0.0.1的id为1
        if (null == id || 1 >= id) {
            return ResultUtil.error("id为空");
        }
        if (StringUtil.isBlank(type)) {
            return ResultUtil.error("type不能为空");
        }
        Long nowUserId = JwtTokenUtil.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            sysUserMapper.suspendSysUser(nowUserId);
            AdminTokenUtil.deleteToken(nowUserId);
            return ResultUtil.errorSuspend();
        }
        mapper.deleteIp(id, type);
        RedisUtil.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtil.success();
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
        RedisUtil.deleteLikeKey(BASE_REDIS_KEY);
    }
}
