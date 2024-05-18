package passionmansour.teambeam.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.execption.member.TokenGenerationException;
import passionmansour.teambeam.execption.member.UserAlreadyExistsException;
import passionmansour.teambeam.model.dto.member.LoginDto;
import passionmansour.teambeam.model.dto.member.MemberDto;
import passionmansour.teambeam.model.dto.member.RegisterDto;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.enums.StartPage;
import passionmansour.teambeam.repository.MemberRepository;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberDto saveMember(RegisterDto registerDto) {

        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        log.info("Encoded password: {}", encodedPassword);

        Member member = Member.builder()
            .memberName(registerDto.getMemberName())
            .mail(registerDto.getMail())
            .password(encodedPassword)
            .notificationCount(0)
            .startPage(StartPage.PROJECT_SELECTION_PAGE)
            .build();

        if (registerDto.getToken() != null) {
            //TODO: 초대된 프로젝트에 추가
        }

        if (memberRepository.findByMail(registerDto.getMail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this mail already exists: " + registerDto.getMail());
        }

        try {

            Member savedMember = memberRepository.save(member);
            MemberDto res = convertToDto(savedMember);
            return res;

        } catch (EntityExistsException e) {
            throw new UserAlreadyExistsException("User with this mail already exists: " + registerDto.getMail());
        } catch (PersistenceException e) {
            throw new RuntimeException("Persistence exception: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    public MemberDto convertToDto(Member member) {

        MemberDto memberDto = MemberDto.builder()
            .memberId(member.getMemberId())
            .memberName(member.getMemberName())
            .mail(member.getMail())
            .password(member.getPassword())
            .startPage(member.getStartPage())
            .notificationCount(member.getNotificationCount())
            .build();

        log.info(memberDto.toString());
        return memberDto;
    }

    // 로그인
    public MemberDto login(LoginDto loginDto) {

        log.info("Authenticating user with mail: {}", loginDto.getMail());

        try {
            // mail 로 사용자 정보 조회
            Member member = memberRepository.findByMail(loginDto.getMail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with mail: " + loginDto.getMail()));

            log.info("Stored encoded password: {}", member.getPassword());

            // 입력된 비밀번호와 저장된 비밀번호 비교
            if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
                throw new BadCredentialsException("Invalid credentials provided");
            }

            // UserDetails 객체로 변환
            User user = new User(member.getMemberName(), member.getPassword(), new ArrayList<>());
            log.info("user {}", user.toString());

            // 토큰 생성
            final String accessToken = tokenService.generateAccessToken(user);
            final String refreshToken = tokenService.generateRefreshToken(user);

            log.info("accessToken: {}", accessToken);
            log.info("refreshToken: {}", refreshToken);

            MemberDto savedMember = convertToDto(member);
            savedMember.setAccessToken(accessToken);
            savedMember.setRefreshToken(refreshToken);

            return savedMember;
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials provided", e);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found with mail: " + loginDto.getMail(), e);
        } catch (Exception e) {
            throw new TokenGenerationException("Failed to generate token", e);
        }

    }

    @Transactional
    public String deleteMember(Long id) {

        memberRepository.deleteById(id);

        return "OK";
    }
}