package com.weiziplus.springboot.common.util.token;

import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.util.Base64Utils;
import com.weiziplus.springboot.common.util.ToolUtils;
import com.weiziplus.springboot.common.util.redis.StringRedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * web用户token配置
 *
 * @author wanglongwei
 * @date 2019/5/8 9:01
 */
@Component
public class WebTokenUtils extends JwtTokenUtils{
    /**
     * web用户
     */
    public final static String AUDIENCE = "web";

    /**
     * web用户redis过期时间--1天过期
     */
    private static Long EXPIRE_TIME = 60L * 60 * 24;

    @Value("${global.token.expire-time.web:86400}")
    private void setExpireTime(String expireTime) {
        WebTokenUtils.EXPIRE_TIME = ToolUtils.valueOfLong(expireTime);
    }

    /**
     * 根据web用户id获取web用户Redis的key值
     *
     * @param userId
     * @return
     */
    public static String getAudienceRedisKey(Long userId) {
        return TokenUtils.getAudienceRedisKey(AUDIENCE, String.valueOf(userId));
    }

    /**
     * 根据web用户id创建token
     *
     * @param userId
     * @return
     */
    public static String createToken(Long userId, String ipAddress) {
        return TokenUtils.createToken(AUDIENCE, String.valueOf(userId), EXPIRE_TIME, ipAddress, null);
    }

    /**
     * 根据web用户id更新token过期时间
     *
     * @param userId
     * @return
     */
    public static Boolean updateExpireTime(Long userId) {
        return StringRedisUtils.expire(getAudienceRedisKey(userId), EXPIRE_TIME);
    }

    /**
     * 根据web用户id删除token
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

}
