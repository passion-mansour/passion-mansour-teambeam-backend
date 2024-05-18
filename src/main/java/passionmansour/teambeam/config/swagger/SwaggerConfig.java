package passionmansour.teambeam.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
    info = @Info(title = "TeamBeam API 명세서",
        description = "팀글벙글 서비스 API 명세서",
        version = "v1"
    )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi OpenApi() {
        // "/api/**" 경로 매칭 api 그룹화
        String[] path = {"/api/**"};

        return GroupedOpenApi.builder()
            .group("TeamBram API")
            .pathsToMatch(path)
            .build();
    }

}
