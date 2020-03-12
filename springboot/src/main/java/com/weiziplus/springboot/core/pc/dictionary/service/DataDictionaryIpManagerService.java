package com.weiziplus.springboot.core.pc.dictionary.service;

import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.enums.DataDictionaryCode;
import com.weiziplus.springboot.common.models.DataDictionaryValue;
import com.weiziplus.springboot.common.util.DateUtils;
import com.weiziplus.springboot.common.util.ResultUtils;
import com.weiziplus.springboot.common.util.ToolUtils;
import com.weiziplus.springboot.common.util.redis.RedisUtils;
import com.weiziplus.springboot.common.util.token.AdminTokenUtils;
import com.weiziplus.springboot.core.pc.dictionary.mapper.DataDictionaryIpManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wanglongwei
 * @date 2020/02/24 20/01
 */
@Service
public class DataDictionaryIpManagerService extends BaseService {

    /**
     * ip规则:允许全部
     */
    private final static String IP_ROLE_VALUE_ALL = "all";

    /**
     * ip规则:只允许白名单
     */
    public final static String IP_ROLE_VALUE_WHITE = "white";

    /**
     * ip规则:只允许黑名单
     */
    public final static String IP_ROLE_VALUE_BLACK = "black";

    @Autowired
    DataDictionaryIpManagerMapper mapper;

    /**
     * ip管理基础redis的key
     */
    private static final String BASE_REDIS_KEY = createOnlyRedisKeyPrefix();

    /**
     * 获取ip规则的值
     * 默认为all
     *
     * @return
     */
    public String getIpRoleValue() {
        String redisKey = createRedisKey(BASE_REDIS_KEY + "getIpRoleValue:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return String.valueOf(object);
        }
        DataDictionaryValue dataDictionaryValue = baseFindOneDataByClassAndColumnAndValue(
                DataDictionaryValue.class, DataDictionaryValue.COLUMN_DICTIONARY_CODE, DataDictionaryCode.IP_ROLE.getCode());
        String value = null == dataDictionaryValue ? IP_ROLE_VALUE_ALL : dataDictionaryValue.getValue();
        RedisUtils.set(redisKey, value);
        return value;
    }

    /**
     * 获取ip规则
     *
     * @return
     */
    public ResultUtils<String> getIpRole() {
        String redisKey = createRedisKey(BASE_REDIS_KEY + "getIpRole:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ResultUtils.success(String.valueOf(object));
        }
        String value = getIpRoleValue();
        RedisUtils.set(redisKey, value);
        return ResultUtils.success(value);
    }

    /**
     * 更新ip规则
     *
     * @param role
     * @return
     */
    public ResultUtils updateIpRole(HttpServletRequest request, String role) {
        Long userId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(userId)) {
            AdminTokenUtils.deleteToken(userId);
            return ResultUtils.error("您没有权限");
        }
        if (ToolUtils.isBlank(role)) {
            return ResultUtils.error("规则不能为空");
        }
        //如果不是这三种类型
        if (!IP_ROLE_VALUE_ALL.equals(role)
                && !IP_ROLE_VALUE_WHITE.equals(role)
                && !IP_ROLE_VALUE_BLACK.equals(role)) {
            return ResultUtils.error("规则错误");
        }
        DataDictionaryValue dataDictionaryValue = baseFindOneDataByClassAndColumnAndValue(
                DataDictionaryValue.class, DataDictionaryValue.COLUMN_DICTIONARY_CODE, DataDictionaryCode.IP_ROLE.getCode());
        if (null != dataDictionaryValue) {
            dataDictionaryValue.setValue(role).setCreateTime(null);
            RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
            baseUpdate(dataDictionaryValue);
            RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
            return ResultUtils.success();
        }
        dataDictionaryValue = new DataDictionaryValue()
                .setDictionaryCode(DataDictionaryCode.IP_ROLE.getCode())
                .setValue(role)
                .setName("ip规则")
                .setCreateTime(DateUtils.getNowDateTime())
                .setRemark("ip拦截的规则,允许全部、只允许白名单、只禁止黑名单");
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        baseInsert(dataDictionaryValue);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 获取ip名单列表
     *
     * @param ipAddress
     * @param type
     * @return
     */
    public ResultUtils<List<DataDictionaryValue>> getIpList(String ipAddress, String type) {
        //如果type不为空
        if (!ToolUtils.isBlank(type)) {
            //type必须是ip名单值的其中之一
            if (!DataDictionaryCode.contains(type)) {
                return ResultUtils.error("类型错误");
            }
        }
        String redisKey = createRedisKey(BASE_REDIS_KEY + "getIpList:", ipAddress, type);
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ResultUtils.success(ToolUtils.objectOfList(object, DataDictionaryValue.class));
        }
        List<DataDictionaryValue> valueList = mapper.getIpList(ipAddress, type);
        RedisUtils.set(redisKey, valueList);
        return ResultUtils.success(valueList);
    }

    /**
     * 获取ip白名单列表
     *
     * @return
     */
    public Set<String> getIpValueListWhite() {
        String redisKey = createRedisKey(BASE_REDIS_KEY + "getIpValueListWhite:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> ipValueList = mapper.getIpValueList(DataDictionaryCode.IP_LIST_WHITE.getCode());
        RedisUtils.set(redisKey, ipValueList);
        return ipValueList;
    }

    /**
     * 获取ip黑名单列表
     *
     * @return
     */
    public Set<String> getIpValueListBlack() {
        String redisKey = createRedisKey(BASE_REDIS_KEY + "getIpValueListBlack:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> ipValueList = mapper.getIpValueList(DataDictionaryCode.IP_LIST_BLACK.getCode());
        RedisUtils.set(redisKey, ipValueList);
        return ipValueList;
    }

    /**
     * 处理异常ip
     *
     * @param ipAddress
     */
    public void handleAbnormalIp(String ipAddress) {
        if (ToolUtils.isBlank(ipAddress)) {
            return;
        }
        DataDictionaryValue dictionaryValue = mapper.getOneIpInfoByIpAddress(ipAddress);
        if (null == dictionaryValue) {
            dictionaryValue = new DataDictionaryValue()
                    .setDictionaryCode(DataDictionaryCode.IP_LIST_ABNORMAL.getCode())
                    .setValue(ipAddress)
                    .setNum(1)
                    .setName(ipAddress)
                    .setRemark("单位时间内访问频率过快的ip")
                    .setCreateTime(DateUtils.getNowDateTime());
            RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
            baseInsert(dictionaryValue);
            RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
            return;
        }
        //如果ip不是异常ip类型
        if (!DataDictionaryCode.IP_LIST_ABNORMAL.getCode().equals(dictionaryValue.getDictionaryCode())) {
            return;
        }
        Integer num = dictionaryValue.getNum();
        num++;
        //异常最大次数---超过将自动拉黑
        int maxNumber = 20;
        if (maxNumber > num) {
            dictionaryValue.setNum(num);
        } else {
            dictionaryValue
                    .setDictionaryCode(DataDictionaryCode.IP_LIST_BLACK.getCode())
                    .setRemark("单位时间内访问频率过快的ip,异常次数过多，自动拉黑")
                    .setCreateTime(DateUtils.getNowDateTime());
        }
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        baseUpdate(dictionaryValue);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
    }

    /**
     * 新增ip
     *
     * @param type
     * @param ipAddress
     * @param remark
     * @return
     */
    public ResultUtils addIp(HttpServletRequest request, String type, String ipAddress, String remark) {
        Long userId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(userId)) {
            AdminTokenUtils.deleteToken(userId);
            return ResultUtils.error("您没有权限");
        }
        if (ToolUtils.isBlank(type)) {
            return ResultUtils.error("类型不能为空");
        }
        if (ToolUtils.isBlank(ipAddress)) {
            return ResultUtils.error("ip地址不能为空");
        }
        //type必须是ip名单值的其中之一
        if (!DataDictionaryCode.contains(type)) {
            return ResultUtils.error("类型错误");
        }
        DataDictionaryValue dictionaryValue = mapper.getOneIpInfoByIpAddress(ipAddress);
        if (null != dictionaryValue) {
            Map<String, String> ipListMap = new HashMap<String, String>(3) {{
                put(DataDictionaryCode.IP_LIST_WHITE.getCode(), "白名单");
                put(DataDictionaryCode.IP_LIST_BLACK.getCode(), "黑名单");
                put(DataDictionaryCode.IP_LIST_ABNORMAL.getCode(), "异常ip");
            }};
            return ResultUtils.error("ip存在，当前类型为" + ipListMap.get(dictionaryValue.getDictionaryCode()));
        }
        dictionaryValue = new DataDictionaryValue()
                .setDictionaryCode(type)
                .setValue(ipAddress)
                .setName(ipAddress)
                .setRemark(remark)
                .setCreateTime(DateUtils.getNowDateTime());
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        baseInsert(dictionaryValue);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtils.success();
    }

    /**
     * 删除ip
     *
     * @param id
     * @return
     */
    public ResultUtils deleteIp(HttpServletRequest request, Integer id) {
        Long userId = AdminTokenUtils.getUserIdByHttpServletRequest(request);
        if (!GlobalConfig.SUPER_ADMIN_ID.equals(userId)) {
            AdminTokenUtils.deleteToken(userId);
            return ResultUtils.error("您没有权限");
        }
        if (null == id || 0 > id) {
            return ResultUtils.error("id错误");
        }
        RedisUtils.setExpireDeleteLikeKey(BASE_REDIS_KEY);
        baseDeleteByClassAndId(DataDictionaryValue.class, id);
        RedisUtils.deleteLikeKey(BASE_REDIS_KEY);
        return ResultUtils.success();
    }
}