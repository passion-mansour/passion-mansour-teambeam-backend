package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import passionmansour.teambeam.model.entity.Calendar;
import passionmansour.teambeam.model.entity.Schedule;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.calendar.calendarId = :calendarId AND s.scheduleTime BETWEEN :startDate AND :endDate")
    List<Schedule> findSchedulesBetweenDates(Long calendarId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT s FROM Schedule s WHERE s.calendar IN :calendars AND YEAR(s.scheduleTime) = :year AND MONTH(s.scheduleTime) = :month")
    List<Schedule> findSchedulesByCalendarIdsAndYearAndMonth(@Param("calendars") List<Calendar> calendars, @Param("year") int year, @Param("month") int month);

    List<Schedule> findScheduleByCalendar(Calendar calendar);
}
