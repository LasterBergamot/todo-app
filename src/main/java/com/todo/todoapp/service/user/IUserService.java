package com.todo.todoapp.service.user;

import com.todo.todoapp.model.user.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface IUserService {

    String getUsername(OAuth2User principal);
    User saveUser(OAuth2User principal);
}
