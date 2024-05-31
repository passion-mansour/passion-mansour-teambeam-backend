package passionmansour.teambeam.controller.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.schedule.*;
import passionmansour.teambeam.model.entity.Schedule;
import passionmansour.teambeam.service.schedule.ScheduleService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/team")
public class CalendarController {

    @Autowired
    private ScheduleService scheduleService;
    @GetMapping("/{projectId}/calendar/month")
    public ResponseEntity<GetCalendarResponse> GetMonthCalendar(@PathVariable Long projectId) {
        GetCalendarResponse response = scheduleService.getMonthCalendar(projectId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{projectId}/calendar/{scheduleId}")
    public ResponseEntity<GetScheduleResponse> GetDayCalendar(@PathVariable Long projectId,
                                                              @PathVariable Long scheduleId) {
        GetScheduleResponse response = scheduleService.getSchedule(projectId,scheduleId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{projectId}/calendar")
    public ResponseEntity<ScheduleDTO> PostSchedule(@RequestBody PostScheduleRequest request,
                                                 @PathVariable Long projectId) {
        ScheduleDTO response = scheduleService.addSchedule(request, projectId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{projectId}/calendar")
    public ResponseEntity<ScheduleDTO> PatchSchedule(@RequestBody PatchScheduleRequest request,
                                             @PathVariable Long projectId) {
        ScheduleDTO response = scheduleService.updateSchedule(request,projectId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{projectId}/calendar/{scheduleId}")
    public ResponseEntity<Void> DeleteSchedule(@PathVariable Long projectId, @PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(projectId,scheduleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
