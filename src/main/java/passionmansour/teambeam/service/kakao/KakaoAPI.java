package passionmansour.teambeam.service.kakao;

import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import passionmansour.teambeam.model.dto.member.KakaoDto;
import passionmansour.teambeam.model.dto.member.response.KakaoUserInfoResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAPI {

    // 요청 파라미터 설정
    @Value("${kakao.client-id}")
    String clientId;
    @Value("${kakao.redirect-uri}")
    String redirectUri;

    public String getAccessToken(String code) {

        String requestUrl = "https://kauth.kakao.com/oauth/token" + "?grant_type=authorization_code" +
            "&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&code=" + code;

        // POST 요청을 통해 엑세스 토큰 획득
        KakaoDto kakaoDto = WebClient.create().post()
            .uri(requestUrl)
            .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ClientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
            .onStatus(HttpStatusCode::is5xxServerError, ClientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
            .bodyToMono(KakaoDto.class)
            .block();

        log.info("[ kakao Service ] Access Token {}", kakaoDto.getAccessToken());
        log.info("[ kakao Service ] Refresh Token {}", kakaoDto.getRefreshToken());
        log.info("[ kakao Service ] Id Token {}", kakaoDto.getIdToken());
        log.info("[ kakao Service ] Scope {}", kakaoDto.getScope());

        return kakaoDto.getAccessToken();
    }

    public KakaoUserInfoResponse getUserInfo(String accessToken) {

        String requestUrl = "https://kapi.kakao.com/v2/user/me";

        KakaoUserInfoResponse userInfo = WebClient.create()
            .get()
            .uri(requestUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ClientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
            .onStatus(HttpStatusCode::is5xxServerError, ClientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
            .bodyToMono(KakaoUserInfoResponse.class)
            .block();

        log.info("[ kakao Service ] Auth ID {}", userInfo.getId());
        log.info("[ kakao Service ] NickName {}", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("[ kakao Service ] Mail {}", userInfo.getKakaoAccount().getEmail());

        return userInfo;
    }

}
