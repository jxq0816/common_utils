package com.inter3i.base;

/**
 * Created by boxiaotong on 2017/4/20.
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存控制
 * Created by lay on 2017/4/13 0013.
 */
public class CacheUtils {
    // 静态Map模拟储存
    private static Map<String, Object> caches = new ConcurrentHashMap<String, Object>();

    // 静态方法模拟添加缓存
    public static void addCache(String key, Object object) throws RuntimeException {
        if (key == null || "".equals(key)) {
            throw new RuntimeException("Method: addCache, cache key can not be empty");
        }
        if (object == null) {
            throw new RuntimeException("cache content can not be empty");
        }
        // 添加缓存
        caches.put(key, object);
    }

    // 静态方法模拟删除缓存
    public static void removeCache(String key) throws Exception {
        try {
            if (key == null || "".equals(key)) {
                throw new Exception("Method: removeCache, cache key can not be empty");
            }
            // 删除缓存
            caches.remove(key);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // 静态方法获取缓存
    public static Object getCache(String key) throws RuntimeException {
        if (key == null || "".equals(key)) {
            throw new RuntimeException("Method: getCache, cache key can not be empty");
        }
        // 获取缓存
        Object object = caches.get(key);
        return object;
    }
}