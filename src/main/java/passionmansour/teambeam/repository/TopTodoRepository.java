package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import passionmansour.teambeam.model.entity.Calendar;
import passionmansour.teambeam.model.entity.MiddleTodo;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.model.entity.TopTodo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface TopTodoRepository extends JpaRepository<TopTodo, Long> {
    @Query("SELECT t FROM TopTodo t WHERE t.project.projectId = :projectId")
    List<TopTodo> findAllByProjectId(Long projectId);

    @Query("SELECT t FROM TopTodo t LEFT JOIN FETCH t.middleTodos WHERE t.topTodoId IN :ids")
    List<TopTodo> findAllWithMiddleTodos(List<Long> ids);

    @Query("SELECT m FROM MiddleTodo m LEFT JOIN FETCH m.bottomTodos WHERE m.middleTodoId IN :ids")
    List<MiddleTodo> findAllWithBottomTodos(List<Long> ids);

    @Query("SELECT t FROM TopTodo t WHERE t.project = :project AND t.startDate <= :endDate AND t.endDate >= :startDate")
    List<TopTodo> findTopTodosByMonth(@Param("project") Project project,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    @Query("SELECT t FROM TopTodo t WHERE t.calendar = :calendar")
    List<TopTodo> findTopTodosByCalendar(@Param("calendar") Calendar calendar);

}
