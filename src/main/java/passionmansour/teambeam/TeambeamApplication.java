package passionmansour.teambeam;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import passionmansour.teambeam.service.redis.RedisService;

@SpringBootApplication
@EnableJpaAuditing
public class TeambeamApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeambeamApplication.class, args);
		System.out.println("hello world");
	}

	@Bean
	public CommandLineRunner run(RedisService redisService) {
		return args -> {
			redisService.checkRedisConnection();
		};
	}
}
