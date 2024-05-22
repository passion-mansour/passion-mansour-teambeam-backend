package passionmansour.teambeam.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.execption.member.InvalidTokenException;
import passionmansour.teambeam.execption.member.TokenGenerationException;
import passionmansour.teambeam.execption.member.UserAlreadyExistsException;
import passionmansour.teambeam.model.dto.member.request.*;
import passionmansour.teambeam.model.dto.member.MemberDto;
import passionmansour.teambeam.model.entity.JoinMember;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.model.entity.Verification;
import passionmansour.teambeam.model.enums.StartPage;
import passionmansour.teambeam.repository.JoinMemberRepository;
import passionmansour.teambeam.repository.MemberRepository;
import passionmansour.teambeam.repository.ProjectRepository;
import passionmansour.teambeam.repository.VerificationRepository;
import passionmansour.teambeam.service.mail.EmailService;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationRepository verificationRepository;
    private final EmailService emailService;
    private final ProjectRepository projectRepository;
    private final JoinMemberRepository joinMemberRepository;

    @Transactional
    public MemberDto saveMember(RegisterRequest registerRequest) {

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        log.info("Encoded password: {}", encodedPassword);

        Member member = new Member();
        member.setMemberName(registerRequest.getMemberName());
        member.setMail(registerRequest.getMail());
        member.setPassword(encodedPassword);
        member.setNotificationCount(member.getNotifications().size());
        member.setStartPage(StartPage.PROJECT_SELECTION_PAGE);



        if (memberRepository.findByMail(registerRequest.getMail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this mail already exists: " + registerRequest.getMail());
        }

        Member savedMember = memberRepository.save(member);
        log.info("member {}", savedMember);

        if (registerRequest.getToken() != null) {
            //TODO: 초대된 프로젝트에 추가
            Long projectIdFromToken = tokenService.getProjectIdFromToken(registerRequest.getToken());
            Project project = projectRepository.findByProjectId(projectIdFromToken).orElseThrow(() ->
                new EntityNotFoundException("Project not found with projectId: " + projectIdFromToken));

            JoinMember joinMember = new JoinMember();
            joinMember.setProject(project);
            joinMember.setMember(savedMember);
            joinMember.setHost(false);

            JoinMember saved = joinMemberRepository.save(joinMember);
            log.info("joinMember {}", saved.toString());
        }

        return convertToDto(savedMember);

    }

    public MemberDto convertToDto(Member member) {

        MemberDto memberDto = MemberDto.builder()
            .memberId(member.getMemberId())
            .memberName(member.getMemberName())
            .mail(member.getMail())
            .password(member.getPassword())
            .startPage(member.getStartPage())
            .profileImage(member.getProfileImage())
            .notificationCount(member.getNotificationCount())
            .build();

        log.info(memberDto.toString());
        return memberDto;
    }

    // 회원가입 메일 인증 요청
    @Transactional
    public void sendRegisterCode(String mail) {

        Optional<Member> member = memberRepository.findByMail(mail);

        if (member.isPresent()) {
            throw new UserAlreadyExistsException("User with this mail already exists: " + mail);
        }

        String subject = "회원가입 메일 인증";
        String text = "회원가입 메일을 인증";

        sendCode(mail, null, subject, text);
    }

    // 회원가입 메일 코드 인증
    @Transactional
    public boolean registerCode(String code) {

        Optional<Verification> verificationOptional = verificationRepository.findByCode(code);
        log.info("verification {}", verificationOptional);

        // 존재하는 코드인지 확인
        if (!verificationOptional.isPresent()) {
            throw new InvalidTokenException("Invalid code");
        }

        Verification verification = verificationOptional.get();

        // 코드 만료 확인
        if (verification.getExpiredDate().isBefore(LocalDateTime.now())) {
            verificationRepository.delete(verification);
            throw new InvalidTokenException("Code has expired");
        }

        verificationRepository.delete(verification);
        return true;

    }

    // 로그인
    public MemberDto login(LoginRequest loginRequest) {

        log.info("Authenticating user with mail: {}", loginRequest.getMail());

        try {
            // mail 로 사용자 정보 조회
            Member member = memberRepository.findByMail(loginRequest.getMail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with mail: " + loginRequest.getMail()));

            log.info("Stored encoded password: {}", member.getPassword());

            // 입력된 비밀번호와 저장된 비밀번호 비교
            if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
                throw new BadCredentialsException("Invalid credentials provided");
            }

            // UserDetails 객체로 변환
            User user = new User(member.getMemberName(), member.getPassword(), new ArrayList<>());
            log.info("user {}", user);

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
            throw new UsernameNotFoundException("User not found with mail: " + loginRequest.getMail(), e);
        } catch (Exception e) {
            throw new TokenGenerationException("Failed to generate token", e);
        }

    }

    @Transactional
    public String sendPasswordResetLink(UpdateMemberRequest request) {

        Optional<Member> memberOptional = memberRepository.findByMail(request.getMail());
        log.info(memberOptional.toString());

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String token = UUID.randomUUID().toString();

            // 인증 정보 생성
            Verification verification = new Verification();
            verification.setToken(token);
            verification.setMember(member);
            verification.setExpiredDate(LocalDateTime.now().plusMinutes(30));

            log.info("Token: {}", verification.getToken());
            log.info("Expired Date: {}", verification.getExpiredDate());

            verificationRepository.save(verification);

            // 재설정 링크 생성
            String resetLink = "http://localhost:3000/reset-password?token=" + token;
            // 메일 전송
            try {
                emailService.sendEmail(request.getMail(), "비밀번호 재설정", "안녕하세요,\n\n비밀번호를 재설정하려면 아래 링크를 클릭하세요:\n\n" + resetLink + "\n\n김시합니다.");
                return resetLink;
            } catch (MailAuthenticationException e) {
                log.error("Mail authentication failed: {}", e.getMessage());
                throw new MailAuthenticationException("Authentication failed");
            } catch (Exception e) {
                log.error("Failed to send email: {}", e.getMessage());
                throw new RuntimeException("Failed to send email", e);
            }
        } else {
            throw new UsernameNotFoundException("User not found with mail: " + request);
        }
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<Verification> verificationOptional = verificationRepository.findByToken(token);

        if (verificationOptional.isPresent()) {
            Verification verification = verificationOptional.get();

            if (verification.getExpiredDate().isAfter(LocalDateTime.now())) {
                Member member = verification.getMember();
                member.setPassword(passwordEncoder.encode(newPassword));
                verificationRepository.delete(verification);

                return true;
            } else {
                throw new InvalidTokenException("Token has expired");
            }
        } else {
            throw new InvalidTokenException("Invalid token");
        }
    }

    public MemberDto getMember(String token) {
        Member member = getMemberByToken(token);

        return convertToDto(member);
    }

    private Member getMemberByToken(String token) {
        // 토큰에서 회원 이름 추출
        String usernameFromToken = tokenService.getUsernameFromToken(token);

        // 해당 회원 정보 조회
        return memberRepository.findByMemberName(usernameFromToken)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with memberName: " + usernameFromToken));

    }

    @Transactional
    public void deleteMember(String token) {
        MemberDto member = getMember(token);
        memberRepository.deleteById(member.getMemberId());
    }

    @Transactional
    public void updatePassword(String token, UpdatePasswordRequest request) {
        Member member = getMemberByToken(token);

        if (!passwordEncoder.matches(request.getOldPassword(), member.getPassword())) {
            throw new BadCredentialsException("Invalid credentials provided");
        }

        member.setPassword(passwordEncoder.encode(request.getNewPassword()));

        log.info("updateMember {}", member);
    }

    @Transactional
    public MemberDto updateMember(String token, UpdateMemberRequest request) {

        Member member = getMemberByToken(token);

        // 프로필 이미지가 제공된 경우 업데이트
        if (request.getProfileImage() != null) {
            member.setProfileImage(request.getProfileImage());
        }

        // 시작 페이지가 제공된 경우 업데이트
        if (request.getStartPage() != null) {
            member.setStartPage(request.getStartPage());
        }

        // 메일이 제공된 경우 업데이트
        if (request.getMail() != null) {
            member.setMail(request.getMail());
        }

        return convertToDto(member);
    }

    // 메일 수정 코드 요청
    @Transactional
    public void sendUpdateMailCode(String token, String mail) {
        Optional<Member> optionalMember = memberRepository.findByMail(mail);

        if (optionalMember.isPresent()) {
            throw new UserAlreadyExistsException("Mail already exists");
        }

        Member member = getMemberByToken(token);

        String subject = "메일 변경";
        String text = "메일 주소를 변경";

        // 100000 (최소값) 부터 999999 (최대값) 사이의 숫자 생성
        sendCode(mail, member, subject, text);

    }

    private void sendCode(String mail, Member member, String subject, String text) {
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

        Verification verification = new Verification();
        verification.setMember(member);
        verification.setCode(code);
        verification.setExpiredDate(LocalDateTime.now().plusMinutes(30));

        verificationRepository.save(verification);

        try {
            emailService.sendEmail(mail, subject, "안녕하세요,\n\n" + text + "하려면 아래 코드를 입력하세요:\n\n" + code + "\n\n김시합니다.");
        } catch (MailAuthenticationException e) {
            log.error("Mail authentication failed: {}", e.getMessage());
            throw new MailAuthenticationException("Authentication failed");
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    // 메일 수정 코드 인증
    public boolean codeAuthentication(String token, UpdateMemberRequest request) {

        Optional<Verification> verificationOptional = verificationRepository.findByCode(request.getCode());
        log.info("verification {}", verificationOptional);

        // 존재하는 코드인지 확인
        if (!verificationOptional.isPresent()) {
            throw new InvalidTokenException("Invalid code");
        }

        Verification verification = verificationOptional.get();

        // 코드 만료 확인
        if (verification.getExpiredDate().isBefore(LocalDateTime.now())) {
            verificationRepository.delete(verification);
            throw new InvalidTokenException("Code has expired");
        }

        MemberDto member = getMember(token);
        // 인증 요청 회원과 인증 회원이 같은지 확인
        if (!member.getMemberId().equals(verification.getMember().getMemberId())) {
            throw new BadCredentialsException("Requested member does not match the member linked with the verification code.");
        }

        verificationRepository.delete(verification);
        return true;
    }
}