package passionmansour.teambeam.controller.todolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.todolist.dto.BottomTodoDTO;
import passionmansour.teambeam.model.dto.todolist.dto.TopTodoDTO;
import passionmansour.teambeam.model.dto.todolist.request.*;
import passionmansour.teambeam.model.dto.todolist.response.GetBottomTodoResponse;
import passionmansour.teambeam.model.dto.todolist.response.GetTodolistResponse;
import passionmansour.teambeam.model.entity.BottomTodo;
import passionmansour.teambeam.model.entity.MiddleTodo;
import passionmansour.teambeam.model.entity.TopTodo;
import passionmansour.teambeam.service.todolist.TodolistService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/team")
public class TodolistController {
    private final TodolistService todolistService;

    @Autowired
    public TodolistController(TodolistService todolistService) {
        this.todolistService = todolistService;
    }

    @GetMapping("/{projectId}/todo")
    public ResponseEntity<GetTodolistResponse> getTodolist(@PathVariable Long projectId){
        List<TopTodoDTO> todolist = todolistService.getTodosByProjectId(projectId);
        GetTodolistResponse response = new GetTodolistResponse("200", todolist);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{projectId}/todo/{bottomTodoId}")
    public ResponseEntity<GetBottomTodoResponse> getBottomTodo(@PathVariable Long projectId, @PathVariable Long bottomTodoId){
        Optional<BottomTodoDTO> bottomTodoDTO = todolistService.findBottomTodoById(bottomTodoId);
        GetBottomTodoResponse response = new GetBottomTodoResponse("200", bottomTodoDTO.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{projectId}/todo/top")
    public ResponseEntity<Void> postTopTodo(@PathVariable Long projectId,@RequestBody PostTopTodoRequest postTopTodoRequest){
        TopTodo topTodo = todolistService.createTopTodo(projectId, postTopTodoRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{projectId}/todo/middle")
    public ResponseEntity<Void> postMiddleTodo(@PathVariable Long projectId, @RequestBody PostMiddleTodoRequest request){
        MiddleTodo middleTodo = todolistService.createMiddleTodo(projectId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{projectId}/todo/bottom")
    public ResponseEntity<Void> postBottomTodo(@PathVariable Long projectId, @RequestBody PostBottomTodoRequest request){
        BottomTodo bottomTodo = todolistService.createBottomTodo(projectId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{projectId}/todo/top/{topTodoId}")
    public ResponseEntity<Void> patchTopTodo(@PathVariable Long projectId, @PathVariable Long topTodoId, @RequestBody PatchTopTodoRequest request) {
        todolistService.updateTopTodo(projectId, topTodoId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{projectId}/todo/middle/{middleTodoId}")
    public ResponseEntity<Void> patchMiddleTodo(@PathVariable Long projectId, @PathVariable Long middleTodoId, @RequestBody PatchMiddleTodoRequest request) {
        todolistService.updateMiddleTodo(projectId, middleTodoId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{projectId}/todo/bottom/{bottomTodoId}")
    public ResponseEntity<Void> patchBottomTodo(@PathVariable Long projectId, @PathVariable Long bottomTodoId, @RequestBody PatchBottomTodoRequest request) {
        todolistService.updateBottomTodo(projectId, bottomTodoId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}/todo/top/{topTodoId}")
    public ResponseEntity<Void> deleteTopTodo(@PathVariable Long projectId, @PathVariable Long topTodoId) {
        todolistService.deleteTopTodo(topTodoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}/todo/middle/{middleTodoId}")
    public ResponseEntity<Void> deleteMiddleTodo(@PathVariable Long projectId, @PathVariable Long middleTodoId) {
        todolistService.deleteMiddleTodo(middleTodoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}/todo/bottom/{bottomTodoId}")
    public ResponseEntity<Void> deleteBottomTodo(@PathVariable Long projectId, @PathVariable Long bottomTodoId) {
        todolistService.deleteBottomTodo(bottomTodoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
