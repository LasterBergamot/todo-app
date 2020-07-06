package com.todo.todoapp.controller.rest.user;

import com.todo.todoapp.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    private static final String GET_MAPPING_USER = "/user";

    private static final String KEY_NAME = "name";

    private final IUserService userService;

    @Autowired
    public UserRestController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(GET_MAPPING_USER)
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        LOGGER.info("Handling user!");

        return Collections.singletonMap(KEY_NAME, userService.handleUser(principal));
    }
}
