package com.todo.todoapp.service.user.impl;

import com.todo.todoapp.model.user.User;
import com.todo.todoapp.repository.user.UserRepository;
import com.todo.todoapp.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Service
public class UserService implements IUserService {

    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private static final String COLLECTION_NAME_USER = "User";

    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_SUB = "sub";
    private static final String ATTRIBUTE_EMAIL = "email";
    private static final String ATTRIBUTE_ID = "id";

    private final UserRepository userRepository;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserService(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void initIndexes() {
        LOGGER.info("Creating index for the 'github_id' field of User.");

        mongoTemplate
                .indexOps(COLLECTION_NAME_USER)
                .ensureIndex(
                        new Index()
                                .on("github_id", Sort.DEFAULT_DIRECTION)
                                .named("User_github_id_index")
                                .unique()
                );

        LOGGER.info("Creating index for the 'google_id' field of User.");

        mongoTemplate
                .indexOps(COLLECTION_NAME_USER)
                .ensureIndex(
                        new Index()
                                .on("google_id", Sort.DEFAULT_DIRECTION)
                                .named("User_google_id_index")
                                .unique()
                );
    }

    @Override
    public String handleUser(OAuth2User principal) {
        String name =  Objects.requireNonNull(principal.getAttribute(ATTRIBUTE_NAME)).toString();

        if (principal instanceof OidcUser) {
            handleGoogleLogin((OidcUser) principal);
        } else if (principal instanceof DefaultOAuth2User) {
            handleGithubLogin((DefaultOAuth2User) principal);
        }

        return name;
    }

    private void handleGoogleLogin(OidcUser oidcUser) {
        String id = Objects.requireNonNull(oidcUser.getAttribute(ATTRIBUTE_SUB)).toString();
        String email = Objects.requireNonNull(oidcUser.getAttribute(ATTRIBUTE_EMAIL)).toString();

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
    }

    private void handleGithubLogin(DefaultOAuth2User defaultOAuth2User) {
        String id = Objects.requireNonNull(defaultOAuth2User.getAttribute(ATTRIBUTE_ID)).toString();
        String email = Objects.requireNonNull(defaultOAuth2User.getAttribute(ATTRIBUTE_EMAIL)).toString();

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
}
