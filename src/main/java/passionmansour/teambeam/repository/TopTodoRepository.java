package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import passionmansour.teambeam.model.entity.MiddleTodo;
import passionmansour.teambeam.model.entity.TopTodo;

import java.util.List;

@Repository
public interface TopTodoRepository extends JpaRepository<TopTodo, Long> {
    @Query("SELECT t FROM TopTodo t WHERE t.project.projectId = :projectId")
    List<TopTodo> findAllByProjectId(Long projectId);

    @Query("SELECT t FROM TopTodo t LEFT JOIN FETCH t.middleTodos WHERE t.topTodoId IN :ids")
    List<TopTodo> findAllWithMiddleTodos(List<Long> ids);

    @Query("SELECT m FROM MiddleTodo m LEFT JOIN FETCH m.bottomTodos WHERE m.middleTodoId IN :ids")
    List<MiddleTodo> findAllWithBottomTodos(List<Long> ids);

}
