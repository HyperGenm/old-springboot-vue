package com.weiziplus.springboot.common.util.redis;

import com.weiziplus.springboot.common.util.ToolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wanglongwei
 * @date 2019/7/19 10:52
 */
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static RedisUtils that;

    @PostConstruct
    private void init() {
        that = this;
    }

    /**
     * 当前项目存放的redis前缀
     */
    private static final String BASE_REDIS_KEY = ToolUtils.getLocalUserDirMd5() + ":";

    /**
     * 根据key和value存入redis---默认一小时过期
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        if (null == key) {
            return;
        }
        long timeOut = 60 * 60L + Math.round(Math.random() * 7);
        that.redisTemplate.opsForValue().set(BASE_REDIS_KEY + key, value, timeOut, TimeUnit.SECONDS);
    }

    /**
     * 根据key和value存入redis
     *
     * @param key
     * @param value
     * @param timeout 过期时间：单位秒
     */
    public static void set(String key, Object value, Long timeout) {
        if (null == key) {
            return;
        }
        if (null == timeout) {
            timeout = 60 * 60L + Math.round(Math.random() * 7);
        }
        that.redisTemplate.opsForValue().set(BASE_REDIS_KEY + key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 根据key和value存入redis---不改变原来过期时间，只改变值
     *
     * @param key
     * @param value
     */
    public static void setNotChangeTimeOut(String key, Object value) {
        if (null == key) {
            return;
        }
        that.redisTemplate.opsForValue().set(BASE_REDIS_KEY + key, value, 0L);
    }

    /**
     * 根据key获取内容
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        if (null == key) {
            return null;
        }
        return that.redisTemplate.opsForValue().get(BASE_REDIS_KEY + key);
    }

    /**
     * 根据key设置过期时间
     *
     * @param key
     * @param timeout 过期时间：单位秒
     * @return
     */
    public static boolean setExpire(String key, Long timeout) {
        if (null == key || null == timeout) {
            return false;
        }
        return that.redisTemplate.expire(BASE_REDIS_KEY + key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 根据key获取内容
     *
     * @param key
     * @return
     */
    public static Long getExpire(String key) {
        if (null == key) {
            return null;
        }
        return that.redisTemplate.getExpire(BASE_REDIS_KEY + key);
    }

    /**
     * 判断某个key是否存在
     *
     * @param key
     * @return
     */
    public static boolean hasKye(String key) {
        if (null == key) {
            return false;
        }
        return that.redisTemplate.hasKey(BASE_REDIS_KEY + key);
    }

    /**
     * 根据key删除
     *
     * @param key
     * @return
     */
    public static boolean delete(String key) {
        if (null == key) {
            return false;
        }
        return that.redisTemplate.delete(BASE_REDIS_KEY + key);
    }

    /**
     * 删除多个
     *
     * @param keys
     * @return
     */
    public static Long delete(Set<String> keys) {
        if (null == keys) {
            return null;
        }
        Set<String> newSet = new HashSet<>(keys.size());
        for (String key : keys) {
            newSet.add(BASE_REDIS_KEY + key);
        }
        return that.redisTemplate.delete(newSet);
    }

    /**
     * 模糊删除
     *
     * @param key
     * @return
     */
    public static Long deleteLikeKey(String key) {
        if (null == key) {
            return null;
        }
        Set<String> keys = that.redisTemplate.keys(BASE_REDIS_KEY + key + "*");
        if (null == keys || 0 >= keys.size()) {
            return 0L;
        }
        return that.redisTemplate.delete(keys);
    }

    /**
     * 设置过期时间删除,模糊查询key
     *
     * @param key
     */
    public static void setExpireDeleteLikeKey(String key) {
        if (null == key) {
            return;
        }
        Set<String> keys = that.redisTemplate.keys(BASE_REDIS_KEY + key + "*");
        if (null == keys || 0 >= keys.size()) {
            return;
        }
        for (String k : keys) {
            that.redisTemplate.expire(k, 3L, TimeUnit.SECONDS);
        }
    }
}
