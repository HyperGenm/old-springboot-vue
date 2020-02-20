package com.weiziplus.springboot.common.util.token;

import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.config.GlobalConfig;
import com.weiziplus.springboot.common.util.Md5Utils;
import com.weiziplus.springboot.common.util.redis.StringRedisUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户token配置
 *
 * @author wanglongwei
 * @date 2019/5/8 9:06
 */
@Slf4j
public class TokenUtils extends BaseService {

    /**
     * BaseService基础redis的key
     */
    private static final String BASE_REDIS_KEY = createOnlyRedisKeyPrefix();

    /**
     * 根据用户id获取用户Redis的key值
     * --------------:为文件夹
     *
     * @param audience---用户角色
     * @param userId---用户ID
     * @return
     */
    public static String getAudienceRedisKey(String audience, Long userId) {
        //如果是生产环境
        if (GlobalConfig.isSpringProfilesPro()) {
            return Md5Utils.encode(BASE_REDIS_KEY.concat(audience).concat(":") + userId);
        }
        return BASE_REDIS_KEY.concat(audience).concat(":") + userId;
    }

    /**
     * 根据用户id创建token
     *
     * @param audience---用户角色
     * @param userId---用户ID
     * @param expireTime---过期时间
     * @return
     */
    protected static String createToken(String audience, Long userId, Long expireTime, String ipAddress, Integer roleId) {
        String token = JwtTokenUtils.createToken(userId, audience, ipAddress, roleId);
        StringRedisUtils.set(getAudienceRedisKey(audience, userId), token, expireTime);
        return token;
    }
}
