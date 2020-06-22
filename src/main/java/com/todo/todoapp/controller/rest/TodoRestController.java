package com.todo.todoapp.controller.rest;

import com.todo.todoapp.model.todo.Todo;
import com.todo.todoapp.service.ITodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoRestController {

    private final ITodoService todoService;

    @Autowired
    public TodoRestController(ITodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getTodos() {
        return todoService.getTodos();
    }

    @GetMapping("/todos/{todoId}")
    public ResponseEntity<Object> getTodo(@PathVariable String todoId) {
        return todoService.getTodo(todoId);
    }

    @PostMapping("/todos")
    public ResponseEntity<Object> saveTodo(@RequestBody Todo todo) {
        return todoService.saveTodo(todo);
    }

    @PutMapping("/todos/{todoId}")
    public ResponseEntity<Object> updateTodo(@PathVariable String todoId, @RequestBody Todo todo) {
        return todoService.updateTodo(todoId, todo);
    }

    @DeleteMapping("/todos/{todoId}")
    public ResponseEntity<Object> deleteTodo(@PathVariable String todoId) {
        return todoService.deleteTodo(todoId);
    }
}
