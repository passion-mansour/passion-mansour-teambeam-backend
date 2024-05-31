package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.Calendar;


public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}
