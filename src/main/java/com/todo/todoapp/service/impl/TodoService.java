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

    private static final String ERROR_MESSAGE_NULL_ID = "The given id was null!";
    private static final String ERROR_MESSAGE_NULL_OR_EMPTY_JSON = "The given JSON was null or empty!";
    private static final String ERROR_MESSAGE_NOT_EXISTING_ID = "No Todo was found with the given id!";

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
            return getResponseEntityForNullId();
        }

        Optional<Todo> optionalTodo = todoRepository.findById(todoId);

        return optionalTodo
                .<ResponseEntity<Object>>map(todo -> ResponseEntity.status(HttpStatus.OK).body(todo))
                .orElseGet(this::getResponseEntityForNonExistingId);
    }

    @Override
    public ResponseEntity<Object> saveTodo(Todo todoFromJSON) {
        if (ObjectUtils.isEmpty(todoFromJSON)) {
            return getResponseEntityForEmptyOrNullJSON();
        }

        todoRepository.save(todoFromJSON);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> updateTodo(String todoId, Todo todoFromJSON) {
        if (ObjectUtils.isEmpty(todoId)) {
            return getResponseEntityForNullId();
        } else if (ObjectUtils.isEmpty(todoFromJSON)) {
            return getResponseEntityForEmptyOrNullJSON();
        }

        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        ResponseEntity<Object> responseEntity = getResponseEntityForNonExistingId();

        if (optionalTodo.isPresent()) {
            todoRepository.save(updateTodo(optionalTodo.get(), todoFromJSON));

            responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
        }

        return responseEntity;
    }

    //TODO: create a util class for this?
    //TODO: create a new Todo object instead of modifying the already existing one?
    private Todo updateTodo(Todo todoFromRepo, Todo todoFromJSON) {
        todoFromRepo.setName(todoFromJSON.getName());
        todoFromRepo.setDeadLine(todoFromJSON.getDeadLine());
        todoFromRepo.setPriority(todoFromJSON.getPriority());

        return todoFromRepo;
    }

    @Override
    public ResponseEntity<Object> deleteTodo(String todoId) {
        if (ObjectUtils.isEmpty(todoId)) {
            return getResponseEntityForNullId();
        }

        todoRepository.deleteById(todoId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<Object> getResponseEntityForNullId() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE_NULL_ID);
    }

    private ResponseEntity<Object> getResponseEntityForEmptyOrNullJSON() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE_NULL_OR_EMPTY_JSON);
    }

    private ResponseEntity<Object> getResponseEntityForNonExistingId() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERROR_MESSAGE_NOT_EXISTING_ID);
    }
}
