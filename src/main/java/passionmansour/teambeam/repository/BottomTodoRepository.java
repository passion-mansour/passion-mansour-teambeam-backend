package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.BottomTodo;
import passionmansour.teambeam.model.entity.Member;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface BottomTodoRepository extends JpaRepository<BottomTodo, Long> {
    List<BottomTodo> findByMemberAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Member member, Date date, Date date2);
}
