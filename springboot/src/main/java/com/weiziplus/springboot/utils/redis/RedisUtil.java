package com.weiziplus.springboot.utils.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wanglongwei
 * @data 2019/7/19 10:52
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static RedisUtil that;

    @PostConstruct
    protected void init() {
        that = this;
    }

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
        that.redisTemplate.opsForValue().set(key, value, 60 * 60L, TimeUnit.SECONDS);
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
            timeout = 60 * 60L;
        }
        that.redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
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
        that.redisTemplate.opsForValue().set(key, value, 0L);
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
        return that.redisTemplate.opsForValue().get(key);
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
        return that.redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
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
        return that.redisTemplate.getExpire(key);
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
        return that.redisTemplate.hasKey(key);
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
        return that.redisTemplate.delete(key);
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
        return that.redisTemplate.delete(keys);
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
        Set<String> keys = that.redisTemplate.keys(key + "*");
        if (null == keys || 0 >= keys.size()) {
            return 0L;
        }
        for (String k : keys) {
            that.redisTemplate.expire(k, 3L, TimeUnit.SECONDS);
        }
        return that.redisTemplate.delete(keys);
    }
}
