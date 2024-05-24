package passionmansour.teambeam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.project.ProjectDto;
import passionmansour.teambeam.model.entity.JoinMember;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.model.enums.ProjectStatus;
import passionmansour.teambeam.repository.JoinMemberRepository;
import passionmansour.teambeam.repository.MemberRepository;
import passionmansour.teambeam.repository.ProjectRepository;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;
import java.util.List;
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

    @Transactional
    public ProjectDto createProject(String token, ProjectDto projectDto) {

        Member member = getMemberByToken(token);

        // 프로젝트 생성
        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setDescription(projectDto.getDescription());
        project.setProjectStatus(ProjectStatus.PROGRESS);
        project.setCreateDate(LocalDateTime.now());

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

    private Member getMemberByToken(String token) {
        // 토큰에서 회원 이름 확인
        String usernameFromToken = tokenService.getUsernameFromToken(token);

        // 해당 회원 정보 조회
        return memberRepository.findByMemberName(usernameFromToken)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with memberName: " + usernameFromToken));
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

        Member member = getMemberByToken(token);

        // 멤버 정보를 통해 참여 정보 조회
        List<JoinMember> joinMembers = joinMemberRepository.findByMember(member);

        //
        List<Project> projects = joinMembers.stream()
            .map(JoinMember::getProject)
            .toList();

        return projects.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
}
