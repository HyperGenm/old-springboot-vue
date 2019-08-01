package com.weiziplus.springboot.utils.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * StringRedisTemplate常用工具类
 *
 * @author wanglongwei
 * @data 2019/4/22 16:10
 */
@Component
public class StringRedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static StringRedisUtil that;

    @PostConstruct
    protected void init() {
        that = this;
    }

    /**
     * 根据key和value存入redis---默认3分钟过期
     *
     * @param key
     * @param value
     */
    public static void set(String key, String value) {
        if (null == key) {
            return;
        }
        that.stringRedisTemplate.opsForValue().set(key, value, 3 * 60L, TimeUnit.SECONDS);
    }

    /**
     * 根据key和value存入redis
     *
     * @param key
     * @param value
     * @param timeout 过期时间：单位秒
     */
    public static void set(String key, String value, Long timeout) {
        if (null == key) {
            return;
        }
        if (null == timeout) {
            timeout = 3 * 60L;
        }
        that.stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 根据key和value存入redis---不改变原来过期时间，只改变值
     *
     * @param key
     * @param value
     */
    public static void setNotChangeTimeOut(String key, String value) {
        if (null == key) {
            return;
        }
        that.stringRedisTemplate.opsForValue().set(key, value, 0L);
    }

    /**
     * 根据key获取内容
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        if (null == key) {
            return null;
        }
        return that.stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 根据key设置过期时间
     *
     * @param key
     * @param timeout 过期时间：单位秒
     * @return
     */
    public static boolean expire(String key, Long timeout) {
        if (null == key) {
            return false;
        }
        return that.stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
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
        return that.stringRedisTemplate.hasKey(key);
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
        return that.stringRedisTemplate.delete(key);
    }

    /**
     * 删除多个
     *
     * @param keys
     * @return
     */
    public static Long delete(Collection<String> keys) {
        if (null == keys) {
            return null;
        }
        return that.stringRedisTemplate.delete(keys);
    }
}
