package com.weiziplus.springboot.util.token;

import com.weiziplus.springboot.util.redis.StringRedisUtils;

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
    private static final Long EXPIRE_TIME = 60L * 60 * 24 * 3;

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
    public static String createToken(Long userId) {
        return TokenUtils.createToken(AUDIENCE, userId, EXPIRE_TIME);
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
