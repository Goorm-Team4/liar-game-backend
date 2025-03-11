package com.goorm.liargame.global.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public void setHashValue(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }
    
    // 특정 key의 hash에서 해당 필드의 값을 가져옵니다.
    public Object getHashValue(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    // 특정 key의 hash 전체 데이터를 Map으로 가져옵니다.
    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    // 특정 key의 hash에서 하나 이상의 필드를 삭제합니다.
    public void deleteHashValue(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    public void updateHashListValue(String key, String hashKey, Object newValue) {
    // Redis 해시에서 해당 필드의 값을 가져옴
    Object currentValue = redisTemplate.opsForHash().get(key, hashKey);
    List<Object> list;
    
    // 값이 이미 List 타입이면 캐스팅, 아니면 새 리스트 생성
    if (currentValue instanceof List) {
        list = (List<Object>) currentValue;
    } else {
        list = new ArrayList<>();
    }
    
    // 새 값을 리스트에 추가
    list.add(newValue);
    
    // 업데이트된 리스트를 다시 Redis 해시 필드에 저장
    redisTemplate.opsForHash().put(key, hashKey, list);
    }
    
    public void updateHashSetValue(String key, String hashKey, Object newValue) {
        // Redis 해시에서 해당 필드의 현재 값을 가져옵니다.
        Object currentValue = redisTemplate.opsForHash().get(key, hashKey);
        HashSet<Object> hashSet;
        
        // 현재 값이 HashSet 타입이면 캐스팅, 그렇지 않으면 새 HashSet을 생성합니다.
        if (currentValue instanceof HashSet) {
            hashSet = (HashSet<Object>) currentValue;
        } else {
            hashSet = new HashSet<>();
        }
        
        // 새 값을 HashSet에 추가합니다.
        hashSet.add(newValue);
        
        // 업데이트된 HashSet을 Redis 해시 필드에 저장합니다.
        redisTemplate.opsForHash().put(key, hashKey, hashSet);
    }

}
