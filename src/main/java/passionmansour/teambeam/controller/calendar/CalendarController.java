package passionmansour.teambeam.controller.calendar;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.schedule.PostScheduleRequest;

@RestController
@RequestMapping("/api/team")
public class CalendarController {
    @GetMapping("/{projectId}/calendar/month")
    public ResponseEntity<Void> GetMonthCalendar(@RequestParam(name = "date") Long month,
                                                 @PathVariable Long projectId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{projectId}/calendar/day")
    public ResponseEntity<Void> GetDayCalendar(@RequestParam(name = "date") Long month,
                                               @PathVariable Long projectId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{projectId}/calendar")
    public ResponseEntity<Void> PostSchedule(@RequestBody PostScheduleRequest response,
                                             @PathVariable String projectId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{projectId}/calendar")
    public ResponseEntity<Void> PatchSchedule(@RequestBody PostScheduleRequest response,
                                             @PathVariable String projectId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}/calendar/{scheduleId}")
    public ResponseEntity<Void> DeleteSchedule(@PathVariable String projectId, @PathVariable String scheduleId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
