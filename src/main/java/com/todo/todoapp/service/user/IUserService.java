package com.todo.todoapp.service.user;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface IUserService {

    String handleUser(OAuth2User principal);
}
