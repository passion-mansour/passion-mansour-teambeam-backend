package passionmansour.teambeam.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.board.request.PostBoardRequest;
import passionmansour.teambeam.model.dto.board.response.BoardResponse;
import passionmansour.teambeam.model.dto.project.ProjectDto;
import passionmansour.teambeam.model.dto.project.ProjectJoinMemberDto;
import passionmansour.teambeam.model.dto.project.request.LinkRequest;
import passionmansour.teambeam.model.dto.project.request.MasterRequest;
import passionmansour.teambeam.model.dto.project.request.UpdateProjectRequest;
import passionmansour.teambeam.model.dto.project.request.UpdateRoleRequest;
import passionmansour.teambeam.model.dto.project.response.ProjectResponse;
import passionmansour.teambeam.model.dto.project.response.TokenAuthenticationResponse;
import passionmansour.teambeam.model.entity.*;
import passionmansour.teambeam.model.enums.ProjectStatus;
import passionmansour.teambeam.repository.*;
import passionmansour.teambeam.service.board.BoardService;
import passionmansour.teambeam.service.mail.EmailService;
import passionmansour.teambeam.service.security.JwtTokenService;
import passionmansour.teambeam.service.security.RedisTokenService;
import passionmansour.teambeam.service.todolist.TodolistService;

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
    private final EmailService emailService;
    private final RedisTokenService redisTokenService;
    private final BoardService boardService;

    private final CalendarRepository calendarRepository;

    private final TodolistService todolistService;

    @Transactional
    public ProjectResponse createProject(String token, ProjectDto projectDto) {

        Member member = tokenService.getMemberByToken(token);

        // 프로젝트 생성
        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setDescription(projectDto.getDescription());
        project.setProjectStatus(ProjectStatus.PROGRESS);
        project.setCreateDate(LocalDateTime.now());
        Project savedProject = projectRepository.save(project);

        //캘린더 생성 알고리즘
        Calendar calendar = new Calendar();
        calendar.setProject(savedProject);
        Calendar savedCalendar = calendarRepository.save(calendar);

        // 프로젝트에 캘린더 설정
        savedProject.setCalendar(savedCalendar);
        savedProject = projectRepository.save(savedProject);


        //기본 투두리스트 생성
        todolistService.createSampleTodolist(savedProject);


        // 게시판 요청 Dto 생성
        PostBoardRequest postBoardRequest = new PostBoardRequest();
        postBoardRequest.setProjectId(savedProject.getProjectId());
        postBoardRequest.setName("게시판");

        // 게시판 생성
        BoardResponse board = boardService.createBoard(postBoardRequest);

        // 참여 회원 생성
        JoinMember joinMember = new JoinMember();
        joinMember.setMember(member);
        joinMember.setHost(true);
        joinMember.setProject(savedProject);

        JoinMember saved = joinMemberRepository.save(joinMember);
        log.info(saved.toString());

        ProjectDto converted = convertToDto(project);

        ProjectResponse response = new ProjectResponse();

        response.setMessage("Project creation successful");
        response.setProject(converted);
        response.setProjectList(null);
        response.setBoardId(board.getBoardId());

        log.info(converted.toString());
        return response;
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

    public ProjectDto getProject(String token, Long projectId) {

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + projectId));

        boolean verified = verifyPermissions(token, project);
        log.info("verified {}", verified);

        if (!verified) {
            throw new BadCredentialsException("Member is not join of the project.");
        }

        return convertToDto(project);
    }

    public boolean verifyPermissions(String token, Project project) {

        Member member = tokenService.getMemberByToken(token);

        return joinMemberRepository.existsByMember_MemberIdAndProject_ProjectId(member.getMemberId(), project.getProjectId());
    }

    @Transactional
    public ProjectDto updateProject(String token, Long projectId, UpdateProjectRequest request) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + projectId));

        boolean verified = verifyPermissions(token, project);
        log.info("verified {}", verified);

        if (!verified) {
            throw new BadCredentialsException("Member is not join of the project.");
        }

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

    public List<ProjectJoinMemberDto> getProjectJoinMembers(String token, Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + projectId));

        boolean verified = verifyPermissions(token, project);
        log.info("verified {}", verified);

        if (!verified) {
            throw new BadCredentialsException("Member is not join of the project.");
        }

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
    public List<ProjectJoinMemberDto> updateMemberRole(String token, Long id, UpdateRoleRequest request) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + id));

        boolean verified = verifyPermissions(token, project);
        log.info("verified {}", verified);

        if (!verified) {
            throw new BadCredentialsException("Member is not join of the project.");
        }

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

        boolean verified = verifyPermissions(token, project);
        log.info("verified {}", verified);

        if (!verified) {
            throw new BadCredentialsException("Member is not join of the project.");
        }

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
    public void deleteJoinMember(String token, Long projectId, MasterRequest request) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + projectId));

        boolean verified = verifyPermissions(token, project);
        log.info("verified {}", verified);

        if (!verified) {
            throw new BadCredentialsException("Member is not join of the project.");
        }

        JoinMember joinMember = joinMemberRepository.findByMember_MemberIdAndProject_ProjectId(request.getMemberId(),
            projectId);
        joinMemberRepository.delete(joinMember);
    }

    @Transactional
    public String sendLink(String token, Long id, LinkRequest request) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + id));

        boolean verified = verifyPermissions(token, project);
        log.info("verified {}", verified);

        if (!verified) {
            throw new BadCredentialsException("Member is not join of the project.");
        }

        // 인증 정보 생성, 저장
        String linkToken = tokenService.generateInvitationToken(request.getMail(), id);

        redisTokenService.storeInvitationToken(request.getMail(), linkToken);

        log.info("token {}", linkToken);

        // 초대 링크 생성
        String link = "http://34.22.108.250:8080/accept-invitation?token=" + linkToken;
        String emailBody = "<html><body><p>안녕하세요,</p><p>프로젝트에 참가하려면 아래 링크를 클릭하세요:</p>" +
            "<a href='" + link + "'>프로젝트 참가</a><p>링크는 24시간 후에 만료됩니다.</p></body></html>";

        // 메일 전송
        try {
            emailService.sendHtmlEmail(request.getMail(), "프로젝트 초대", emailBody);
            return link;
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
        Optional<Member> member = memberRepository.findByMailAndIsDeletedFalse(mail);
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

    @Transactional
    public void deleteProject(String token, Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + projectId));

        boolean verified = verifyPermissions(token, project);
        log.info("verified {}", verified);

        if (!verified) {
            throw new BadCredentialsException("Member is not join of the project.");
        }

        projectRepository.delete(project);
    }
}