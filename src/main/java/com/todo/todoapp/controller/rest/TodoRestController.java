package com.todo.todoapp.controller.rest;

import com.todo.todoapp.model.todo.Todo;
import com.todo.todoapp.service.ITodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoRestController {

    private static final String GET_MAPPING_TODOS = "/todos";
    private static final String GET_MAPPING_TODOS_WITH_TODO_ID_PATHVAR = "/todos/{todoId}";

    private static final String POST_MAPPING_TODOS = "/todos";

    private static final String PUT_MAPPING_TODOS_WITH_TODO_ID_PATHVAR = "/todos/{todoId}";

    private static final String DELETE_MAPPING_TODOS_WITH_TODO_ID_PATHVAR = "/todos/{todoId}";

    private final ITodoService todoService;

    @Autowired
    public TodoRestController(ITodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(GET_MAPPING_TODOS)
    public ResponseEntity<List<Todo>> getTodos() {
        return todoService.getTodos();
    }

    @GetMapping(GET_MAPPING_TODOS_WITH_TODO_ID_PATHVAR)
    public ResponseEntity<Object> getTodo(@PathVariable String todoId) {
        return todoService.getTodo(todoId);
    }

    @PostMapping(POST_MAPPING_TODOS)
    public ResponseEntity<Object> saveTodo(@RequestBody Todo todo) {
        return todoService.saveTodo(todo);
    }

    @PutMapping(PUT_MAPPING_TODOS_WITH_TODO_ID_PATHVAR)
    public ResponseEntity<Object> updateTodo(@PathVariable String todoId, @RequestBody Todo todo) {
        return todoService.updateTodo(todoId, todo);
    }

    @DeleteMapping(DELETE_MAPPING_TODOS_WITH_TODO_ID_PATHVAR)
    public ResponseEntity<Object> deleteTodo(@PathVariable String todoId) {
        return todoService.deleteTodo(todoId);
    }
}
