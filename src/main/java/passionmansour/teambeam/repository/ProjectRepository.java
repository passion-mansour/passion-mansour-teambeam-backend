package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Project;

import java.util.Optional;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByProjectId(Long projectId);

    Optional<Project> findByJoinMembers(Member member);
}
