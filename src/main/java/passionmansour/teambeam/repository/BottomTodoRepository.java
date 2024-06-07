package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.BottomTodo;
import passionmansour.teambeam.model.entity.Member;

import java.util.Date;
import java.util.List;

public interface BottomTodoRepository extends JpaRepository<BottomTodo, Long> {
    List<BottomTodo> findByMemberAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Member member, Date date, Date date2);

    List<BottomTodo> findByEndDate(Date endDate);
}
