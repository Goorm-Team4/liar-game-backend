package com.goorm.liargame.global.common.utils;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setValue(String key, Object val, Long time) {
        redisTemplate.opsForValue().set(key, val, time, TimeUnit.MILLISECONDS);
    }

    public void setPermanentValue(String key, Object val) {
        redisTemplate.opsForValue().set(key, val);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    public Long getRemainingTTL(String accessToken) {
        return redisTemplate.getExpire(accessToken, TimeUnit.MILLISECONDS);
    }

}
