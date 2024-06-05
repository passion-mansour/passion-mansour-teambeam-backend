package passionmansour.teambeam;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.core.RedisTemplate;
import passionmansour.teambeam.service.redis.RedisService;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableJpaAuditing
public class TeambeamApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TeambeamApplication.class, args);
		System.out.println("hello world");

		context.registerShutdownHook();

		// 종료 후크 추가
		context.addApplicationListener(event -> {
			if (event instanceof org.springframework.context.event.ContextClosedEvent) {
				System.out.println("Shutting down...");
				try {
					RedisTemplate<String, Object> redisTemplate = context.getBean("redisTemplate", RedisTemplate.class);
					redisTemplate.getConnectionFactory().getConnection().flushDb();
					System.out.println("Redis 데이터가 초기화되었습니다.");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Redis 데이터 초기화 실패: " + e.getMessage());
				}
			}
		});
	}

	@Bean
	public CommandLineRunner run(ObjectMapper objectMapper) {
		return args -> {
			System.out.println(objectMapper.writeValueAsString(LocalDateTime.now()));
		};
	}
}
