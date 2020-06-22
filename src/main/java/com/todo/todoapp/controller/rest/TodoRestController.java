package com.todo.todoapp.controller.rest;

import com.todo.todoapp.model.todo.Todo;
import com.todo.todoapp.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TodoRestController {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoRestController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getTodos() {
        return new ResponseEntity<>(todoRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/todos/{todoId}")
    public ResponseEntity<Todo> getTodo(@PathVariable(required = false) String todoId) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        ResponseEntity responseEntity = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("No Todo exists with the given id!");

        if (optionalTodo.isPresent()) {
            responseEntity = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(optionalTodo.get());
        }

        return responseEntity;
    }

    @PostMapping("/todos")
    public ResponseEntity saveTodo(@RequestBody Todo todo) {
        todoRepository.save(todo);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/todos/{todoId}")
    public ResponseEntity updateTodo(@PathVariable String todoId, @RequestBody Todo todo) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        if (optionalTodo.isPresent()) {
            Todo todoFromRepo = optionalTodo.get();
            todoFromRepo.setName(todo.getName());
            todoFromRepo.setDeadLine(todo.getDeadLine());
            todoFromRepo.setPriority(todo.getPriority());

            todoRepository.save(todoFromRepo);

            httpStatus = HttpStatus.OK;
        }

        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/todos/{todoId}")
    public ResponseEntity deleteTodo(@PathVariable String todoId) {
        todoRepository.deleteById(todoId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
