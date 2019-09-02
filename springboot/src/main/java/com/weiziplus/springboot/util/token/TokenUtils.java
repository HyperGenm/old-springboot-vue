package com.weiziplus.springboot.util.token;

import com.weiziplus.springboot.config.GlobalConfig;
import com.weiziplus.springboot.util.redis.StringRedisUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户token配置
 *
 * @author wanglongwei
 * @data 2019/5/8 9:06
 */
@Slf4j
public class TokenUtils {

    /**
     * 根据用户id获取用户Redis的key值
     * --------------:为文件夹
     *
     * @param audience---用户角色
     * @param userId---用户ID
     * @return
     */
    public static String getAudienceRedisKey(String audience, Long userId) {
        return GlobalConfig.TOKEN.concat(":").concat(audience).concat(":") + userId;
    }

    /**
     * 根据用户id创建token
     *
     * @param audience---用户角色
     * @param userId---用户ID
     * @param expireTime---过期时间
     * @return
     */
    protected static String createToken(String audience, Long userId, Long expireTime, String ipAddress) {
        String token = JwtTokenUtils.createToken(userId, audience, ipAddress);
        StringRedisUtils.set(getAudienceRedisKey(audience, userId), token, expireTime);
        return token;
    }
}
