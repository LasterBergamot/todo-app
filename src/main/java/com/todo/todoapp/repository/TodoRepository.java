package com.todo.todoapp.repository;

import com.todo.todoapp.model.todo.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "todos", path = "todos")
public interface TodoRepository extends MongoRepository<Todo, String> {}
