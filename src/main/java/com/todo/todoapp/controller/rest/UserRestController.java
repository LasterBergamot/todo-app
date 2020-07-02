package com.todo.todoapp.controller.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserRestController {

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        String name = (principal.getAuthorities().size() == 4)
                ? String.valueOf(((DefaultOidcUser) principal).getIdToken().getClaims().get("name"))
                : principal.getAttribute("login");

        return Collections.singletonMap("name", name);
    }
}
