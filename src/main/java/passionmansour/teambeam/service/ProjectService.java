package passionmansour.teambeam.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.project.ProjectDto;
import passionmansour.teambeam.model.dto.project.ProjectJoinMemberDto;
import passionmansour.teambeam.model.dto.project.request.LinkRequest;
import passionmansour.teambeam.model.dto.project.request.MasterRequest;
import passionmansour.teambeam.model.dto.project.request.UpdateProjectRequest;
import passionmansour.teambeam.model.dto.project.request.UpdateRoleRequest;
import passionmansour.teambeam.model.dto.project.response.TokenAuthenticationResponse;
import passionmansour.teambeam.model.entity.*;
import passionmansour.teambeam.model.enums.ProjectStatus;
import passionmansour.teambeam.repository.*;
import passionmansour.teambeam.service.mail.EmailService;
import passionmansour.teambeam.service.security.JwtTokenService;
import passionmansour.teambeam.service.security.RedisTokenService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenService tokenService;
    private final JoinMemberRepository joinMemberRepository;
    private final TagRepository tagRepository;
    private final EmailService emailService;
    private final RedisTokenService redisTokenService;

    @Transactional
    public ProjectDto createProject(String token, ProjectDto projectDto) {

        Member member = tokenService.getMemberByToken(token);

        // 프로젝트 생성
        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setDescription(projectDto.getDescription());
        project.setProjectStatus(ProjectStatus.PROGRESS);
        project.setCreateDate(LocalDateTime.now());

        //캘린더 생성 알고리즘
        Calendar calendar = new Calendar();
        project.setCalendar(calendar);

        //기본 투두리스트 생성


        Project savedProject = projectRepository.save(project);

        // 참여 회원 생성
        JoinMember joinMember = JoinMember.builder()
            .member(member)
            .isHost(true)
            .project(savedProject)
            .build();

        JoinMember saved = joinMemberRepository.save(joinMember);
        log.info(saved.toString());

        ProjectDto converted = convertToDto(project);

        log.info(converted.toString());
        return converted;
    }



    public ProjectDto convertToDto(Project project) {

        return ProjectDto.builder()
            .projectId(project.getProjectId())
            .projectName(project.getProjectName())
            .projectStatus(project.getProjectStatus())
            .description(project.getDescription())
            .createDate(project.getCreateDate())
            .build();
    }

    public List<ProjectDto> getProjectList(String token) {

        Member member = tokenService.getMemberByToken(token);

        // 멤버 정보를 통해 참여 정보 조회
        List<JoinMember> joinMembers = joinMemberRepository.findByMember(member);

        // 멤버의 참여 프로젝트 조회
        List<Project> projects = joinMembers.stream()
            .map(JoinMember::getProject)
            .toList();

        return projects.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    public ProjectDto getProject(Long id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + id));

        List<Tag> tagList = tagRepository.findByProject_ProjectId(id);

        return convertToDto(project);
    }

    @Transactional
    public ProjectDto updateProject(Long id, UpdateProjectRequest request) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + id));

        if (request.getProjectName() != null) {
            project.setProjectName(request.getProjectName());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getProjectStatus() != null) {
            project.setProjectStatus(request.getProjectStatus());
        }

        return convertToDto(project);
    }

    public List<ProjectJoinMemberDto> getProjectJoinMembers(Long id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + id));

        List<JoinMember> joinMembers = project.getJoinMembers();

        return joinMembers.stream().map(this::convertToDto).toList();
    }

    private ProjectJoinMemberDto convertToDto(JoinMember joinMember) {
        ProjectJoinMemberDto dto = new ProjectJoinMemberDto();
        dto.setMemberId(joinMember.getMember().getMemberId());
        dto.setMemberName(joinMember.getMember().getMemberName());
        dto.setMail(joinMember.getMember().getMail());
        dto.setMemberRole(joinMember.getMemberRole() != null ? joinMember.getMemberRole().toString() : null);
        dto.setHost(joinMember.isHost());
        return dto;
    }

    @Transactional
    public List<ProjectJoinMemberDto> updateMemberRole(Long id, UpdateRoleRequest request) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + id));

        for (UpdateRoleRequest.MemberRoles memberRoles : request.getMembers()) {
            JoinMember joinMember = joinMemberRepository.findByMember_MemberIdAndProject_ProjectId(memberRoles.getMemberId(), id);

            if (joinMember == null) {
                throw new UsernameNotFoundException("User not found with memberId: " + memberRoles.getMemberId());
            }
            joinMember.setMemberRole(memberRoles.getMemberRole());
            log.info("joinMember {}", joinMember);
        }

        List<JoinMember> joinMembers = project.getJoinMembers();

        return joinMembers.stream().map(this::convertToDto).toList();
    }

    @Transactional
    public List<ProjectJoinMemberDto> updateMaster(String token, Long projectId, Long memberId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + projectId));

        Member memberByToken = tokenService.getMemberByToken(token);
        JoinMember member = joinMemberRepository.findByMember_MemberIdAndProject_ProjectId(memberByToken.getMemberId(),
            projectId);
        member.setHost(false);

        JoinMember target = joinMemberRepository.findByMember_MemberIdAndProject_ProjectId(memberId, projectId);
        target.setHost(true);

        log.info("Previous Master {}", member);
        log.info("New master {}", target);

        List<JoinMember> joinMembers = project.getJoinMembers();

        return joinMembers.stream().map(this::convertToDto).toList();
    }

    @Transactional
    public void deleteJoinMember(Long projectId, MasterRequest request) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + projectId));

        JoinMember joinMember = joinMemberRepository.findByMember_MemberIdAndProject_ProjectId(request.getMemberId(),
            projectId);
        joinMemberRepository.delete(joinMember);
    }

    @Transactional
    public String sendLink(Long id, LinkRequest request) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + id));

        // 인증 정보 생성, 저장
        String token = tokenService.generateInvitationToken(request.getMail(), id);

        redisTokenService.storeInvitationToken(request.getMail(), token);

        log.info("token {}", token);

        // 초대 링크 생성
        String resetLink = "http://34.22.108.250:8080/accept-invitation?token=" + token;
        // 메일 전송
        try {
            emailService.sendEmail(request.getMail(), "프로젝트 초대",
                "안녕하세요,\n\n프로젝트에 참가하려면 아래 링크를 클릭하세요:\n\n" + resetLink
                    + "\n\n링크는 24시간 후에 만료됩니다.\n\n" + "\n\n김시합니다.");
            return resetLink;
        } catch (MailAuthenticationException e) {
            log.error("Mail authentication failed: {}", e.getMessage());
            throw new MailAuthenticationException("Authentication failed");
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }


    @Transactional
    public TokenAuthenticationResponse tokenAuthentication(String token) {

        // 토큰에서 메일 추출
        String mail = redisTokenService.getMailByToken(token);
        log.info("redisTokenService.getMailByToken(token) {}", mail);

        // 메일로 멤버 조회
        Optional<Member> member = memberRepository.findByMail(mail);
        log.info("member {}", member);

        // 회원
        if (member.isPresent()) {
            // 토큰에서 프로젝트 아이디 추출
            Long projectIdFromToken = tokenService.getProjectIdFromToken(token);

            // 해당 프로젝트 조회
            Project project = projectRepository.findByProjectId(projectIdFromToken)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + projectIdFromToken));

            JoinMember joinMember = new JoinMember();
            joinMember.setProject(project);
            joinMember.setMember(member.get());
            joinMember.setHost(false);

            JoinMember savedJoinMember = joinMemberRepository.save(joinMember);

            log.info("joinMember {}", savedJoinMember);

            TokenAuthenticationResponse response = new TokenAuthenticationResponse();
            response.setMessage("Successful Project Participation");
            response.setMember(true);

            redisTokenService.deleteInvitationToken(token);

            return response;
        }
        // 비회원
        TokenAuthenticationResponse response = new TokenAuthenticationResponse();
        response.setMessage("Membership registration required");
        response.setMember(false);
        response.setToken(token);

        return response;

    }
}
