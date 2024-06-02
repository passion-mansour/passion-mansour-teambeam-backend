package passionmansour.teambeam.service.mypage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.mypage.response.GetMyTodoResponse;
import passionmansour.teambeam.model.dto.schedule.GetCalendarResponse;
import passionmansour.teambeam.model.dto.schedule.ScheduleDTO;
import passionmansour.teambeam.model.dto.schedule.ScheduleTopTodoDTO;
import passionmansour.teambeam.model.dto.todolist.dto.BottomTodoDTO;
import passionmansour.teambeam.model.dto.todolist.request.PatchBottomTodoRequest;
import passionmansour.teambeam.model.entity.*;
import passionmansour.teambeam.repository.BottomTodoRepository;
import passionmansour.teambeam.repository.JoinMemberRepository;
import passionmansour.teambeam.repository.MemberRepository;
import passionmansour.teambeam.repository.ScheduleRepository;
import passionmansour.teambeam.service.schedule.ConvertSchedule;
import passionmansour.teambeam.service.schedule.ScheduleService;
import passionmansour.teambeam.service.todolist.ConvertTodoService;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class MypageService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JoinMemberRepository joinMemberRepository;

    @Autowired
    private BottomTodoRepository bottomTodoRepository;

    @Autowired
    private ConvertTodoService convertTodoService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ConvertSchedule convertSchedule;

    public List<GetMyTodoResponse> getMyTodo(Long userId, Date date){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Project not found"));;
        List<BottomTodo> bottomTodoList = bottomTodoRepository
                .findByMemberAndStartDateLessThanEqualAndEndDateGreaterThanEqual(member, date, date);
        Map<Long, List<BottomTodo>> todosByProject = bottomTodoList.stream()
                .collect(Collectors.groupingBy(todo -> todo.getProject().getProjectId()));

        return todosByProject.entrySet().stream()
                .map(entry -> {
                    Long projectId = entry.getKey();
                    List<BottomTodo> bottomTodos = entry.getValue();
                    String projectName = bottomTodos.stream()
                            .findFirst()
                            .map(todo -> todo.getProject().getProjectName())
                            .orElse("Unknown Project");

                    List<BottomTodoDTO> todoDtos = bottomTodos.stream()
                            .map(todo -> convertTodoService.convertToDto(todo, todo.getMiddleTodo().getTopTodo().getTopTodoId(), todo.getMiddleTodo().getMiddleTodoId()))
                            .collect(Collectors.toList());

                    return new GetMyTodoResponse(projectId, projectName, todoDtos);
                })
                .collect(Collectors.toList());
    }
    public List<GetCalendarResponse> getMyCalendar(Long userId){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        List<JoinMember> joinMemberList = joinMemberRepository.findByMember(member);
        List<ScheduleTopTodoDTO> allTopTodos = new ArrayList<>();
        List<ScheduleDTO> allSchedules = new ArrayList<>();

        joinMemberList.forEach(joinMember -> {
            GetCalendarResponse calendarResponse = scheduleService.getMonthCalendar(joinMember.getProject().getProjectId());
            allTopTodos.addAll(calendarResponse.getTopTodos());
            allSchedules.addAll(calendarResponse.getSchedules());
        });

        GetCalendarResponse combinedResponse = new GetCalendarResponse();
        combinedResponse.setStatus("200");
        combinedResponse.setTopTodos(allTopTodos);
        combinedResponse.setSchedules(allSchedules);


        return Collections.singletonList(combinedResponse);
    }

    public Optional<BottomTodoDTO> findBottomTodoById(Long id) {
        Optional<BottomTodo> bottomTodo = bottomTodoRepository.findById(id);
        MiddleTodo middleTodo = bottomTodo.get().getMiddleTodo();
        TopTodo topTodo = middleTodo.getTopTodo();
        return Optional.ofNullable(convertTodoService
                .convertToDto(bottomTodo.get(), topTodo.getTopTodoId(), middleTodo.getMiddleTodoId()));

    }

    @Transactional
    public BottomTodoDTO updateBottomTodoMemo(Long bottomTodoId, PatchBottomTodoRequest request) {
        BottomTodo bottomTodo = bottomTodoRepository.findById(bottomTodoId)
                .orElseThrow(() -> new RuntimeException("BottomTodo 찾지 못했습니다."));
        if (request.getMemo() != null) {
            bottomTodo.setMemo(request.getMemo());
        }
        return convertTodoService.convertToDto(bottomTodoRepository.save(bottomTodo),
                bottomTodo.getMiddleTodo().getTopTodo().getTopTodoId(),
                bottomTodo.getMiddleTodo().getMiddleTodoId());
    }

}
