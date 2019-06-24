package com.weiziplus.springboot.utils.token;

import com.weiziplus.springboot.utils.redis.StringRedisUtil;

/**
 * 系统用户token配置
 *
 * @author wanglongwei
 * @data 2019/5/7 9:53
 */
public class AdminTokenUtil {

    /**
     * 系统用户
     */
    public final static String AUDIENCE = "admin";

    /**
     * 系统用户redis过期时间--12小时过期
     */
    private static final Long EXPIRE_TIME = 60L * 60 * 12;

    /**
     * 根据系统用户id获取系统用户Redis的key值
     *
     * @param userId
     * @return
     */
    public static String getAudienceRedisKey(Long userId) {
        return TokenUtil.getAudienceRedisKey(AUDIENCE, userId);
    }

    /**
     * 根据系统用户id创建token
     *
     * @param userId
     * @return
     */
    public static String createToken(Long userId) {
        return TokenUtil.createToken(AUDIENCE, userId, EXPIRE_TIME);
    }

    /**
     * 根据系统用户id更新token过期时间
     *
     * @param userId
     * @return
     */
    public static Boolean updateExpireTime(Long userId) {
        return StringRedisUtil.expire(getAudienceRedisKey(userId), EXPIRE_TIME);
    }

    /**
     * 根据系统用户id删除token
     *
     * @param userId
     * @return
     */
    public static Boolean deleteToken(Long userId) {
        return StringRedisUtil.delete(getAudienceRedisKey(userId));
    }
}
