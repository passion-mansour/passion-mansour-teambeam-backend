package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.TodoTag;

public interface TodoTagRepository extends JpaRepository<TodoTag, Long> {
}
