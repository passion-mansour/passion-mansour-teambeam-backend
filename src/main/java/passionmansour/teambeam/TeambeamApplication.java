package passionmansour.teambeam;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import passionmansour.teambeam.service.redis.RedisService;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableJpaAuditing
public class TeambeamApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeambeamApplication.class, args);
		System.out.println("hello world");
	}

	@Bean
	public CommandLineRunner run(RedisService redisService, ObjectMapper objectMapper) {
		return args -> {
			redisService.checkRedisConnection();
			System.out.println(objectMapper.writeValueAsString(LocalDateTime.now()));
		};
	}
}
