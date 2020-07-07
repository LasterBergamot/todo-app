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

import static com.todo.todoapp.util.Constants.ATTRIBUTE_EMAIL;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_ID;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_LOGIN;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_NAME;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_SUB;
import static com.todo.todoapp.util.Constants.COLLECTION_NAME_USER;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_GIVEN_PRINCIPAL_IS_NULL;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_GIVEN_USER_COULD_NOT_BE_SAVED_TO_ANY_AVAILABLE_SERVICE;
import static com.todo.todoapp.util.Constants.INDEX_NAME_USER_GITHUB_ID_INDEX;
import static com.todo.todoapp.util.Constants.INDEX_NAME_USER_GOOGLE_ID_INDEX;
import static com.todo.todoapp.util.Constants.KEY_GITHUB_ID;
import static com.todo.todoapp.util.Constants.KEY_GOOGLE_ID;

@Service
public class UserService implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final MongoUtil mongoUtil;

    @Autowired
    public UserService(UserRepository userRepository, MongoUtil mongoUtil) {
        this.userRepository = userRepository;
        this.mongoUtil = mongoUtil;
    }

    @PostConstruct
    public void initIndexes() {
        mongoUtil.createIndex("Creating index for the 'github_id' field of User.", COLLECTION_NAME_USER, KEY_GITHUB_ID, INDEX_NAME_USER_GITHUB_ID_INDEX);
        mongoUtil.createIndex("Creating index for the 'google_id' field of User.", COLLECTION_NAME_USER, KEY_GOOGLE_ID, INDEX_NAME_USER_GOOGLE_ID_INDEX);
    }

    @Override
    public String getUsername(OAuth2User principal) {
        checkPrincipal(principal);
        checkPrincipalAttributes(principal);

        return principal instanceof OidcUser
                ? principal.getAttribute(ATTRIBUTE_NAME).toString()
                : principal.getAttribute(ATTRIBUTE_LOGIN).toString();
    }

    private void checkPrincipal(OAuth2User principal) {
        if (principal == null) {
            LOGGER.error("The given principal was null");

            throw new IllegalArgumentException(ERR_MSG_THE_GIVEN_PRINCIPAL_IS_NULL);
        }
    }

    private void checkPrincipalAttributes(OAuth2User principal) {
        if (principal.getAttribute(ATTRIBUTE_NAME) == null) {
            throw new IllegalArgumentException("The principal's name attribute is null!");
        } else if (principal.getAttribute(ATTRIBUTE_LOGIN) == null) {
            throw new IllegalArgumentException("The principal's login attribute is null!");
        }

        if (principal instanceof OidcUser) {
            checkGoogleAttributes((OidcUser) principal);
        } else if (principal instanceof DefaultOAuth2User) {
            checkGithubAttributes((DefaultOAuth2User) principal);
        }
    }

    private void checkGoogleAttributes(OidcUser oidcUser) {
        if (oidcUser.getAttribute(ATTRIBUTE_SUB) == null) {
            throw new IllegalArgumentException("The principal's sub attribute is null!");
        } else if (oidcUser.getAttribute(ATTRIBUTE_EMAIL) == null) {
            throw new IllegalArgumentException("The principal's email attribute is null!");
        }
    }

    private void checkGithubAttributes(DefaultOAuth2User defaultOAuth2User) {
        if (defaultOAuth2User.getAttribute(ATTRIBUTE_ID) == null) {
            throw new IllegalArgumentException("The principal's id attribute is null!");
        } else if (defaultOAuth2User.getAttribute(ATTRIBUTE_EMAIL) == null) {
            throw new IllegalArgumentException("The principal's email attribute is null!");
        }
    }

    @Override
    public User handleUser(OAuth2User principal) {
        checkPrincipal(principal);

        User returnedUser = handleLogin(principal);

        if (returnedUser == null) {
            throw new IllegalArgumentException(ERR_MSG_THE_GIVEN_USER_COULD_NOT_BE_SAVED_TO_ANY_AVAILABLE_SERVICE);
        }

        return returnedUser;
    }

    private User handleLogin(OAuth2User principal) {
        User returnedUser = null;

        if (principal instanceof OidcUser) {
            returnedUser = handleGoogleLogin((OidcUser) principal);
        } else if (principal instanceof DefaultOAuth2User) {
            returnedUser = handleGithubLogin((DefaultOAuth2User) principal);
        }

        return returnedUser;
    }

    private User handleGoogleLogin(OidcUser oidcUser) {
        User returnedUser;
        String id = oidcUser.getAttribute(ATTRIBUTE_SUB).toString();
        String email = oidcUser.getAttribute(ATTRIBUTE_EMAIL).toString();

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
        String id = defaultOAuth2User.getAttribute(ATTRIBUTE_ID).toString();
        String email = defaultOAuth2User.getAttribute(ATTRIBUTE_EMAIL).toString();

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
