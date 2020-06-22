package com.todo.todoapp.service;

import com.todo.todoapp.model.todo.Todo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITodoService {

    ResponseEntity<List<Todo>> getTodos();
    ResponseEntity<Object> getTodo(String todoId);
    ResponseEntity<Object> saveTodo(Todo todo);
    ResponseEntity<Object> updateTodo(String todoId, Todo todo);
    ResponseEntity<Object> deleteTodo(String todoId);
}
