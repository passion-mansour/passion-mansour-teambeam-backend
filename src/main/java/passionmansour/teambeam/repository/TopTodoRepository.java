package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import passionmansour.teambeam.model.entity.TopTodo;

import java.util.List;

@Repository
public interface TopTodoRepository extends JpaRepository<TopTodo, Long> {
    @Query("SELECT t FROM TopTodo t LEFT JOIN FETCH t.middleTodos m LEFT JOIN FETCH m.bottomTodos WHERE t.project.projectId = :projectId")
    List<TopTodo> findAllWithDetailsByProjectId(Long projectId);

}
