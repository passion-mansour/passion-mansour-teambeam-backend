package passionmansour.teambeam.controller.member;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import passionmansour.teambeam.model.dto.member.response.KakaoUserInfoResponse;
import passionmansour.teambeam.service.kakao.KakaoAPI;
import passionmansour.teambeam.service.kakao.KakaoService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "KAKAO Controller", description = "카카오 관련 API입니다.")
@Slf4j
public class KakaoController {

    private final KakaoAPI kakaoAPI;
    private final KakaoService kakaoService;

    @GetMapping("/kakao/login")
    public ResponseEntity<?> login(@RequestParam("code") String code) {
        String accessToken = kakaoAPI.getAccessToken(code);

        KakaoUserInfoResponse userInfo = kakaoAPI.getUserInfo(accessToken);

        return kakaoService.kakaoLogin(userInfo.getKakaoAccount().getEmail(), userInfo.getKakaoAccount().getProfile().getNickName());

    }
}
