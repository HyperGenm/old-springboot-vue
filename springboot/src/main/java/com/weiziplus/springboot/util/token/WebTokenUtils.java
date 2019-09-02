package com.weiziplus.springboot.util.token;

import com.weiziplus.springboot.util.ToolUtils;
import com.weiziplus.springboot.util.redis.StringRedisUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * web用户token配置
 *
 * @author wanglongwei
 * @data 2019/5/8 9:01
 */
public class WebTokenUtils {
    /**
     * web用户
     */
    public final static String AUDIENCE = "web";

    /**
     * web用户redis过期时间--3天过期
     */
    private static Long EXPIRE_TIME = 60L * 60 * 24 * 3;

    @Value("${global.token-web-expire-time:259200}")
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
        return TokenUtils.getAudienceRedisKey(AUDIENCE, userId);
    }

    /**
     * 根据web用户id创建token
     *
     * @param userId
     * @return
     */
    public static String createToken(Long userId, String ipAddress) {
        return TokenUtils.createToken(AUDIENCE, userId, EXPIRE_TIME, ipAddress);
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
}
