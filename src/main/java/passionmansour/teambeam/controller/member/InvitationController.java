package passionmansour.teambeam.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import passionmansour.teambeam.model.dto.member.response.ErrorResponse;
import passionmansour.teambeam.model.dto.member.response.RegisterResponse;
import passionmansour.teambeam.model.dto.project.response.TokenAuthenticationResponse;
import passionmansour.teambeam.service.ProjectService;

@Controller
@RequiredArgsConstructor
@Tag(name = "Invitation Controller", description = "멤버 초대 수락 관련 API입니다.")
@Slf4j
public class InvitationController {

    private final ProjectService projectService;

    @Operation(summary = "멤버 초대 수락", description = "사용자가 초대 수락을 위해 링크를 클릭합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인증 토큰 검증 성공",
            content = @Content(schema = @Schema(implementation = RegisterResponse.class))),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버/프로젝트",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/accept-invitation")
    public String tokenAuthentication(@RequestParam("token") String token) {

        TokenAuthenticationResponse response = projectService.tokenAuthentication(token);

        log.info("response {}", response);
        
        if (response.isMember()) {
            return "redirect:https://k0bf03acb7c00a.user-app.krampoline.com/user/login";
        } else {
            return "redirect:https://k0bf03acb7c00a.user-app.krampoline.com/user/join?token=" + response.getToken();
        }
    }

}
