package passionmansour.teambeam.controller.todolist;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.todolist.PostTopTodoRequestDto;

@Controller
@RequestMapping("/api/team")
public class TodolistController {

    @GetMapping("/{projectId}/todo")
    public ResponseEntity<Void> getTodolist(@PathVariable Long projectId){

    }

    @GetMapping("{projectId}/todo/{bottomTodoId}")
    public ResponseEntity<Void> getBottomTodo(@PathVariable Long projectId, @PathVariable Long bottomTodoId){

    }

    @PostMapping("/{projectId}/todo/top")
    public ResponseEntity<Void> postTopTodo(@RequestBody PostTopTodoRequestDto postTopTodoRequestDto){
        //서비스
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{projectId}/todo/middle")
    public ResponseEntity<Void> postMiddleTodo(@PathVariable Long projectId, @RequestBody ){

    }

    @PostMapping("/{projectId}/todo/bottom")
    public ResponseEntity<Void> postBottomTodo(@PathVariable Long projectId, @RequestBody ){

    }

    @PatchMapping("/{projectId}/todo/top")
    public ResponseEntity<Void> patchTopTodo(@PathVariable Long projectId, @RequestBody ){

    }

    @PatchMapping("/{projectId}/todo/middle")
    public ResponseEntity<Void> patchMiddleTodo(@PathVariable Long projectId, @RequestBody ){

    }

    @PatchMapping("/{projectId}/todo/bottom")
    public ResponseEntity<Void> patchBottomTodo(@PathVariable Long projectId, @RequestBody ){

    }

    @DeleteMapping("/{projectId}/todo/top")
    public ResponseEntity<Void> deleteTopTodo(@PathVariable Long projectId, @RequestBody ){

    }

    @DeleteMapping("/{projectId}/todo/middle")
    public ResponseEntity<Void> deleteMiddleTodo(@PathVariable Long projectId, @RequestBody ){

    }

    @DeleteMapping("/{projectId}/todo/bottom")
    public ResponseEntity<Void> deleteBottomTodo(@PathVariable Long projectId, @RequestBody ){

    }

}
