package passionmansour.teambeam.controller.mypage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.mypage.request.GetMyTodolistRequest;
import passionmansour.teambeam.model.dto.mypage.response.GetMyTodoResponse;
import passionmansour.teambeam.model.dto.schedule.GetCalendarResponse;
import passionmansour.teambeam.model.dto.schedule.ScheduleDTO;
import passionmansour.teambeam.service.mypage.MypageService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/my")
public class MypageController {
    @Autowired
    private MypageService mypageService;

    @GetMapping("/main/todo/{userId}")
    public List<GetMyTodoResponse> getMyTodolist(@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                                                 @PathVariable Long userId){
        List<GetMyTodoResponse> response = mypageService.getMyTodo(userId, date);
        return response;
    }

    @GetMapping("/main/schedule/{userId}")
    public List<GetCalendarResponse> getMySchedule(@PathVariable Long userId,
                                              @RequestParam(name = "month") int month,
                                              @RequestParam(name = "year") int year){

        List<GetCalendarResponse> response = mypageService.getMyCalendar(userId, year, month);
        return response;
    }
}
