package passionmansour.teambeam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.project.ProjectDto;
import passionmansour.teambeam.model.dto.project.response.ProjectResponse;
import passionmansour.teambeam.service.ProjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Project Controller", description = "프로젝트 관련 API입니다.")
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/project")
    public ResponseEntity<ProjectResponse> createProject(@RequestHeader("Authorization") String token,
                                                         @Valid @RequestBody ProjectDto projectDto) {

        ProjectDto project = projectService.createProject(token, projectDto);

        ProjectResponse response = new ProjectResponse("Project creation successful", project, null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/projectList")
    public ResponseEntity<ProjectResponse> getProjectList(@RequestHeader("Authorization") String token) {
        List<ProjectDto> projectList = projectService.getProjectList(token);

        ProjectResponse response = new ProjectResponse("Project lookup successful", null, projectList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
