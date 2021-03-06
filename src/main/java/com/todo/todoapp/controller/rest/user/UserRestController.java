package com.todo.todoapp.controller.rest.user;

import com.todo.todoapp.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    private static final String GET_MAPPING_USERNAME = "/username";
    private static final String GET_MAPPING_HANDLE_USER = "/handleUser";

    private static final String KEY_NAME = "name";
    private static final String KEY_USER = "user";

    private static final String PRE_AUTHORIZE_ROLE_USER = "hasRole('ROLE_USER')";

    private final IUserService userService;

    @Autowired
    public UserRestController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(GET_MAPPING_USERNAME)
    public Map<String, Object> getUsername(@AuthenticationPrincipal OAuth2User principal) {
        LOGGER.info("Getting username!");

        return Collections.singletonMap(KEY_NAME, userService.getUsername(principal));
    }

    //TODO: should be a POST method
    @PreAuthorize(PRE_AUTHORIZE_ROLE_USER)
    @GetMapping(GET_MAPPING_HANDLE_USER)
    public Map<String, Object> handleUser(@AuthenticationPrincipal OAuth2User principal) {
        LOGGER.info("Handling user!");

        return Collections.singletonMap(KEY_USER, userService.handleUser(principal));
    }
}
