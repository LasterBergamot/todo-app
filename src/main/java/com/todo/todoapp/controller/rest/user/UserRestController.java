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
        String id, email;
        String name = "";

        // Google
        if (principal instanceof OidcUser) {
            OidcUser defaultOidcUser = (OidcUser) principal;

            id = Objects.requireNonNull(defaultOidcUser.getAttribute("sub")).toString();
            name = Objects.requireNonNull(defaultOidcUser.getAttribute("name")).toString();
            email = Objects.requireNonNull(defaultOidcUser.getAttribute("email")).toString();

            if (userRepository.findByEmail(email) == null) {
                User user = new User.Builder()
                        .withEmail(email)
                        .withGoogleId(id)
                        .build();

                userRepository.save(user);
            } else {
                if (userRepository.findByGoogleId(id) == null) {
                    User savedUser = userRepository.findByEmail(email);
                    savedUser.setGoogleId(id);

                    userRepository.save(savedUser);
                }
            }

            // Github
        } else if (principal instanceof DefaultOAuth2User) {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) principal;

            id = Objects.requireNonNull(defaultOAuth2User.getAttribute("id")).toString();
            name = Objects.requireNonNull(defaultOAuth2User.getAttribute("login")).toString();
            email = Objects.requireNonNull(defaultOAuth2User.getAttribute("email")).toString();

            if (userRepository.findByEmail(email) == null) {
                User user = new User.Builder()
                        .withEmail(email)
                        .withGithubId(id)
                        .build();

                userRepository.save(user);
            } else {
                if (userRepository.findByGithubId(id) == null) {
                    User savedUser = userRepository.findByEmail(email);
                    savedUser.setGithubId(id);

                    userRepository.save(savedUser);
                }
            }
        }

        return Collections.singletonMap("name", name);
    }
}
