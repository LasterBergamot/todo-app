package com.todo.todoapp.repository;

import com.todo.todoapp.model.todo.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "todos", path = "todos")
public interface TodoRepository extends MongoRepository<Todo, String> {

    List<Todo> findByPriority(@Param("priority") String priority);
}
