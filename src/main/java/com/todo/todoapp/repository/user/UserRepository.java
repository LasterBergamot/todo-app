package com.todo.todoapp.repository.user;

import com.todo.todoapp.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
    User findByGithubId(String githubId);
    User findByGoogleId(String googleId);
}
