package passionmansour.teambeam.config.redis;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class RedisShutdown {

    private final RedisTemplate<String, Object> redisTemplate;


    @Autowired
    public RedisShutdown(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PreDestroy
    public void clearRedisData() {
        // 모든 키 삭제
        redisTemplate.getConnectionFactory().getConnection().flushDb();
        System.out.println("Redis 데이터가 초기화되었습니다.");
    }
}