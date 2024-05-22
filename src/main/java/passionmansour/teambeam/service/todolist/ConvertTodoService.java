package passionmansour.teambeam.service.todolist;

import org.springframework.stereotype.Service;
import passionmansour.teambeam.model.dto.todolist.dto.BottomTodoDTO;
import passionmansour.teambeam.model.dto.todolist.dto.MiddleTodoDTO;
import passionmansour.teambeam.model.dto.todolist.dto.TopTodoDTO;
import passionmansour.teambeam.model.entity.BottomTodo;
import passionmansour.teambeam.model.entity.MiddleTodo;
import passionmansour.teambeam.model.entity.TopTodo;

import java.util.stream.Collectors;

@Service
public class ConvertTodoService {
    public TopTodoDTO convertToDto(TopTodo topTodo) {
        TopTodoDTO dto = new TopTodoDTO();
        dto.setTopTodoId(topTodo.getTopTodoId());
        dto.setTitle(topTodo.getTopTodoTitle());
        dto.setStatus(topTodo.isTopTodoStatus());
        dto.setStartDate(topTodo.getStartDate());
        dto.setEndDate(topTodo.getEndDate());
        dto.setMiddleTodos(topTodo.getMiddleTodos().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public MiddleTodoDTO convertToDto(MiddleTodo middleTodo) {
        MiddleTodoDTO dto = new MiddleTodoDTO();
        dto.setMiddleTodoId(middleTodo.getMiddleTodoId());
        dto.setTitle(middleTodo.getMiddleTodoTitle());
        dto.setStatus(middleTodo.isMiddleTodoStatus());
        dto.setStartDate(middleTodo.getStartDate());
        dto.setEndDate(middleTodo.getEndDate());
        dto.setBottomTodos(middleTodo.getBottomTodos().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public BottomTodoDTO convertToDto(BottomTodo bottomTodo) {
        BottomTodoDTO dto = new BottomTodoDTO();
        dto.setBottomTodoId(bottomTodo.getBottomTodoId());
        dto.setTitle(bottomTodo.getBottomTodoTitle());
        dto.setStatus(bottomTodo.isBottomTodoStatus());
        dto.setStartDate(bottomTodo.getStartDate());
        dto.setEndDate(bottomTodo.getEndDate());
        dto.setMemo(bottomTodo.getMemo());
        dto.setMember(bottomTodo.getMember());
        return dto;
    }
}
