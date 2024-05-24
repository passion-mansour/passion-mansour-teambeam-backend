package passionmansour.teambeam.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import passionmansour.teambeam.model.dto.project.response.TokenAuthenticationResponse;
import passionmansour.teambeam.service.ProjectService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class InvitationController {

    private final ProjectService projectService;

    @GetMapping("/accept-invitation")
    public String tokenAuthentication(@RequestParam("token") String token) {

        TokenAuthenticationResponse response = projectService.tokenAuthentication(token);

        log.info("response {}", response);
        
        if (response.isMember()) {
            return "redirect:http://localhost:3000/login";
        } else {
            return "redirect:http://localhost:3000/signup?token=" + response.getToken();
        }
    }

}
