package passionmansour.teambeam.service.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void checkRedisConnection() {
        String response = redisTemplate.execute((RedisConnection connection) -> connection.ping());
        if ("PONG".equals(response)) {
            log.info("Redis connection is healthy");
        } else {
            log.error("Failed to connect to Redis");
        }
    }
}
