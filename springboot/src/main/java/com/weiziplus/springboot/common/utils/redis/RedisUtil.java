package com.weiziplus.springboot.common.utils.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * redis
 *
 * @author wanglongwei
 * @data 2019/4/22 16:10
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static RedisUtil that;

    @PostConstruct
    protected void init() {
        that = this;
    }

    /**
     * 根据key和value存入redis
     *
     * @param key
     * @param value
     */
    public static void set(String key, String value) {
        that.redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 根据key和value存入redis
     *
     * @param key
     * @param value
     * @param timeout 过期时间：单位秒
     */
    public static void set(String key, String value, Long timeout) {
        that.redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 根据key获取内容
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return that.redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据key设置过期时间
     *
     * @param key
     * @param timeout 过期时间：单位秒
     * @return
     */
    public static boolean expire(String key, Long timeout) {
        return that.redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 判断某个key是否存在
     *
     * @param key
     * @return
     */
    public static boolean hasKye(String key) {
        return that.redisTemplate.hasKey(key);
    }

    /**
     * 根据key删除
     *
     * @param key
     * @return
     */
    public static boolean delete(String key) {
        return that.redisTemplate.delete(key);
    }

    /**
     * 删除多个
     *
     * @param keys
     * @return
     */
    public static Long delete(Collection<String> keys) {
        return that.redisTemplate.delete(keys);
    }
}
