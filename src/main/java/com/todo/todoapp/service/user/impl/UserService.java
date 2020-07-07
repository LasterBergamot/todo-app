package com.todo.todoapp.service.user.impl;

import com.todo.todoapp.model.user.User;
import com.todo.todoapp.repository.user.UserRepository;
import com.todo.todoapp.service.user.IUserService;
import com.todo.todoapp.util.MongoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Optional;

import static com.todo.todoapp.util.Constants.ATTRIBUTE_EMAIL;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_ID;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_LOGIN;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_NAME;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_SUB;
import static com.todo.todoapp.util.Constants.COLLECTION_NAME_USER;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_GIVEN_PRINCIPAL_WAS_NULL;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_GIVEN_USER_COULD_NOT_BE_SAVED_TO_ANY_AVAILABLE_SERVICE;
import static com.todo.todoapp.util.Constants.INDEX_NAME_USER_GITHUB_ID_INDEX;
import static com.todo.todoapp.util.Constants.INDEX_NAME_USER_GOOGLE_ID_INDEX;
import static com.todo.todoapp.util.Constants.KEY_GITHUB_ID;
import static com.todo.todoapp.util.Constants.KEY_GOOGLE_ID;

@Service
public class UserService implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initIndexes() {
        MongoUtil.createIndex("Creating index for the 'github_id' field of User.", COLLECTION_NAME_USER, KEY_GITHUB_ID, INDEX_NAME_USER_GITHUB_ID_INDEX);
        MongoUtil.createIndex("Creating index for the 'google_id' field of User.", COLLECTION_NAME_USER, KEY_GOOGLE_ID, INDEX_NAME_USER_GOOGLE_ID_INDEX);
    }

    @Override
    public String getUsername(OAuth2User principal) {
        checkPrincipal(principal);

        return principal instanceof OidcUser
                ? Objects.requireNonNull(principal.getAttribute(ATTRIBUTE_NAME)).toString()
                : Objects.requireNonNull(principal.getAttribute(ATTRIBUTE_LOGIN)).toString();
    }

    private void checkPrincipal(OAuth2User principal) {
        if (Optional.ofNullable(principal).isEmpty()) {
            LOGGER.error("The given principal was null");

            throw new IllegalArgumentException(ERR_MSG_THE_GIVEN_PRINCIPAL_WAS_NULL);
        }
    }

    @Override
    public User saveUser(OAuth2User principal) {
        checkPrincipal(principal);

        User returnedUser = null;

        if (principal instanceof OidcUser) {
            returnedUser = handleGoogleLogin((OidcUser) principal);
        } else if (principal instanceof DefaultOAuth2User) {
            returnedUser = handleGithubLogin((DefaultOAuth2User) principal);
        }

        if (returnedUser == null) {
            throw new IllegalArgumentException(ERR_MSG_THE_GIVEN_USER_COULD_NOT_BE_SAVED_TO_ANY_AVAILABLE_SERVICE);
        }

        return returnedUser;
    }

    private User handleGoogleLogin(OidcUser oidcUser) {
        User returnedUser;
        String id = Objects.requireNonNull(oidcUser.getAttribute(ATTRIBUTE_SUB)).toString();
        String email = Objects.requireNonNull(oidcUser.getAttribute(ATTRIBUTE_EMAIL)).toString();

        if (userRepository.findByEmail(email) == null) {
            User user = new User.Builder()
                    .withEmail(email)
                    .withGoogleId(id)
                    .build();

            returnedUser = userRepository.save(user);
        } else {
            returnedUser = userRepository.findByEmail(email);

            if (userRepository.findByGoogleId(id) == null) {
                returnedUser.setGoogleId(id);

                returnedUser = userRepository.save(returnedUser);
            }
        }

        return returnedUser;
    }

    private User handleGithubLogin(DefaultOAuth2User defaultOAuth2User) {
        User returnedUser;
        String id = Objects.requireNonNull(defaultOAuth2User.getAttribute(ATTRIBUTE_ID)).toString();
        String email = Objects.requireNonNull(defaultOAuth2User.getAttribute(ATTRIBUTE_EMAIL)).toString();

        if (userRepository.findByEmail(email) == null) {
            User user = new User.Builder()
                    .withEmail(email)
                    .withGithubId(id)
                    .build();

            returnedUser = userRepository.save(user);
        } else {
            returnedUser = userRepository.findByEmail(email);

            if (userRepository.findByGithubId(id) == null) {
                returnedUser.setGithubId(id);

                returnedUser = userRepository.save(returnedUser);
            }
        }

        return returnedUser;
    }
}
