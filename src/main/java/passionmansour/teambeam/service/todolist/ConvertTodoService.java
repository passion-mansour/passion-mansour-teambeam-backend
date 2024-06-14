package passionmansour.teambeam.service.todolist;

import org.springframework.stereotype.Service;
import passionmansour.teambeam.model.dto.todolist.dto.BottomTodoDTO;
import passionmansour.teambeam.model.dto.todolist.dto.MiddleTodoDTO;
import passionmansour.teambeam.model.dto.todolist.dto.TopTodoDTO;
import passionmansour.teambeam.model.entity.BottomTodo;
import passionmansour.teambeam.model.entity.MiddleTodo;
import passionmansour.teambeam.model.entity.TopTodo;

import java.util.List;
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
                .map(middleTodo -> convertToDto(middleTodo, topTodo.getTopTodoId()))
                .collect(Collectors.toList()));
        return dto;
    }

    public MiddleTodoDTO convertToDto(MiddleTodo middleTodo, Long topTodoId) {
        MiddleTodoDTO dto = new MiddleTodoDTO();
        dto.setMiddleTodoId(middleTodo.getMiddleTodoId());
        dto.setTopTodoId(topTodoId);
        dto.setTitle(middleTodo.getMiddleTodoTitle());
        dto.setStatus(middleTodo.isMiddleTodoStatus());
        dto.setStartDate(middleTodo.getStartDate());
        dto.setEndDate(middleTodo.getEndDate());
        dto.setBottomTodos(middleTodo.getBottomTodos().stream()
                .map(bottomTodo -> convertToDto(bottomTodo, topTodoId, middleTodo.getMiddleTodoId() ))
                .collect(Collectors.toList()));
        return dto;
    }

    public BottomTodoDTO convertToDto(BottomTodo bottomTodo,Long topTodoId, Long middleTodoId) {
        BottomTodoDTO dto = new BottomTodoDTO();
        dto.setBottomTodoId(bottomTodo.getBottomTodoId());
        dto.setMiddleTodoId(middleTodoId);
        dto.setTopTodoId(topTodoId);
        dto.setTitle(bottomTodo.getBottomTodoTitle());
        dto.setStatus(bottomTodo.isBottomTodoStatus());
        dto.setStartDate(bottomTodo.getStartDate());
        dto.setEndDate(bottomTodo.getEndDate());
        dto.setMemo(bottomTodo.getMemo());
        dto.setBottomMember(bottomTodo.getMember().getMemberId(),bottomTodo.getMember().getMemberName());
        List<String> tagNames = bottomTodo.getTodoTags().stream()
                .map(todoTag -> todoTag.getTag().getTagName())
                .collect(Collectors.toList());
        dto.setTaglist(tagNames);
        return dto;
    }
}
