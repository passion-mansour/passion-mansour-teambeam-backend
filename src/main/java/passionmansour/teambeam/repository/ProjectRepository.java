package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
