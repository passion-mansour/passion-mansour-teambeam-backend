package passionmansour.teambeam.service.todolist;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.todolist.dto.BottomTodoDTO;
import passionmansour.teambeam.model.dto.todolist.dto.TopTodoDTO;
import passionmansour.teambeam.model.dto.todolist.request.*;
import passionmansour.teambeam.model.entity.*;
import passionmansour.teambeam.repository.*;

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
    private ConvertTodoService convertTodoService;

    @Autowired
    private MemberRepository memberRepository;

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
        return bottomTodoRepository.findById(id)
                .map(convertTodoService::convertToDto);
    }

    @Transactional
    public TopTodo createTopTodo(Long projectId, PostTopTodoRequest request) {

        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (!projectOptional.isPresent()) {
            throw new RuntimeException("Project 찾지 못했습니다.");
        }

        TopTodo topTodo = new TopTodo();
        topTodo.setTopTodoTitle(request.getTitle());
        topTodo.setProject(projectOptional.get());
        topTodo.setStartDate(request.getStartDate());
        topTodo.setEndDate(request.getEndDate());
        return topTodoRepository.save(topTodo);
    }

    @Transactional
    public MiddleTodo createMiddleTodo(Long projectId, PostMiddleTodoRequest request) {

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
        return middleTodoRepository.save(middleTodo);
    }

    @Transactional
    public BottomTodo createBottomTodo(Long projectId, PostBottomTodoRequest request) {

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
        return bottomTodoRepository.save(bottomTodo);
    }

    @Transactional
    public TopTodo updateTopTodo(Long projectId, Long topTodoId, PatchTopTodoRequest request) {
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
        return topTodoRepository.save(topTodo);
    }

    @Transactional
    public MiddleTodo updateMiddleTodo(Long projectId, Long middleTodoId, PatchMiddleTodoRequest request) {
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
        return middleTodoRepository.save(middleTodo);
    }

    @Transactional
    public BottomTodo updateBottomTodo(Long projectId, Long bottomTodoId, PatchBottomTodoRequest request) {
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

        return bottomTodoRepository.save(bottomTodo);
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


}
