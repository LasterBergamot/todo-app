package com.todo.todoapp.service.impl;

import com.mongodb.MongoWriteException;
import com.todo.todoapp.model.todo.Priority;
import com.todo.todoapp.model.todo.Todo;
import com.todo.todoapp.model.todo.builder.TodoBuilder;
import com.todo.todoapp.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.todo.todoapp.util.TodoConstants.ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID;
import static com.todo.todoapp.util.TodoConstants.ERR_MSG_NULL_JSON;
import static com.todo.todoapp.util.TodoConstants.ERR_MSG_NULL_OR_EMPTY_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TodoServiceTest {

    private static final String TODO_ID_ONE = "1";
    private static final String TODO_NAME_ONE = "Todo #1";

    private static final String TODO_ID_TWO = "2";
    private static final String TODO_NAME_TWO = "Todo #2";

    private static final String TODO_ID_THREE = "3";
    private static final String TODO_NAME_THREE = "Todo #3";

    private static final String TODO_ID_FOUR = "4";

    private static final String EMPTY_STRING = "";

    private static final List<Todo> TODO_LIST = List.of(
            new TodoBuilder()
                    .withId(TODO_ID_ONE)
                    .withName(TODO_NAME_ONE)
                    .withPriority(Priority.SMALL)
                    .build(),
            new TodoBuilder()
                    .withId(TODO_ID_TWO)
                    .withName(TODO_NAME_TWO)
                    .withDeadline(LocalDate.now())
                    .withPriority(Priority.MEDIUM).build(),
            new TodoBuilder()
                    .withId(TODO_ID_THREE)
                    .withName(TODO_NAME_THREE)
                    .withPriority(Priority.BIG)
                    .build()
    );

    private static final String KEY_EMPTY_TODO = "EmptyTodo";
    private static final String KEY_TODO_WITHOUT_NAME = "TodoWithoutName";
    private static final String KEY_TODO_WITH_EMPTY_NAME = "TodoWithEmptyName";
    private static final String KEY_TODO_WITHOUT_PRIORITY = "TodoWithoutPriority";
    private static final String KEY_NON_EXISTING_TODO = "NonExistingTodo";
    private static final String KEY_TODO_FOR_UPDATING = "TodoForUpdating";

    private static final Map<String, Todo> TODO_MAP = Map.of(
            KEY_EMPTY_TODO, new TodoBuilder().build(),
            KEY_TODO_WITHOUT_NAME, new TodoBuilder()
                    .withId(TODO_ID_ONE)
                    .withDeadline(LocalDate.now())
                    .withPriority(Priority.SMALL)
                    .build(),
            KEY_TODO_WITH_EMPTY_NAME, new TodoBuilder()
                    .withId(TODO_ID_ONE)
                    .withName(EMPTY_STRING)
                    .withDeadline(LocalDate.now())
                    .withPriority(Priority.MEDIUM)
                    .build(),
            KEY_TODO_WITHOUT_PRIORITY, new TodoBuilder()
                    .withId(TODO_ID_ONE)
                    .withName(TODO_NAME_ONE)
                    .withDeadline(LocalDate.now())
                    .build(),
            KEY_NON_EXISTING_TODO, new TodoBuilder()
                    .withId(TODO_ID_FOUR)
                    .withName(EMPTY_STRING)
                    .withDeadline(LocalDate.now())
                    .withPriority(Priority.BIG)
                    .build(),
            KEY_TODO_FOR_UPDATING, new TodoBuilder()
                    .withId(TODO_ID_ONE)
                    .withName(TODO_NAME_TWO)
                    .withDeadline(LocalDate.now())
                    .withPriority(Priority.MEDIUM)
                    .build()
    );

    private TodoService todoService;

    private TodoRepository todoRepository;
    private MongoTemplate mongoTemplate;

    private Validator validator;


    @BeforeEach
    public void setUp() {
        todoRepository = mock(TodoRepository.class);
        mongoTemplate = mock(MongoTemplate.class);
    }

    /*
        getTodos()
     */

    @Test
    public void test_getTodosShouldReturnAnEmptyList_WhenThereAreNoRecordsInTheDatabase() {
        // GIVEN
        List<Todo> noTodos = Collections.emptyList();

        // WHEN
        when(todoRepository.findAll()).thenReturn(noTodos);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.ok(noTodos), todoService.getTodos());

        // VERIFY
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    public void test_getTodosShouldReturnAllOfTheRecords_WhenTheDatabaseIsNotEmpty() {
        // GIVEN
        List<Todo> returnedTodos = TODO_LIST;

        // WHEN
        when(todoRepository.findAll()).thenReturn(returnedTodos);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.ok(returnedTodos), todoService.getTodos());

        // VERIFY
        verify(todoRepository, times(1)).findAll();
    }

    /*
        getTodo()
     */

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoIdIsNull() {
        // GIVEN
        String nullTodoId = null;

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.getTodo(nullTodoId));

        // VERIFY
        verify(todoRepository, times(0)).findById(nullTodoId);
    }

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoIdIsEmpty() {
        // GIVEN
        String emptyTodoId = EMPTY_STRING;

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.getTodo(emptyTodoId));

        // VERIFY
        verify(todoRepository, times(0)).findById(anyString());
    }

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithNotFoundAndWithTheAppropriateErrorMessage_WhenNoTodoExistsWithTheGivenId() {
        // GIVEN
        String nonExistingTodoId = TODO_ID_FOUR;

        // WHEN
        when(todoRepository.findById(TODO_ID_FOUR)).thenReturn(Optional.empty());

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID), todoService.getTodo(nonExistingTodoId));

        // VERIFY
        verify(todoRepository, times(1)).findById(anyString());
    }

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithOk_WhenTheDesiredTodoCanBeRetrieved() {
        // GIVEN
        String todoId = TODO_ID_ONE;
        Todo storedTodo = TODO_LIST.get(0);
        Optional<Todo> optionalStoredTodo = Optional.of(storedTodo);

        // WHEN
        when(todoRepository.findById(todoId)).thenReturn(optionalStoredTodo);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.ok(storedTodo), todoService.getTodo(todoId));

        // VERIFY
        verify(todoRepository, times(1)).findById(anyString());
    }

    /*
        saveTodo()
     */

    @Test
    public void test_saveTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoFromJSONIsNull() {
        // GIVEN
        Todo nullTodoFromJSON = null;

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_JSON), todoService.saveTodo(nullTodoFromJSON));

        // VERIFY
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenTheGivenTodoFromJSONIsEmpty() {
        // GIVEN
        Todo emptyTodoFromJSON = TODO_MAP.get(KEY_EMPTY_TODO);
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(emptyTodoFromJSON);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenTheNameFieldIsEmptyInTheGivenTodoFromJSON() {
        // GIVEN
        Todo todoFromJSONWithEmptyName = TODO_MAP.get(KEY_TODO_WITH_EMPTY_NAME);
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(todoFromJSONWithEmptyName);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldINullInTheGivenTodoFromJSON() {
        // GIVEN
        Todo todoFromJSONWithoutPriority = TODO_MAP.get(KEY_TODO_WITHOUT_PRIORITY);
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(todoFromJSONWithoutPriority);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Disabled
    @Test
    public void test_saveTodoShouldThrowAMongoWriteException_WhenARecordAlreadyExistsWithTheSameName() {
        // GIVEN
        Todo todo = TODO_LIST.get(0);

        // WHEN
        when(todoRepository.save(todo)).thenReturn(todo);
        when(todoRepository.save(todo)).thenThrow(MongoWriteException.class);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(todo), todoService.saveTodo(todo));
        assertThrows(MongoWriteException.class, () -> todoService.saveTodo(todo));

        // VERIFY

    }

    @Test
    public void test_saveTodoShouldReturnAResponseEntityWithCreated_WhenTheGivenTodoFromJSONIsValid() {
        // GIVEN
        Todo todoFromJSON = TODO_LIST.get(0);

        // WHEN
        when(todoRepository.save(todoFromJSON)).thenReturn(todoFromJSON);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(todoFromJSON), todoService.saveTodo(todoFromJSON));

        // VERIFY
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    /*
        updateTodo()
     */

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoIdIsNull() {
        // GIVEN
        String nullTodoId = null;
        Todo todoFromJSON = TODO_LIST.get(0);

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.updateTodo(nullTodoId, todoFromJSON));

        // VERIFY
        verify(todoRepository, times(0)).findById(anyString());
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoFromJSONIsNull() {
        // GIVEN
        String todoId = TODO_ID_ONE;
        Todo nullTodoFromJSON = null;

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_JSON), todoService.updateTodo(todoId, nullTodoFromJSON));

        // VERIFY
        verify(todoRepository, times(0)).findById(anyString());
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoIdIsEmpty() {
        // GIVEN
        String emptyTodoId = EMPTY_STRING;
        Todo todoFromJSON = TODO_LIST.get(0);

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.updateTodo(emptyTodoId, todoFromJSON));

        // VERIFY
        verify(todoRepository, times(0)).findById(anyString());
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenTodoFromJSONIsEmpty() {
        // GIVEN
        Todo emptyTodoFromJSON = TODO_MAP.get(KEY_EMPTY_TODO);
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(emptyTodoFromJSON);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).findById(anyString());
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenTheNameFieldIsEmptyInTodoFromJSON() {
        // GIVEN
        Todo todoFromJSONWithEmptyName = TODO_MAP.get(KEY_TODO_WITH_EMPTY_NAME);
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(todoFromJSONWithEmptyName);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).findById(anyString());
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldIsEmptyInTodoFromJSON() {
        // GIVEN
        Todo todoFromJSONWithoutPriority = TODO_MAP.get(KEY_TODO_WITHOUT_PRIORITY);
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(todoFromJSONWithoutPriority);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).findById(anyString());
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithNotFoundAndWithTheAppropriateErrorMessage_WhenNoTodoExistsWithTheGivenId() {
        // GIVEN
        String nonExistingTodoId = TODO_ID_FOUR;
        Todo todoFromJSON = TODO_MAP.get(KEY_NON_EXISTING_TODO);

        // WHEN
        when(todoRepository.findById(nonExistingTodoId)).thenReturn(Optional.empty());

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID), todoService.updateTodo(nonExistingTodoId, todoFromJSON));

        // VERIFY
        verify(todoRepository, times(1)).findById(anyString());
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithCreated_WhenTheDesiredTodoCanBeUpdated() {
        // GIVEN
        String todoId = TODO_ID_ONE;
        Todo todoFromJSON = TODO_MAP.get(KEY_TODO_FOR_UPDATING);
        Todo storedTodo = TODO_LIST.get(0);

        // WHEN
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(storedTodo));
        when(todoRepository.save(todoFromJSON)).thenReturn(todoFromJSON);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(todoFromJSON), todoService.updateTodo(todoId, todoFromJSON));

        // VERIFY
        verify(todoRepository, times(1)).findById(anyString());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    /*
        deleteTodo()
     */

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenIdIsNull() {
        // GIVEN
        String nullTodoId = null;

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.deleteTodo(nullTodoId));

        // VERIFY
        verify(todoRepository, times(0)).findById(anyString());
        verify(todoRepository, times(0)).deleteById(anyString());
    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenIdIsEmpty() {
        // GIVEN
        String emptyTodoId = EMPTY_STRING;

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.deleteTodo(emptyTodoId));

        // VERIFY
        verify(todoRepository, times(0)).findById(anyString());
        verify(todoRepository, times(0)).deleteById(anyString());
    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithNotFoundAndWithTheAppropriateErrorMessage_WhenNoTodoExistsWithTheGivenId() {
        // GIVEN
        String nonExistingTodoId = TODO_ID_FOUR;

        // WHEN
        when(todoRepository.findById(nonExistingTodoId)).thenReturn(Optional.empty());

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID), todoService.deleteTodo(nonExistingTodoId));

        // VERIFY
        verify(todoRepository, times(1)).findById(anyString());
        verify(todoRepository, times(0)).deleteById(anyString());
    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithOk_WhenTheDesiredTodoHasBeenDeleted() {
        // GIVEN
        Todo storedTodo = TODO_LIST.get(0);
        String todoId = storedTodo.getId();

        // WHEN
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(storedTodo));
        doNothing().when(todoRepository).deleteById(todoId);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(new ResponseEntity<>(HttpStatus.OK), todoService.deleteTodo(todoId));

        // VERIFY
        verify(todoRepository, times(1)).findById(anyString());
        verify(todoRepository, times(1)).deleteById(anyString());
    }

    private void createValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
}
