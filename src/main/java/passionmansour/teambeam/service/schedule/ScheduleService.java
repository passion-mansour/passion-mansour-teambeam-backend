package passionmansour.teambeam.service.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.schedule.*;
import passionmansour.teambeam.model.dto.todolist.dto.TopTodoDTO;
import passionmansour.teambeam.model.entity.*;
import passionmansour.teambeam.repository.MemberRepository;
import passionmansour.teambeam.repository.ProjectRepository;
import passionmansour.teambeam.repository.ScheduleRepository;
import passionmansour.teambeam.repository.TopTodoRepository;
import passionmansour.teambeam.service.todolist.ConvertTodoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TopTodoRepository topTodoRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ConvertSchedule convertSchedule;

    @Autowired
    private ConvertTodoService convertTodoService;

    public GetCalendarResponse getMonthCalendar(Long projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Calendar calendar = project.getCalendar();
        if (calendar == null) {
            throw new RuntimeException("Calendar not found for the project");
        }

//        LocalDate startDate = LocalDate.of(year, month, 1);
//        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
//        LocalDateTime startDateTime = startDate.atStartOfDay();
//        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // Fetch schedules and top todos using queries
//        List<ScheduleDTO> schedules = scheduleRepository.findSchedulesBetweenDates(calendar.getCalendarId(), startDateTime, endDateTime)
//                .stream()
//                .map(schedule -> convertSchedule.convertSchedule(schedule))
//                .collect(Collectors.toList());
//
//        List<ScheduleTopTodoDTO> topTodos = topTodoRepository.findTopTodosByMonth(project, startDate, endDate).stream()
//                .map(topTodo -> convertSchedule.convertTopTodo(topTodo))
//                .collect(Collectors.toList());

        List<ScheduleDTO> schedules = scheduleRepository.findScheduleByCalendar(calendar)
                .stream()
                .map(schedule -> convertSchedule.convertSchedule(schedule)).collect(Collectors.toList());
        List<TopTodo> todos = topTodoRepository.findTopTodosByCalendar(calendar);

        List<ScheduleTopTodoDTO> topTodos = todos.stream().map(topTodo -> convertSchedule.convertTopTodo(topTodo))
                .collect(Collectors.toList());

        return new GetCalendarResponse("200", topTodos, schedules);

    }

    public GetScheduleResponse getSchedule (Long projectId, Long scheduleId){
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        return new GetScheduleResponse(200, convertSchedule.convertSchedule(schedule));


    }
    public ScheduleDTO addSchedule(PostScheduleRequest request, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Calendar calendar = project.getCalendar();
        if (calendar == null) {
            throw new RuntimeException("Calendar not found for the project");
        }



        Schedule schedule = new Schedule();
        schedule.setScheduleTitle(request.getTitle());
        schedule.setScheduleTime(request.getTime());
        schedule.setScheduleLocate(request.getLocation());
        schedule.setScheduleContent(request.getContent());
        schedule.setScheduleLink(request.getLink());
        schedule.setCalendar(calendar);

        List<ScheduleMember> scheduleMembers = request.getMemberId().stream()
                .map(memberId -> {
                    Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));
                    ScheduleMember scheduleMember = new ScheduleMember();
                    scheduleMember.setMember(member);
                    scheduleMember.setSchedule(schedule);
                    return scheduleMember;
                })
                .collect(Collectors.toList());

        schedule.setScheduleMembers(scheduleMembers);

        return convertSchedule.convertSchedule(scheduleRepository.save(schedule));
    }

    public ScheduleDTO updateSchedule(PatchScheduleRequest request, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getCalendar().equals(project.getCalendar())) {
            throw new RuntimeException("Schedule does not belong to the project's calendar");
        }

        schedule.setScheduleTitle(request.getTitle());
        schedule.setScheduleTime(request.getTime());
        schedule.setScheduleLocate(request.getLocation());
        schedule.setScheduleContent(request.getContent());
        schedule.setScheduleLink(request.getLink());

        List<ScheduleMember> scheduleMembers = request.getMemberId().stream()
                .map(memberId -> {
                    Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));
                    ScheduleMember scheduleMember = new ScheduleMember();
                    scheduleMember.setMember(member);
                    scheduleMember.setSchedule(schedule);
                    return scheduleMember;
                })
                .collect(Collectors.toList());

        schedule.setScheduleMembers(scheduleMembers);

        return convertSchedule.convertSchedule(scheduleRepository.save(schedule));
    }

    public void deleteSchedule(Long projectId, Long scheduleId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getCalendar().equals(project.getCalendar())) {
            throw new RuntimeException("Schedule does not belong to the project's calendar");
        }

        scheduleRepository.delete(schedule);
    }
}
