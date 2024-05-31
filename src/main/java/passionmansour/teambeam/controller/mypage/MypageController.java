package passionmansour.teambeam.controller.mypage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.mypage.request.GetMyTodolistRequest;
import passionmansour.teambeam.model.dto.mypage.response.GetMyTodoResponse;
import passionmansour.teambeam.model.dto.schedule.GetCalendarResponse;
import passionmansour.teambeam.model.dto.schedule.ScheduleDTO;
import passionmansour.teambeam.model.dto.todolist.dto.BottomTodoDTO;
import passionmansour.teambeam.model.dto.todolist.request.PatchBottomTodoRequest;
import passionmansour.teambeam.model.dto.todolist.response.GetBottomTodoResponse;
import passionmansour.teambeam.service.mypage.MypageService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public List<GetCalendarResponse> getMySchedule(@PathVariable Long userId){

        List<GetCalendarResponse> response = mypageService.getMyCalendar(userId);
        return response;
    }

    @GetMapping("/main/{bottomTodoId}")
    public ResponseEntity<GetBottomTodoResponse> getBottomTodo(@PathVariable("bottomTodoId") Long bottomTodoId){
        Optional<BottomTodoDTO> bottomTodoDTO = mypageService.findBottomTodoById(bottomTodoId);
        GetBottomTodoResponse response = new GetBottomTodoResponse("200", bottomTodoDTO.get());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/main/{bottomTodoId}")
    public ResponseEntity<BottomTodoDTO> patchBottomTodo(@PathVariable("bottomTodoId") Long bottomTodoId, @RequestBody PatchBottomTodoRequest request) {
        BottomTodoDTO bottomTodo = mypageService.updateBottomTodoMemo(bottomTodoId, request);
        return ResponseEntity.ok(bottomTodo);
    }
}
