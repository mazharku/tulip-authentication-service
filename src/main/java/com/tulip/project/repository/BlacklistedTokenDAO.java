package com.tulip.project.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Objects;


@Repository
public class BlacklistedTokenDAO {
    private final StringRedisTemplate stringRedisTemplate;

    public BlacklistedTokenDAO(@Qualifier("stringRedisTemplate") StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setTokenBlacklisted(String token, Date expireAt) {
        stringRedisTemplate.opsForValue().set(token, String.valueOf(new Date()));
        stringRedisTemplate.expireAt(token, expireAt);
    }

    public boolean isTokenBlacklisted(String token) {
        Boolean hasKey = stringRedisTemplate.hasKey(token);
        return Objects.requireNonNullElse(hasKey, false);
    }

}
