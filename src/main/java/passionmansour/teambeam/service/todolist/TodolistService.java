package passionmansour.teambeam.service.todolist;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.todolist.dto.BottomTodoDTO;
import passionmansour.teambeam.model.dto.todolist.dto.MiddleTodoDTO;
import passionmansour.teambeam.model.dto.todolist.dto.TopTodoDTO;
import passionmansour.teambeam.model.dto.todolist.request.*;
import passionmansour.teambeam.model.entity.*;
import passionmansour.teambeam.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TodolistService {
    @Autowired
    private TopTodoRepository topTodoRepository;

    @Autowired
    private MiddleTodoRepository middleTodoRepository;

    @Autowired
    private BottomTodoRepository bottomTodoRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ConvertTodoService convertTodoService;

    private ModelMapper modelMapper;

    public List<TopTodoDTO> getTodosByProjectId(Long projectId) {
        List<TopTodo> topTodos = topTodoRepository.findAllByProjectId(projectId);
        List<Long> topTodoIds = topTodos.stream().map(TopTodo::getTopTodoId).collect(Collectors.toList());

        List<TopTodo> topTodosWithMiddleTodos = topTodoRepository.findAllWithMiddleTodos(topTodoIds);

        List<Long> middleTodoIds = topTodosWithMiddleTodos.stream()
                .flatMap(t -> t.getMiddleTodos().stream())
                .map(MiddleTodo::getMiddleTodoId)
                .collect(Collectors.toList());

        List<MiddleTodo> middleTodosWithBottomTodos = topTodoRepository.findAllWithBottomTodos(middleTodoIds);

        // 병합 단계 필요
        for (TopTodo topTodo : topTodosWithMiddleTodos) {
            for (MiddleTodo middleTodo : topTodo.getMiddleTodos()) {
                middleTodo.setBottomTodos(
                        middleTodosWithBottomTodos.stream()
                                .filter(b -> b.getMiddleTodoId().equals(middleTodo.getMiddleTodoId()))
                                .flatMap(b -> b.getBottomTodos().stream())
                                .collect(Collectors.toList())
                );
            }
        }
        return topTodosWithMiddleTodos.stream()
                .map(convertTodoService::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<BottomTodoDTO> findBottomTodoById(Long id) {
        Optional<BottomTodo> bottomTodo = bottomTodoRepository.findById(id);
        MiddleTodo middleTodo = bottomTodo.get().getMiddleTodo();
        TopTodo topTodo = middleTodo.getTopTodo();
        return Optional.ofNullable(convertTodoService
                .convertToDto(bottomTodo.get(), topTodo.getTopTodoId(), middleTodo.getMiddleTodoId()));

    }

    @Transactional
    public TopTodoDTO createTopTodo(Long projectId, PostTopTodoRequest request) {

        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (!projectOptional.isPresent()) {
            throw new RuntimeException("Project 찾지 못했습니다.");
        }

        TopTodo topTodo = new TopTodo();
        topTodo.setTopTodoTitle(request.getTitle());
        topTodo.setProject(projectOptional.get());
        topTodo.setStartDate(request.getStartDate());
        topTodo.setEndDate(request.getEndDate());
        topTodo.setCalendar(projectOptional.get().getCalendar());

        return convertTodoService.convertToDto(topTodoRepository.save(topTodo));
    }

    @Transactional
    public MiddleTodoDTO createMiddleTodo(Long projectId, PostMiddleTodoRequest request) {

        Optional<TopTodo> topTodoOptional = topTodoRepository.findById(request.getTopTodoId()); //탑 todo 찾는 로직
        if (!topTodoOptional.isPresent()) {
            throw new RuntimeException("Toptodo 찾지 못했습니다.");
        }
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (!projectOptional.isPresent()) {
            throw new RuntimeException("Project 찾지 못했습니다.");
        }

        MiddleTodo middleTodo = new MiddleTodo();
        middleTodo.setMiddleTodoTitle(request.getTitle());
        middleTodo.setProject(projectOptional.get());
        middleTodo.setTopTodo(topTodoOptional.get());
        middleTodo.setStartDate(request.getStartDate());
        middleTodo.setEndDate(request.getEndDate());
        return convertTodoService.convertToDto(middleTodoRepository.save(middleTodo),topTodoOptional.get().getTopTodoId());
    }

    @Transactional
    public BottomTodoDTO createBottomTodo(Long projectId, PostBottomTodoRequest request) {

        Optional<MiddleTodo> middleTodoOptional = middleTodoRepository.findById(request.getMiddleTodoId()); //탑 todo 찾는 로직
        if (!middleTodoOptional.isPresent()) {
            throw new RuntimeException("MiddleTodo 찾지 못했습니다.");
        }
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (!projectOptional.isPresent()) {
            throw new RuntimeException("Project 찾지 못했습니다.");
        }
        Optional<Member> memberOptional = memberRepository.findById(request.getMember());
        if (!memberOptional.isPresent()) {
            throw new RuntimeException("member 찾지 못했습니다.");
        }


        BottomTodo bottomTodo = new BottomTodo();
        bottomTodo.setBottomTodoTitle(request.getTitle());
        bottomTodo.setProject(projectOptional.get());
        bottomTodo.setMiddleTodo(middleTodoOptional.get());
        bottomTodo.setStartDate(request.getStartDate());
        bottomTodo.setEndDate(request.getEndDate());
        bottomTodo.setMember(memberOptional.get());
        return convertTodoService.convertToDto(bottomTodoRepository.save(bottomTodo),
                middleTodoOptional.get().getTopTodo().getTopTodoId(),
                middleTodoOptional.get().getMiddleTodoId());
    }

    @Transactional
    public TopTodoDTO updateTopTodo(Long projectId, Long topTodoId, PatchTopTodoRequest request) {
        TopTodo topTodo = topTodoRepository.findById(topTodoId)
                .orElseThrow(() -> new RuntimeException("TopTodo 찾지 못했습니다."));
        if (request.getTitle() != null) {
            topTodo.setTopTodoTitle(request.getTitle());
        }
        if (request.isStatus() == true) {
            topTodo.setTopTodoStatus(true);
        }
        if (request.getStartDate() != null) {
            topTodo.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            topTodo.setEndDate(request.getEndDate());
        }
        return convertTodoService.convertToDto(topTodoRepository.save(topTodo));
    }

    @Transactional
    public MiddleTodoDTO updateMiddleTodo(Long projectId, Long middleTodoId, PatchMiddleTodoRequest request) {
        MiddleTodo middleTodo = middleTodoRepository.findById(middleTodoId)
                .orElseThrow(() -> new RuntimeException("MiddleTodo 찾지 못했습니다."));
        if (request.getTitle() != null) {
            middleTodo.setMiddleTodoTitle(request.getTitle());
        }
        if (request.isStatus() == true) {
            middleTodo.setMiddleTodoStatus(true);
        }
        if (request.getStartDate() != null) {
            middleTodo.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            middleTodo.setEndDate(request.getEndDate());
        }
        return convertTodoService.convertToDto(middleTodoRepository.save(middleTodo),middleTodo.getTopTodo().getTopTodoId());
    }

    @Transactional
    public BottomTodoDTO updateBottomTodo(Long projectId, Long bottomTodoId, PatchBottomTodoRequest request) {
        BottomTodo bottomTodo = bottomTodoRepository.findById(bottomTodoId)
                .orElseThrow(() -> new RuntimeException("BottomTodo 찾지 못했습니다."));
        if (request.getTitle() != null) {
            bottomTodo.setBottomTodoTitle(request.getTitle());
        }
        if (request.isStatus() == true) {
            bottomTodo.setBottomTodoStatus(true);
        }
        if (request.getStartDate() != null) {
            bottomTodo.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            bottomTodo.setEndDate(request.getEndDate());
        }
        if (request.getMember() != null){
            Optional<Member> memberOptional = memberRepository.findById(request.getMember());
            if (!memberOptional.isPresent()) {
                throw new RuntimeException("member 찾지 못했습니다.");
            }
            bottomTodo.setMember(memberOptional.get());
        }
        return convertTodoService.convertToDto(bottomTodoRepository.save(bottomTodo),
                bottomTodo.getMiddleTodo().getTopTodo().getTopTodoId(),
                bottomTodo.getMiddleTodo().getMiddleTodoId());
    }

    @Transactional
    public void deleteTopTodo(Long topTodoId) {
        topTodoRepository.deleteById(topTodoId);
    }

    @Transactional
    public void deleteMiddleTodo(Long middleTodoId) {
        middleTodoRepository.deleteById(middleTodoId);
    }

    @Transactional
    public void deleteBottomTodo(Long bottomTodoId) {
        bottomTodoRepository.deleteById(bottomTodoId);
    }

    @Transactional
    public void createSampleTodolist(Project project){
        TopTodo sampleTopTodo = new TopTodo();
        sampleTopTodo.setTopTodoTitle("Sample TopTodo");
        sampleTopTodo.setProject(project);
        sampleTopTodo.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
        sampleTopTodo.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(10)));
        sampleTopTodo.setCalendar(project.getCalendar());
        sampleTopTodo = topTodoRepository.save(sampleTopTodo);

        // Create sample MiddleTodo
        MiddleTodo sampleMiddleTodo = new MiddleTodo();
        sampleMiddleTodo.setMiddleTodoTitle("Sample MiddleTodo");
        sampleMiddleTodo.setProject(project);
        sampleMiddleTodo.setTopTodo(sampleTopTodo);
        sampleMiddleTodo.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
        sampleMiddleTodo.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(5)));
        sampleMiddleTodo = middleTodoRepository.save(sampleMiddleTodo);

        // Create sample BottomTodo
        BottomTodo sampleBottomTodo = new BottomTodo();
        sampleBottomTodo.setBottomTodoTitle("Sample BottomTodo");
        sampleBottomTodo.setProject(project);
        sampleBottomTodo.setMiddleTodo(sampleMiddleTodo);
        sampleBottomTodo.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
        sampleBottomTodo.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(2)));
        // Assuming there is a member to assign, for now, let's fetch the first one
        Optional<Member> memberOptional = memberRepository.findAll().stream().findFirst();
        if (memberOptional.isPresent()) {
            sampleBottomTodo.setMember(memberOptional.get());
        } else {
            throw new RuntimeException("No members found to assign to sample BottomTodo.");
        }
        bottomTodoRepository.save(sampleBottomTodo);

    }

}
