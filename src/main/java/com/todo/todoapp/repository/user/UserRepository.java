package com.todo.todoapp.repository.user;

import com.todo.todoapp.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

//    @Query("SELECT u from User u WHERE u.github_id = ?1")
    User findUserByGithubId(String githubId);

    User findUserByGoogleId(String googleId);
}
