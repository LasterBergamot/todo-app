package com.todo.todoapp.repository.todo;

import com.todo.todoapp.model.todo.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {

    @Query("SELECT t FROM Todo t WHERE t.user_id = ?1")
    List<Todo> getTodosOfUser(String userId);

}
