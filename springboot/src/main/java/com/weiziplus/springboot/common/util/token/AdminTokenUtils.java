package com.weiziplus.springboot.common.util.token;

import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.util.Base64Utils;
import com.weiziplus.springboot.common.util.ToolUtils;
import com.weiziplus.springboot.common.util.redis.StringRedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统用户token配置
 *
 * @author wanglongwei
 * @date 2019/5/7 9:53
 */
@Component
public class AdminTokenUtils extends JwtTokenUtils {

    /**
     * 系统用户
     */
    public final static String AUDIENCE = "admin";

    /**
     * 系统用户redis过期时间--3小时过期
     */
    private static Long EXPIRE_TIME = 60L * 60 * 3;

    @Value("${global.token.expire-time.admin:10800}")
    private void setExpireTime(String expireTime) {
        AdminTokenUtils.EXPIRE_TIME = ToolUtils.valueOfLong(expireTime);
    }

    /**
     * 根据系统用户id获取系统用户Redis的key值
     *
     * @param userId
     * @return
     */
    public static String getAudienceRedisKey(Long userId) {
        return TokenUtils.getAudienceRedisKey(AUDIENCE, String.valueOf(userId));
    }

    /**
     * 根据系统用户id创建token
     *
     * @param userId
     * @return
     */
    public static String createToken(Long userId, String ipAddress, Integer roleId) {
        return TokenUtils.createToken(AUDIENCE, String.valueOf(userId), EXPIRE_TIME, ipAddress, roleId);
    }

    /**
     * 根据系统用户id更新token过期时间
     *
     * @param userId
     * @return
     */
    public static Boolean updateExpireTime(Long userId) {
        return StringRedisUtils.expire(getAudienceRedisKey(userId), EXPIRE_TIME);
    }

    /**
     * 根据系统用户id删除token
     *
     * @param userId
     * @return
     */
    public static Boolean deleteToken(Long userId) {
        return StringRedisUtils.delete(getAudienceRedisKey(userId));
    }

    /**
     * 根据token获取用户id
     *
     * @param token
     * @return
     */
    public static Long getUserIdByToken(String token) {
        return Long.valueOf(Base64Utils.decode(getTokenBody(token).getId()));
    }

    /**
     * 根据request获取用户id
     *
     * @param request
     * @return
     */
    public static Long getUserIdByHttpServletRequest(HttpServletRequest request) {
        return Long.valueOf(Base64Utils.decode(getTokenBody(request.getHeader(GlobalConfig.TOKEN)).getId()));
    }

    /**
     * 根据token获取用户角色
     *
     * @param token
     * @return
     */
    public static Integer getRoleIdByToken(String token) {
        return ToolUtils.valueOfInteger(Base64Utils.decode(getTokenBody(token).getSubject()));
    }
}