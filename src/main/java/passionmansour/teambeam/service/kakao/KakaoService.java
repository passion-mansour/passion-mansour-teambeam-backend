package passionmansour.teambeam.service.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.member.MemberDto;
import passionmansour.teambeam.model.dto.member.request.LoginRequest;
import passionmansour.teambeam.model.dto.member.request.RegisterRequest;
import passionmansour.teambeam.model.dto.member.response.LoginResponse;
import passionmansour.teambeam.model.dto.member.response.RegisterResponse;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.repository.MemberRepository;
import passionmansour.teambeam.service.MemberService;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JwtTokenService tokenService;

    @Transactional
    public ResponseEntity<?> kakaoLogin(String mail, String nickname) {
        // 카카오 메일 회원 여부 확인
        Optional<Member> optionalMember = memberRepository.findByMailAndIsDeletedFalse(mail);

        // 회원
        if (optionalMember.isPresent()) {
            LoginRequest request = new LoginRequest();
            request.setMail(mail);
            request.setPassword("kakao");

            MemberDto member = memberService.login(request);

            LoginResponse response = new LoginResponse("Login successful", member.getMemberId());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", member.getAccessToken());
            headers.add("RefreshToken", member.getRefreshToken());

            return ResponseEntity.ok().headers(headers).body(response);

        }

        RegisterRequest request = new RegisterRequest();

        if (nickname == null) {
            String baseNickname = mail.split("@")[0];
            request.setMemberName(baseNickname);
        } else {
            request.setMemberName(nickname);
        }
        request.setMail(mail);
        request.setPassword("kakao");

        MemberDto member = memberService.saveMember(request);

        // UserDetails 객체로 변환
        User user = new User(member.getMail(), member.getPassword(), new ArrayList<>());
        log.info("user {}", user);

        // 토큰 생성
        final String accessToken = tokenService.generateAccessToken(user);
        final String refreshToken = tokenService.generateRefreshToken(user);

        log.info("accessToken: {}", accessToken);
        log.info("refreshToken: {}", refreshToken);

        member.setAccessToken(accessToken);
        member.setRefreshToken(refreshToken);

        RegisterResponse response = new RegisterResponse("Registration successful", member.getMemberId(), member.getMemberName());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", member.getAccessToken());
        headers.add("RefreshToken", member.getRefreshToken());

        return ResponseEntity.ok().headers(headers).body(response);

    }

}
