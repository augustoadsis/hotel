package com.augusto.hotel.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CacheService {

    public static final String AVAILABILITIES = "AVAILABILITIES";
    public static final String REDIS_DOWN = "Redis down";
    @Autowired
    private RedisTemplate<String,?> redisTemplate;

    public Mono<List<LocalDate>> getCache(String key) {
        HashOperations operations = redisTemplate.opsForHash();
        try {
            if (operations.hasKey(AVAILABILITIES, key)) {
                List<LocalDate> dates = (List<LocalDate>) operations.get(AVAILABILITIES, key);
                return Mono.justOrEmpty(dates);
            } else
                return null;
        } catch (Exception e) {
            log.error(REDIS_DOWN);
        }
        return null;
    }

    public void putCache(String key, List<LocalDate> dates) {
        try {
            redisTemplate.opsForHash().put(AVAILABILITIES, key, dates.stream().filter(d -> d.isAfter(LocalDate.now())).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error(REDIS_DOWN);
        }
    }

    public void evictCache() {
        try {
            redisTemplate.delete(AVAILABILITIES);
        } catch (Exception e) {
            log.error(REDIS_DOWN);
        }
    }
}
