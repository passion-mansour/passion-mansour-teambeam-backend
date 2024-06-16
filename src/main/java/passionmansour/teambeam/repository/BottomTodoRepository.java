package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.BottomTodo;
import passionmansour.teambeam.model.entity.Member;

import java.time.LocalDate;
import java.util.List;

public interface BottomTodoRepository extends JpaRepository<BottomTodo, Long> {
    List<BottomTodo> findByMemberAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Member member, LocalDate date, LocalDate date2);

    List<BottomTodo> findByEndDate(LocalDate endDate);
}
