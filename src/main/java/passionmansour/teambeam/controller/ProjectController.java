package passionmansour.teambeam.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.member.response.ErrorResponse;
import passionmansour.teambeam.model.dto.member.response.RegisterResponse;
import passionmansour.teambeam.model.dto.project.ProjectDto;
import passionmansour.teambeam.model.dto.project.ProjectJoinMemberDto;
import passionmansour.teambeam.model.dto.project.request.LinkRequest;
import passionmansour.teambeam.model.dto.project.request.MasterRequest;
import passionmansour.teambeam.model.dto.project.request.UpdateProjectRequest;
import passionmansour.teambeam.model.dto.project.request.UpdateRoleRequest;
import passionmansour.teambeam.model.dto.project.response.ProjectJoinMemberResponse;
import passionmansour.teambeam.model.dto.project.response.ProjectResponse;
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

    @Operation(summary = "프로젝트 생성", description = "사용자가 프로젝트 생성을 위해 필수 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "프로젝트 생성 성공",
            content = @Content(schema = @Schema(implementation = RegisterResponse.class))),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    // 프로젝트 생성
    @PostMapping("/project")
    public ResponseEntity<ProjectResponse> createProject(@RequestHeader("Authorization") String token,
                                                         @Valid @RequestBody ProjectDto projectDto) {

        ProjectResponse response = projectService.createProject(token, projectDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "프로젝트 리스트 조회", description = "사용자가 참여 중인 프로젝트의 리스트를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "프로젝트 리스트 조회 성공"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    // 프로젝트 리스트 조회
    @GetMapping("/projectList")
    public ResponseEntity<ProjectResponse> getProjectList(@RequestHeader("Authorization") String token) {
        List<ProjectDto> projectList = projectService.getProjectList(token);

        ProjectResponse response = new ProjectResponse("Successfully retrieved list", null, projectList, null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "프로젝트 정보 조회", description = "특정 프로젝트의 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "프로젝트 정보 조회 성공"),
        @ApiResponse(responseCode = "401", description = "페이지 요청 권한 없음"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 프로젝트"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    // 프로젝트 정보 조회
    @GetMapping("/team/{projectId}/setting")
    public ResponseEntity<ProjectResponse> getProject(@RequestHeader("Authorization") String token,
                                                      @PathVariable("projectId") Long id) {
        ProjectDto project = projectService.getProject(token, id);

        ProjectResponse response = new ProjectResponse("Successfully retrieved project details", project, null, null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "프로젝트 참여 멤버 조회", description = "특정 프로젝트의 참여 멤버를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "프로젝트 참여 멤버 조회 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 프로젝트"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    // 프로젝트 참여 멤버 조회
    @GetMapping("/team/{projectId}/joinMember")
    public ResponseEntity<ProjectJoinMemberResponse> getProjectJoinMembers(@RequestHeader("Authorization") String token,
                                                                           @PathVariable("projectId") Long id) {
        List<ProjectJoinMemberDto> joinMembers = projectService.getProjectJoinMembers(token, id);

        ProjectJoinMemberResponse response = new ProjectJoinMemberResponse();
        response.setMessage("Successfully retrieved project join members");
        response.setJoinMemberList(joinMembers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "프로젝트 정보 수정", description = "사용자가 특정 프로젝트의 정보를 수정하기 위해 수정할 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "프로젝트 정보 수정 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 프로젝트"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    // 프로젝트 정보 수정
    @PatchMapping("/team/{projectId}/setting")
    public ResponseEntity<ProjectResponse> updateProject(@RequestHeader("Authorization") String token,
                                                         @PathVariable("projectId") Long id,
                                                         @RequestBody UpdateProjectRequest request) {
        ProjectDto project = projectService.updateProject(token, id, request);

        ProjectResponse response = new ProjectResponse("Successfully updated project details", project, null, null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "프로젝트 참여 멤버 역할 수정", description = "사용자가 특정 프로젝트의 참여 멤버 역할을 수정하기 위해 역할을 선택합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "프로젝트 멤버 역할 수정 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 프로젝트"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    // 프로젝트 역할 수정
    @PatchMapping("/team/{projectId}/setting/role")
    public ResponseEntity<ProjectJoinMemberResponse> updateMaster(@RequestHeader("Authorization") String token,
                                                                  @PathVariable("projectId") Long id,
                                                                  @RequestBody UpdateRoleRequest request) {

        List<ProjectJoinMemberDto> joinMembers = projectService.updateMemberRole(token, id, request);

        ProjectJoinMemberResponse response = new ProjectJoinMemberResponse();
        response.setMessage("Successfully updated member roles");
        response.setJoinMemberList(joinMembers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "프로젝트 권한 위임", description = "사용자가 특정 프로젝트의 참여 멤버 중 권한을 위임할 멤버를 선택합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "프로젝트 권한 위임 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 프로젝트"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    // 프로젝트 권한 위입
    @PatchMapping("/team/{projectId}/setting/master")
    public ResponseEntity<ProjectJoinMemberResponse> updateMaster(@RequestHeader("Authorization") String token,
                                                                  @PathVariable("projectId") Long id,
                                                                  @RequestBody MasterRequest request) {
        List<ProjectJoinMemberDto> joinMembers = projectService.updateMaster(token, id, request.getMemberId());

        ProjectJoinMemberResponse response = new ProjectJoinMemberResponse();
        response.setMessage("Successfully updated project master");
        response.setJoinMemberList(joinMembers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "프로젝트 참가 멤버 삭제", description = "사용자가 특정 프로젝트의 참여 멤버 중 삭제할 멤버를 선택합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "프로젝트 참여 멤버 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 프로젝트"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    // 프로젝트 참가 멤버 삭제
    @DeleteMapping("/team/{projectId}/setting/member")
    public ResponseEntity<ProjectJoinMemberResponse> deleteJoinMember(@RequestHeader("Authorization") String token,
                                                                      @PathVariable("projectId") Long id,
                                                                      @RequestBody MasterRequest request) {
        projectService.deleteJoinMember(token, id, request);

        List<ProjectJoinMemberDto> joinMembers = projectService.getProjectJoinMembers(token, id);

        ProjectJoinMemberResponse response = new ProjectJoinMemberResponse();
        response.setMessage("Successfully deleted join member");
        response.setJoinMemberList(joinMembers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "프로젝트 멤버 초대", description = "사용자가 특정 프로젝트의 멤버를 추가하기 위해 메일을 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "프로젝트 멤버 초대 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 프로젝트"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    // 프로젝트 멤버 초대
    @PostMapping("/team/{projectId}/setting/member")
    public ResponseEntity<Map<String, Object>> sendInvitationLink(@RequestHeader("Authorization") String token,
                                                                  @PathVariable("projectId") Long id,
                                                                  @RequestBody LinkRequest request) {
        String link = projectService.sendLink(token, id, request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Successfully sent Email");
        response.put("url", link);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/team/project/{projectId}")
    public ResponseEntity<?> deleteProject(@RequestHeader("Authorization") String token,
                                           @PathVariable("projectId") Long id) {
        projectService.deleteProject(token, id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Successfully deleted project");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
