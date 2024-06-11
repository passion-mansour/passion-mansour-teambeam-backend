package passionmansour.teambeam.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.execption.member.TokenGenerationException;
import passionmansour.teambeam.execption.member.UserAlreadyExistsException;
import passionmansour.teambeam.model.dto.member.request.*;
import passionmansour.teambeam.model.dto.member.MemberDto;
import passionmansour.teambeam.model.dto.member.response.ProfileImageResponse;
import passionmansour.teambeam.model.entity.JoinMember;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.model.enums.StartPage;
import passionmansour.teambeam.repository.JoinMemberRepository;
import passionmansour.teambeam.repository.MemberRepository;
import passionmansour.teambeam.repository.ProjectRepository;
import passionmansour.teambeam.service.mail.EmailService;
import passionmansour.teambeam.service.security.JwtTokenService;
import passionmansour.teambeam.service.security.RedisTokenService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ProjectRepository projectRepository;
    private final JoinMemberRepository joinMemberRepository;
    private final RedisTokenService redisTokenService;

    @Transactional
    public MemberDto saveMember(RegisterRequest registerRequest) throws IOException {

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // 이미지 리스트 가져오기
        List<Path> images = getImagesFromClassPath("images");

        // 랜덤 이미지 선택
        Random random = new Random();
        Path randomImage = images.get(random.nextInt(images.size()));
        log.info("randomImage {}", randomImage);
        log.info("randomImage.getFileName {}", randomImage.getFileName());

        Optional<Member> existingMember = memberRepository.findByMail(registerRequest.getMail());

        Member member;
        if (existingMember.isPresent()) {
            member = existingMember.get();
            if (member.isDeleted()) {
                // 논리 삭제된 계정을 재사용하여 업데이트
                member.setDeleted(false);
                member.setMemberName(registerRequest.getMemberName());
                member.setPassword(encodedPassword);
                member.setNotificationCount(0); // 초기화
                member.setProfileImage(randomImage.getFileName().toString());
                member.setStartPage(StartPage.PROJECT_SELECTION_PAGE);
            } else {
                throw new UserAlreadyExistsException("User with this mail already exists: " + registerRequest.getMail());
            }
        } else {
            member = new Member();
            member.setMemberName(registerRequest.getMemberName());
            member.setMail(registerRequest.getMail());
            member.setPassword(encodedPassword);
            member.setNotificationCount(0); // 초기화
            member.setProfileImage(randomImage.getFileName().toString());
            member.setStartPage(StartPage.PROJECT_SELECTION_PAGE);
        }

        // 멤버 저장
        Member savedMember = memberRepository.save(member);
        log.info("Saved member {}", savedMember);

        // 프로젝트 참가 처리
        if (registerRequest.getToken() != null) {
            joinProjectWithToken(registerRequest.getToken(), savedMember);
        }

        return convertToDto(savedMember);
    }

    private void joinProjectWithToken(String token, Member member) {
        Long projectIdFromToken = tokenService.getProjectIdFromToken(token);
        Project project = projectRepository.findByProjectId(projectIdFromToken).orElseThrow(() ->
            new EntityNotFoundException("Project not found with projectId: " + projectIdFromToken));

        JoinMember joinMember = new JoinMember();
        joinMember.setProject(project);
        joinMember.setMember(member);
        joinMember.setHost(false);

        JoinMember savedJoinMember = joinMemberRepository.save(joinMember);
        log.info("JoinMember {}", savedJoinMember);
    }

    public MemberDto convertToDto(Member member) {

        MemberDto memberDto = null;
        memberDto = MemberDto.builder()
            .memberId(member.getMemberId())
            .memberName(member.getMemberName())
            .mail(member.getMail())
            .password(member.getPassword())
            .startPage(member.getStartPage())
            .profileImage(getImageAsBase64(member.getProfileImage()))
            .notificationCount(member.getNotificationCount())
            .build();

        return memberDto;
    }

    // 회원가입 메일 인증 요청
    @Transactional
    public String sendRegisterCode(String mail) {

        Optional<Member> member = memberRepository.findByMailAndIsDeletedFalse(mail);

        if (member.isPresent()) {
            throw new UserAlreadyExistsException("User with this mail already exists: " + mail);
        }

        String subject = "회원가입 메일 인증";
        String text = "회원가입 메일을 인증";

        return sendCode(mail, subject, text);
    }

    // 로그인
    public MemberDto login(LoginRequest loginRequest) {

        log.info("Authenticating user with mail: {}", loginRequest.getMail());

        try {
            // mail 로 사용자 정보 조회
            Member member = memberRepository.findByMailAndIsDeletedFalse(loginRequest.getMail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with mail: " + loginRequest.getMail()));

            log.info("Stored encoded password: {}", member.getPassword());

            // 입력된 비밀번호와 저장된 비밀번호 비교
            if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
                throw new BadCredentialsException("Invalid credentials provided");
            }

            // UserDetails 객체로 변환
            User user = new User(member.getMail(), member.getPassword(), new ArrayList<>());
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
        } catch (TokenGenerationException e) {
            throw new TokenGenerationException("Failed to generate token", e);
        }

    }

    @Transactional
    public String sendPasswordResetLink(UpdateMemberRequest request) {

        Optional<Member> memberOptional = memberRepository.findByMailAndIsDeletedFalse(request.getMail());
        log.info(memberOptional.toString());

        if (memberOptional.isPresent()) {
            String token = UUID.randomUUID().toString();

            // 인증 정보 저장
            redisTokenService.storeResetToken(token, request.getMail());

            log.info("token {}", token);

            // 재설정 링크 생성
            String resetLink = "https://k0bf03acb7c00a.user-app.krampoline.com/user/settingPassword?token=" + token;
            String emailBody = "<html><body><p>안녕하세요,</p><p>비밀번호를 재설정하려면 아래 링크를 클릭하세요:</p>" +
                "<a href='" + resetLink + "'>비밀번호 재설정</a><p>링크는 30분 후에 만료됩니다.</p></body></html>";

            // 메일 전송
            try {
                emailService.sendHtmlEmail(request.getMail(), "비밀번호 재설정", emailBody);
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

        String mail = null;

        mail = redisTokenService.getMailByResetToken(token);
        log.info("mail {}", mail);

        String finalMail = mail;
        Member member = memberRepository.findByMailAndIsDeletedFalse(mail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with mail: " + finalMail));
        log.info("member {}", member);

        member.setPassword(passwordEncoder.encode(newPassword));
        redisTokenService.deleteResetToken(token);

        log.info("savedMember {}", member);

        return true;
    }

    // 토큰으로 멤버 조회 -> 알림 수 업데이트
    @Transactional
    public MemberDto getMember(String token) {
        Member member = tokenService.getMemberByToken(token);
        int size = member.getNotifications().size();
        member.setNotificationCount(size);
        return convertToDto(member);
    }

    // 멤버 아이디를 통해 멤버 조회 (프로필 이미지 조회용)
    public MemberDto getMemberById(String token, Long memberId) {
        Member memberByToken = tokenService.getMemberByToken(token);

        Member member = memberRepository.findById(memberId)
            .orElseThrow(()-> new UsernameNotFoundException("User not found with memberId: " + memberId));

        return convertToDto(member);
    }

    // 이미지 인코딩
    public static String encodeImageToBase64(Path imagePath) throws IOException {
        byte[] imageBytes = Files.readAllBytes(imagePath);
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    // 특정 이미지 인코딩
    public String getImageAsBase64(String imageName) {
        try {
            ClassPathResource resource = new ClassPathResource("images/" + imageName);
            Path imagePath = Paths.get(resource.getURI());
            return encodeImageToBase64(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 이미지 리스트 반환
    public List<ProfileImageResponse> getProfileImages() throws IOException{
        return getImagesFromClassPath("images")
            .stream()
            .map(imagePath -> {
                try {
                    String fileName = imagePath.getFileName().toString();
                    String base64 = encodeImageToBase64(imagePath);
                    return new ProfileImageResponse(fileName, base64);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to encode image", e);
                }
            })
            .collect(Collectors.toList());
    }

    // 이미지 리소스 가져오기
    private List<Path> getImagesFromClassPath(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        return Files.walk(Paths.get(resource.getURI()))
            .filter(Files::isRegularFile)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMember(String token) {
        MemberDto member = getMember(token);
        memberRepository.deleteById(member.getMemberId());
    }

    @Transactional
    public void updatePassword(String token, UpdatePasswordRequest request) {
        Member member = tokenService.getMemberByToken(token);

        if (!passwordEncoder.matches(request.getOldPassword(), member.getPassword())) {
            throw new BadCredentialsException("Invalid credentials provided");
        }

        member.setPassword(passwordEncoder.encode(request.getNewPassword()));

        log.info("updateMember {}", member);
    }

    @Transactional
    public MemberDto updateMember(String token, UpdateMemberRequest request) {

        Member member = tokenService.getMemberByToken(token);

        // 프로필 이미지가 제공된 경우 업데이트
        if (request.getProfileImage() != null) {
            member.setProfileImage(request.getProfileImage());
        }

        // 시작 페이지가 제공된 경우 업데이트
        if (request.getStartPage() != null) {
            member.setStartPage(request.getStartPage());
        }

        // 이름이 제공된 경우 업데이트
        if (request.getMemberName() != null) {
            member.setMemberName(request.getMemberName());
        }

        // 메일이 제공된 경우 업데이트
        if (request.getMail() != null) {
            member.setMail(request.getMail());

            // UserDetails 객체로 변환
            User user = new User(request.getMail(), member.getPassword(), new ArrayList<>());
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
        } else {
            return convertToDto(member);
        }
    }

    // 메일 수정 코드 요청
    @Transactional
    public String sendUpdateMailCode(String token, String mail) {
        tokenService.getMemberByToken(token);
        Optional<Member> optionalMember = memberRepository.findByMailAndIsDeletedFalse(mail);

        if (optionalMember.isPresent()) {
            throw new UserAlreadyExistsException("Mail already exists");
        }

        String subject = "메일 주소 변경";
        String text = "메일 주소를 변경";

        return sendCode(mail, subject, text);
    }

    private String sendCode(String mail, String subject, String text) {
        // 100000 (최소값) 부터 999999 (최대값) 사이의 숫자 생성
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

        try {
            emailService.sendEmail(mail, subject, "안녕하세요,\n\n" + text + "하려면 아래 코드를 입력하세요:\n\n" + code + "\n\n김시합니다.");
        } catch (MailAuthenticationException e) {
            log.error("Mail authentication failed: {}", e.getMessage());
            throw new MailAuthenticationException("Authentication failed");
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }

        return code;
    }
}