package com.todo.todoapp.service.impl;

import com.todo.todoapp.model.todo.Todo;
import com.todo.todoapp.repository.TodoRepository;
import com.todo.todoapp.service.ITodoService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class TodoService implements ITodoService {

    private static final String ERROR_MESSAGE_NULL_ID = "The given id was null!";
    private static final String ERROR_MESSAGE_NULL_OR_EMPTY_JSON = "The given JSON was null or empty!";
    private static final String ERROR_MESSAGE_NOT_EXISTING_ID = "No Todo was found with the given id!";

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoService.class);

    private final TodoRepository todoRepository;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public TodoService(TodoRepository todoRepository, MongoTemplate mongoTemplate) {
        this.todoRepository = todoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void initIndexes() {
        mongoTemplate
                .indexOps("Todo")
                .ensureIndex(
                        new Index()
                                .on("name", Sort.DEFAULT_DIRECTION)
                                .named("Todo_name_index")
                                .unique()
                );
    }

    /**
     * Return with all of the Todos found in the database.
     *
     * @return - a ResponseEntity with all of the Todos from the database, with all of their fields.
     */
    @Override
    public ResponseEntity<List<Todo>> getTodos() {
        LOGGER.info("Getting all Todos from the database!");

        return new ResponseEntity<>(todoRepository.findAll(), HttpStatus.OK);
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
            return getResponseEntityForNullId();
        }

        Optional<Todo> optionalTodo = todoRepository.findById(todoId);

        LOGGER.info("Getting Todo from the database!");

        return optionalTodo
                .<ResponseEntity<Object>>map(todo -> ResponseEntity.status(HttpStatus.OK).body(todo))
                .orElseGet(this::getResponseEntityForNonExistingId);
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
            return getResponseEntityForEmptyOrNullJSON();
        }

        LOGGER.info("Saving Todo into the database!");

        return new ResponseEntity<>(todoRepository.save(todoFromJSON), HttpStatus.CREATED);
    }

    /**
     * Updates the TodoObject, given by its ID, with the given TodoObject, if it's valid.
     *
     * @param todoId - the desired TodoObject to be updated
     * @param todoFromJSON - the TodoObject used to update the already existing TodoObject
     * @return - a ResponseEntity with HttpStatus.BAD_REQUEST (400) if the given ID- or the given TodoFromJSON object is null,
     *           a ResponseEntity with HttpStatus.NOT_FOUND (404) if with the given ID no TodoObject was found,
     *           else a ResponseEntity with HttpStatus.CREATED (201) with the updated TodoObject
     */
    @Override
    public ResponseEntity<Object> updateTodo(String todoId, @Valid Todo todoFromJSON) {
        if (ObjectUtils.isEmpty(todoId)) {
            return getResponseEntityForNullId();
        } else if (ObjectUtils.isEmpty(todoFromJSON)) {
            return getResponseEntityForEmptyOrNullJSON();
        }

        LOGGER.info("Updating Todo!");

        Optional<Todo> optionalTodo = todoRepository.findById(todoId);

        return optionalTodo.isEmpty()
                ? getResponseEntityForNonExistingId()
                : ResponseEntity.status(HttpStatus.CREATED).body(todoRepository.save(updateTodo(optionalTodo.get(), todoFromJSON)));
    }

    //TODO: create a util class for this?
    //TODO: create a new Todo object instead of modifying the already existing one?
    private Todo updateTodo(Todo todoFromRepo, Todo todoFromJSON) {
        todoFromRepo.setName(todoFromJSON.getName());
        todoFromRepo.setDeadline(todoFromJSON.getDeadline());
        todoFromRepo.setPriority(todoFromJSON.getPriority());

        return todoFromRepo;
    }

    @Override
    public ResponseEntity<Object> deleteTodo(String todoId) {
        if (ObjectUtils.isEmpty(todoId)) {
            return getResponseEntityForNullId();
        }

        LOGGER.info("Deleting Todo from the database!");

        todoRepository.deleteById(todoId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<Object> getResponseEntityForNullId() {
        LOGGER.error(ERROR_MESSAGE_NULL_ID);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE_NULL_ID);
    }

    private ResponseEntity<Object> getResponseEntityForEmptyOrNullJSON() {
        LOGGER.error(ERROR_MESSAGE_NULL_OR_EMPTY_JSON);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE_NULL_OR_EMPTY_JSON);
    }

    private ResponseEntity<Object> getResponseEntityForNonExistingId() {
        LOGGER.error(ERROR_MESSAGE_NOT_EXISTING_ID);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERROR_MESSAGE_NOT_EXISTING_ID);
    }
}
