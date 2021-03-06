package com.todo.todoapp.service.todo.impl;

import com.todo.todoapp.model.todo.Todo;
import com.todo.todoapp.model.user.User;
import com.todo.todoapp.repository.todo.TodoRepository;
import com.todo.todoapp.repository.user.UserRepository;
import com.todo.todoapp.service.todo.ITodoService;
import com.todo.todoapp.util.MongoUtil;
import com.todo.todoapp.util.TodoUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.todo.todoapp.util.Constants.ATTRIBUTE_ID;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_SUB;
import static com.todo.todoapp.util.Constants.COLLECTION_NAME_TODO;
import static com.todo.todoapp.util.Constants.ERR_MSG_CURRENTLY_NO_USER_IS_LOGGED_IN;
import static com.todo.todoapp.util.Constants.ERR_MSG_CURRENTLY_THE_GIVEN_USER_DOES_NOT_EXIST_IN_THE_DATABASE;
import static com.todo.todoapp.util.Constants.ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID;
import static com.todo.todoapp.util.Constants.ERR_MSG_NULL_JSON;
import static com.todo.todoapp.util.Constants.ERR_MSG_NULL_OR_EMPTY_ID;
import static com.todo.todoapp.util.Constants.INDEX_NAME_TODO_NAME_INDEX;
import static com.todo.todoapp.util.Constants.KEY_NAME;

@Service
@Validated
public class TodoService implements ITodoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoService.class);

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final MongoUtil mongoUtil;

    @Autowired
    public TodoService(TodoRepository todoRepository, UserRepository userRepository, MongoUtil mongoUtil) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.mongoUtil = mongoUtil;
    }

    @PostConstruct
    public void initIndexes() {
        mongoUtil.createIndex("Creating index for the 'name' field of Todo.", COLLECTION_NAME_TODO, KEY_NAME, INDEX_NAME_TODO_NAME_INDEX);
    }

    /**
     * Return with all of the Todos found in the database.
     *
     * @return - a ResponseEntity with HttpStatus.OK (200) and all of the Todos from the database, with all of their fields.
     */
    @Override
    public ResponseEntity<List<Todo>> getTodos() {
        LOGGER.info("Getting all Todos from the database!");

        return ResponseEntity.ok(todoRepository.findAll());
    }

    /**
     * Return with all of the Todos for the given user.
     *
     * @param springSecurityContext - the object which holds the currently logged in user
     * @return - a ResponseEntity with all of the Todos for the given user
     *           if the given SecurityContext does not exist, it will return with a ResponseEntity with HttpStatus.BAD_REQUEST (400)
     *           if no user was found in the database, then it will also return with a ResponseEntity with HttpStatus.NOT_FOUND (404)
     */
    @Override
    public ResponseEntity<Object> getTodos(SecurityContext springSecurityContext) {
        if (springSecurityContext == null) {
            return getErrorSpecificResponseEntity(HttpStatus.BAD_REQUEST, ERR_MSG_CURRENTLY_NO_USER_IS_LOGGED_IN);
        }

        User user = getUserFromDatabase(springSecurityContext);

        if (user == null) {
            return getErrorSpecificResponseEntity(HttpStatus.NOT_FOUND, ERR_MSG_CURRENTLY_THE_GIVEN_USER_DOES_NOT_EXIST_IN_THE_DATABASE);
        }

        LOGGER.info("Getting Todos for the given user!");

        return ResponseEntity.ok(todoRepository.findByUserId(user.getId()));
    }

    private User getUserFromDatabase(SecurityContext springSecurityContext) {
        User user = null;
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) springSecurityContext.getAuthentication();
        OAuth2User principal = oAuth2AuthenticationToken.getPrincipal();

        if (principal instanceof OidcUser) {
            user = handleGoogle(principal);
        } else if (principal instanceof DefaultOAuth2User) {
            user = handleGithub(principal);
        }

        return user;
    }

    private User handleGoogle(OAuth2User principal) {
        OidcUser defaultOidcUser = (OidcUser) principal;
        String googleId = Objects.requireNonNull(defaultOidcUser.getAttribute(ATTRIBUTE_SUB)).toString();

        return userRepository.findByGoogleId(googleId);
    }

    private User handleGithub(OAuth2User principal) {
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) principal;
        String githubId = Objects.requireNonNull(defaultOAuth2User.getAttribute(ATTRIBUTE_ID)).toString();

        return userRepository.findByGithubId(githubId);
    }

    /**
     * Returns a ResponseEntity with the TodoObject if any exists with the given ID.
     *
     * @param todoId - the ID of the desired TodoObject
     * @return  - ResponseEntity with HttpStatus.OK (200) and the TodoObject if it exists,
     *            else a ResponseEntity with HttpStatus.NOT_FOUND (404)
     */
    @Override
    public ResponseEntity<Object> getTodo(String todoId) {
        if (ObjectUtils.isEmpty(todoId)) {
            return getErrorSpecificResponseEntity(HttpStatus.BAD_REQUEST, ERR_MSG_NULL_OR_EMPTY_ID);
        }

        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        ResponseEntity<Object> responseEntity;

        if (optionalTodo.isPresent()) {
            LOGGER.info("Getting Todo from the database!");

            responseEntity = ResponseEntity.ok(optionalTodo.get());
        } else {
            responseEntity = getErrorSpecificResponseEntity(HttpStatus.NOT_FOUND, ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID);
        }

        return responseEntity;
    }

    /**
     * Saves the given TodoObject into the database, if it's not null and it's valid.
     *
     * @param todoFromJSON - a valid TodoObject in JSON format
     * @return - a ResponseEntity with HttpStatus.CREATED (201) and with the saved TodoObject,
     *           else a ResponseEntity with HttpStatus.BAD_REQUEST (400)
     */
    @Override
    public ResponseEntity<Object> saveTodo(@Valid Todo todoFromJSON) {
        if (ObjectUtils.isEmpty(todoFromJSON)) {
            return getErrorSpecificResponseEntity(HttpStatus.BAD_REQUEST, ERR_MSG_NULL_JSON);
        }

        LOGGER.info("Saving Todo into the database!");

        return ResponseEntity.status(HttpStatus.CREATED).body(todoRepository.save(todoFromJSON));
    }

    /**
     * Updates the TodoObject, given by its ID, with the given TodoObject, if it's valid.
     *
     * @param todoId - the desired TodoObject to be updated
     * @param todoFromJSON - the TodoObject used to update the already existing TodoObject
     * @return - a ResponseEntity with HttpStatus.BAD_REQUEST (400) if the given ID- or the given TodoFromJSON object is null,
     *           a ResponseEntity with HttpStatus.NOT_FOUND (404) if with the given ID no TodoObject was found,
     *           else a ResponseEntity with HttpStatus.OK (200) with the updated TodoObject
     */
    @Override
    public ResponseEntity<Object> updateTodo(String todoId, @Valid Todo todoFromJSON) {
        if (ObjectUtils.isEmpty(todoId)) {
            return getErrorSpecificResponseEntity(HttpStatus.BAD_REQUEST, ERR_MSG_NULL_OR_EMPTY_ID);
        } else if (ObjectUtils.isEmpty(todoFromJSON)) {
            return getErrorSpecificResponseEntity(HttpStatus.BAD_REQUEST, ERR_MSG_NULL_JSON);
        }

        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        ResponseEntity<Object> responseEntity;

        if (optionalTodo.isPresent()) {
            Todo updatedTodo = TodoUtil.updateTodo(optionalTodo.get(), todoFromJSON);

            responseEntity = ResponseEntity.status(HttpStatus.OK).body(todoRepository.save(updatedTodo));
        } else {
            responseEntity = getErrorSpecificResponseEntity(HttpStatus.NOT_FOUND, ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID);
        }

        return responseEntity;
    }

    /**
     * Deletes the TodoObject from the database with the given ID.
     *
     * @param todoId - the ID of the TodoObject to be deleted
     * @return - a ResponseEntity with HttpStatus.BAD_REQUEST (400), if the given ID was null,
     *           a ResponseEntity with HttpStatus.NOT_FOUND (404), if no TodoObject was found with the given ID,
     *           else a ResponseEntity with HttpStatus.OK (200), if the TodoObject was successfully deleted.
     */
    @Override
    public ResponseEntity<Object> deleteTodo(String todoId) {
        if (ObjectUtils.isEmpty(todoId)) {
            return getErrorSpecificResponseEntity(HttpStatus.BAD_REQUEST, ERR_MSG_NULL_OR_EMPTY_ID);
        }

        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        ResponseEntity<Object> responseEntity;

        if (optionalTodo.isPresent()) {
            LOGGER.info("Deleting Todo from the database!");

            todoRepository.deleteById(todoId);
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } else {
            responseEntity = getErrorSpecificResponseEntity(HttpStatus.NOT_FOUND, ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID);
        }

        return responseEntity;
    }

    private ResponseEntity<Object> getErrorSpecificResponseEntity(HttpStatus httpStatus, String errorMessage) {
        LOGGER.error(errorMessage);

        return ResponseEntity.status(httpStatus).body(errorMessage);
    }
}
