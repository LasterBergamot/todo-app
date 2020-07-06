package com.todo.todoapp.controller.rest.user;

import com.todo.todoapp.model.user.User;
import com.todo.todoapp.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserRestController {

    private final UserRepository userRepository;

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        String name = "";
        String id = "";

        // Google
        if (principal instanceof OidcUser) {
            OidcUser defaultOidcUser = (OidcUser) principal;

            name = defaultOidcUser.getAttribute("name");
            id = defaultOidcUser.getAttribute("sub");

            if (userRepository.findUserByGoogleId(id) == null) {
                User user = new User.Builder()
                        .withGoogleId(id)
                        .build();

                userRepository.save(user);
            }

            // Github
        } else if (principal instanceof DefaultOAuth2User) {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) principal;

            name = defaultOAuth2User.getAttribute("login");
            id = Objects.requireNonNull(defaultOAuth2User.getAttribute("id")).toString();

            if (userRepository.findUserByGithubId(id) == null) {
                User user = new User.Builder()
                        .withGithubId(id)
                        .build();

                userRepository.save(user);
            }
        }

        return Collections.singletonMap("name", name);
    }
}
