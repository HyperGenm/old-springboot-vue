package com.weiziplus.springboot.service.data.dictionary;

import com.github.pagehelper.PageHelper;
import com.weiziplus.springboot.base.BaseService;
import com.weiziplus.springboot.config.GlobalConfig;
import com.weiziplus.springboot.mapper.data.dictionary.DataDictionaryIpFilterMapper;
import com.weiziplus.springboot.mapper.system.SysUserMapper;
import com.weiziplus.springboot.models.DataDictionaryValue;
import com.weiziplus.springboot.util.DateUtils;
import com.weiziplus.springboot.util.PageUtils;
import com.weiziplus.springboot.util.ResultUtils;
import com.weiziplus.springboot.util.ToolUtils;
import com.weiziplus.springboot.util.redis.RedisUtils;
import com.weiziplus.springboot.util.token.AdminTokenUtils;
import com.weiziplus.springboot.util.token.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author wanglongwei
 * @date 2019/8/5 9:53
 */
@Service
public class DataDictionaryIpFilterService extends BaseService {

    @Autowired
    DataDictionaryIpFilterMapper mapper;

    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * ip名单数据库中code为ipFilter
     */
    private static final String DICTIONARY_CODE = "ipFilter";

    /**
     * 白名单
     */
    private static final int TYPE_WHITE = 0;

    /**
     * 黑名单
     */
    private static final int TYPE_BLACK = 1;

    /**
     * ipFilter基础redis的key
     */
    private static final String BASE_REDIS_KEY = "service:data:dictionary:DataDictionaryIpFilterService:";

    /**
     * 获取ip白名单列表
     *
     * @return
     */
    public Set<String> getIpValueWhiteList() {
        String redisKey = createRedisKey(BASE_REDIS_KEY + "getIpValueWhiteList");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> ipWhiteList = mapper.getIpValueList(TYPE_WHITE);
        RedisUtils.set(redisKey, ipWhiteList, 60 * 60 * 24L);
        return ipWhiteList;
    }

    /**
     * 获取ip黑名单列表
     *
     * @return
     */
    public Set<String> getIpValueBlackList() {
        String redisKey = createRedisKey(BASE_REDIS_KEY + "getIpValueBlackList");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> ipBlackList = mapper.getIpValueList(TYPE_BLACK);
        RedisUtils.set(redisKey, ipBlackList, 60 * 60 * 24L);
        return ipBlackList;
    }

    /**
     * 获取ip名单分页列表
     *
     * @return
     */
    public ResultUtils getPageList(Integer pageNum, Integer pageSize, Integer type) {
        String redisKey = createRedisKey(BASE_REDIS_KEY + "getPageListBlack", type, pageSize, pageNum);
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
    public ResultUtils add(HttpServletRequest request, String ip, Integer type) {
        if (ToolUtils.isBlank(ip)) {
            return ResultUtils.error("ip不能为空");
        }
        if (null == type) {
            return ResultUtils.error("type不能为空");
        }
        if (TYPE_WHITE != type && TYPE_BLACK != type) {
            return ResultUtils.error("type错误");
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
        DataDictionaryValue value = new DataDictionaryValue();
        value.setDictionaryCode(DICTIONARY_CODE);
        value.setType(type);
        value.setName(ip);
        value.setValue(ip);
        value.setCreateTime(DateUtils.getNowDateTime());
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        baseInsert(value);
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
    public ResultUtils delete(HttpServletRequest request, Long id) {
        //127.0.0.1的id为1
        if (null == id || 1 >= id) {
            return ResultUtils.error("id为空");
        }
        Long nowUserId = JwtTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(nowUserId)) {
            sysUserMapper.suspendSysUser(nowUserId);
            AdminTokenUtils.deleteToken(nowUserId);
            return ResultUtils.errorSuspend();
        }
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        mapper.deleteIp(id);
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
        DataDictionaryValue value = new DataDictionaryValue();
        value.setDictionaryCode(DICTIONARY_CODE);
        //1为黑名单
        value.setType(TYPE_BLACK);
        value.setName(ip);
        value.setValue(ip);
        value.setCreateTime(DateUtils.getNowDateTime());
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        baseInsert(value);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
    }
}
