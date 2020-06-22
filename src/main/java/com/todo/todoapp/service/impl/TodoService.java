package com.todo.todoapp.service.impl;

import com.todo.todoapp.model.todo.Todo;
import com.todo.todoapp.repository.TodoRepository;
import com.todo.todoapp.service.ITodoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService implements ITodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public ResponseEntity<List<Todo>> getTodos() {
        return new ResponseEntity<>(todoRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getTodo(String todoId) {
        if (ObjectUtils.isEmpty(todoId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The given id was null!");
        }

        Optional<Todo> optionalTodo = todoRepository.findById(todoId);

        return optionalTodo
                .<ResponseEntity<Object>>map(todo -> ResponseEntity.status(HttpStatus.OK).body(todo))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Todo exists with the given id!"));
    }

    @Override
    public ResponseEntity<Object> saveTodo(Todo todo) {

        if (ObjectUtils.isEmpty(todo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The given JSON was null or empty!");
        }

        todoRepository.save(todo);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> updateTodo(String todoId, Todo todo) {

        if (ObjectUtils.isEmpty(todoId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The given id was null!");
        } else if (ObjectUtils.isEmpty(todo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The given JSON was null or empty!");
        }

        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Todo was found with the given id!");

        if (optionalTodo.isPresent()) {
            Todo todoFromRepo = optionalTodo.get();
            todoFromRepo.setName(todo.getName());
            todoFromRepo.setDeadLine(todo.getDeadLine());
            todoFromRepo.setPriority(todo.getPriority());

            todoRepository.save(todoFromRepo);

            responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> deleteTodo(String todoId) {
        if (ObjectUtils.isEmpty(todoId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The given id was null!");
        }

        todoRepository.deleteById(todoId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
