package com.todo.todoapp.service;

import com.todo.todoapp.model.todo.Todo;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

public interface ITodoService {

    ResponseEntity<List<Todo>> getTodos();
    ResponseEntity<Object> getTodo(String todoId);
    ResponseEntity<Object> saveTodo(@Valid Todo todoFromJSON);
    ResponseEntity<Object> updateTodo(String todoId, @Valid Todo todoFromJSON);
    ResponseEntity<Object> deleteTodo(String todoId);
}
