package passionmansour.teambeam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.project.ProjectDto;
import passionmansour.teambeam.model.dto.project.ProjectJoinMemberDto;
import passionmansour.teambeam.model.dto.project.request.LinkRequest;
import passionmansour.teambeam.model.dto.project.request.MasterRequest;
import passionmansour.teambeam.model.dto.project.request.UpdateProjectRequest;
import passionmansour.teambeam.model.dto.project.request.UpdateRoleRequest;
import passionmansour.teambeam.model.dto.project.response.ProjectJoinMemberResponse;
import passionmansour.teambeam.model.dto.project.response.ProjectResponse;
import passionmansour.teambeam.model.dto.project.response.TokenAuthenticationResponse;
import passionmansour.teambeam.service.ProjectService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Project Controller", description = "프로젝트 관련 API입니다.")
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    // 프로젝트 생성
    @PostMapping("/project")
    public ResponseEntity<ProjectResponse> createProject(@RequestHeader("Authorization") String token,
                                                         @Valid @RequestBody ProjectDto projectDto) {

        ProjectDto project = projectService.createProject(token, projectDto);

        ProjectResponse response = new ProjectResponse("Project creation successful", project, null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 프로젝트 리스트 조회
    @GetMapping("/projectList")
    public ResponseEntity<ProjectResponse> getProjectList(@RequestHeader("Authorization") String token) {
        List<ProjectDto> projectList = projectService.getProjectList(token);

        ProjectResponse response = new ProjectResponse("Project list inquiry successful", null, projectList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 프로젝트 정보 조회
    @GetMapping("/team/{projectId}/setting")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable("projectId") Long id) {
        ProjectDto project = projectService.getProject(id);

        ProjectResponse response = new ProjectResponse("Project list inquiry successful", project, null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 프로젝트 참여 멤버 조회
    @GetMapping("/team/{projectId}/joinMember")
    public ResponseEntity<ProjectJoinMemberResponse> getProjectJoinMembers(@PathVariable("projectId") Long id) {
        List<ProjectJoinMemberDto> joinMembers = projectService.getProjectJoinMembers(id);

        ProjectJoinMemberResponse response = new ProjectJoinMemberResponse();
        response.setMessage("Project joinMember inquiry successful");
        response.setJoinMemberList(joinMembers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 프로젝트 정보 수정
    @PatchMapping("/team/{projectId}/setting")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable("projectId") Long id, @RequestBody UpdateProjectRequest request) {
        ProjectDto project = projectService.updateProject(id, request);

        ProjectResponse response = new ProjectResponse("Update project successful", project, null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 프로젝트 역할 수정
    @PatchMapping("/team/{projectId}/setting/role")
    public ResponseEntity<ProjectJoinMemberResponse> updateMaster(@PathVariable("projectId") Long id, @RequestBody UpdateRoleRequest request) {

        List<ProjectJoinMemberDto> joinMembers = projectService.updateMemberRole(id, request);

        ProjectJoinMemberResponse response = new ProjectJoinMemberResponse();
        response.setMessage("Modifying all members' roles successfully");
        response.setJoinMemberList(joinMembers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 프로젝트 권한 위입
    @PatchMapping("/team/{projectId}/setting/master")
    public ResponseEntity<ProjectJoinMemberResponse> updateMaster(@RequestHeader("Authorization") String token,
                                          @PathVariable("projectId") Long id,
                                          @RequestBody MasterRequest request) {
        List<ProjectJoinMemberDto> joinMembers = projectService.updateMaster(token, id, request.getMemberId());

        ProjectJoinMemberResponse response = new ProjectJoinMemberResponse();
        response.setMessage("Modifying member master information successfully");
        response.setJoinMemberList(joinMembers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 프로젝트 참가 멤버 삭제
    @DeleteMapping("/team/{projectId}/setting/member")
    public ResponseEntity<ProjectJoinMemberResponse> deleteJoinMember(@PathVariable("projectId") Long id,
                                                                      @RequestBody MasterRequest request) {
        projectService.deleteJoinMember(id, request);

        List<ProjectJoinMemberDto> joinMembers = projectService.getProjectJoinMembers(id);

        ProjectJoinMemberResponse response = new ProjectJoinMemberResponse();
        response.setMessage("Delete member successfully");
        response.setJoinMemberList(joinMembers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 프로젝트 멤버 초대
    @PostMapping("/team/{projectId}/setting/member")
    public ResponseEntity<Map<String, Object>> sendInvitationLink(@PathVariable("projectId") Long id,
                                                                  @RequestBody LinkRequest request) {
        String link = projectService.sendLink(id, request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Email sent successfully");
        response.put("url", link);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 프로젝트 초대 수락
    @PostMapping("/team/{projectId}/invitation")
    public ResponseEntity<?> tokenAuthentication(@RequestParam("token") String token) {

        TokenAuthenticationResponse response = projectService.tokenAuthentication(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
