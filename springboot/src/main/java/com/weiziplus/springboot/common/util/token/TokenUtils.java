package com.weiziplus.springboot.common.util.token;

import com.weiziplus.springboot.common.base.BaseService;
import com.weiziplus.springboot.common.util.redis.StringRedisUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

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
    public static String getAudienceRedisKey(String audience, String userId) {
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
    protected static String createToken(String audience, String userId, Long expireTime, HttpServletRequest request, ExpandModel expandModel) {
        String token = JwtTokenUtils.createToken(userId, audience, request, expandModel);
        StringRedisUtils.set(getAudienceRedisKey(audience, userId), token, expireTime);
        return token;
    }
}
