package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.JoinMember;
import passionmansour.teambeam.model.entity.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
