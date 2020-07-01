package com.todo.todoapp.service.impl;

import com.todo.todoapp.model.todo.Priority;
import com.todo.todoapp.model.todo.Todo;
import com.todo.todoapp.model.todo.builder.TodoBuilder;
import com.todo.todoapp.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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

    private static Object[][] getTodosDataProvider() {
        return new Object[][] {
                {Collections.emptyList(), ResponseEntity.ok(Collections.emptyList()), 1},
                {TODO_LIST, ResponseEntity.ok(TODO_LIST), 1}
        };
    }

    @ParameterizedTest
    @MethodSource("getTodosDataProvider")
    public void getTodosTest(List<Todo> todoList, ResponseEntity<Object> expectedResponseEntity, int expectedNumberOfInvocations) {
        // GIVEN

        // WHEN
        when(todoRepository.findAll()).thenReturn(todoList);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(expectedResponseEntity, todoService.getTodos());

        // VERIFY
        verify(todoRepository, times(expectedNumberOfInvocations)).findAll();
    }

    /*
        getTodo()
     */

    private static Object[][] getTodoDataProvider() {
        return new Object[][] {
                {null, ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), 0},
                {EMPTY_STRING, ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), 0}
        };
    }

    @ParameterizedTest
    @MethodSource("getTodoDataProvider")
    public void getTodoTest(String todoId, ResponseEntity<Object> expectedResponseEntity, int expectedNumberOfInvocations) {
        // GIVEN

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(expectedResponseEntity, todoService.getTodo(todoId));

        // VERIFY
        verify(todoRepository, times(expectedNumberOfInvocations)).findById(anyString());
    }

    private static Object[][] getTodoWithMockingDataProvider() {
        return new Object[][] {
                {TODO_ID_FOUR, Optional.empty(), ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID), 1},
                {TODO_ID_ONE, Optional.of(TODO_LIST.get(0)), ResponseEntity.ok(TODO_LIST.get(0)), 1}
        };
    }

    @ParameterizedTest
    @MethodSource("getTodoWithMockingDataProvider")
    public void getTodoWithMockingTest(String todoId, Optional<Todo> optionalStoredTodo, ResponseEntity<Object> expectedResponseEntity, int expectedNumberOfInvocations) {
        // GIVEN

        // WHEN
        when(todoRepository.findById(todoId)).thenReturn(optionalStoredTodo);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(expectedResponseEntity, todoService.getTodo(todoId));

        // VERIFY
        verify(todoRepository, times(expectedNumberOfInvocations)).findById(anyString());
    }

    /*
        saveTodo()
     */

    private static Object[][] saveTodoConstraintViolationExceptionDataProvider() {
        return new Object[][] {
                {TODO_MAP.get(KEY_EMPTY_TODO)},
                {TODO_MAP.get(KEY_TODO_WITH_EMPTY_NAME)},
                {TODO_MAP.get(KEY_TODO_WITHOUT_PRIORITY)}
        };
    }

    @ParameterizedTest
    @MethodSource("saveTodoConstraintViolationExceptionDataProvider")
    public void saveTodoConstraintViolationExceptionTest(Todo todoFromJSON) {
        // GIVEN
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(todoFromJSON);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

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

    private static Object[][] updateTodoDataProvider() {
        return new Object[][] {
                {null, TODO_LIST.get(0), ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID)},
                {TODO_ID_ONE, null, ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_JSON)},
                {EMPTY_STRING, TODO_LIST.get(0), ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID)}
        };
    }

    @ParameterizedTest
    @MethodSource("updateTodoDataProvider")
    public void updateTodoTest(String todoId, Todo todoFromJSON, ResponseEntity<Object> expectedResponseEntity) {
        // GIVEN

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(expectedResponseEntity, todoService.updateTodo(todoId, todoFromJSON));

        // VERIFY
        verify(todoRepository, times(0)).findById(anyString());
        verify(todoRepository, times(0)).save(any(Todo.class));
    }

    private static Object[][] updateTodoConstraintViolationExceptionDataProvider() {
        return new Object[][] {
                {TODO_MAP.get(KEY_EMPTY_TODO)},
                {TODO_MAP.get(KEY_TODO_WITH_EMPTY_NAME)},
                {TODO_MAP.get(KEY_TODO_WITHOUT_PRIORITY)}
        };
    }

    @ParameterizedTest
    @MethodSource("updateTodoConstraintViolationExceptionDataProvider")
    public void updateTodoConstraintViolationExceptionTest(Todo todoFromJSON) {
        // GIVEN
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(todoFromJSON);
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

    private static Object[][] deleteTodoDataProvider() {
        return new Object[][] {
                {null},
                {EMPTY_STRING}
        };
    }

    @ParameterizedTest
    @MethodSource("deleteTodoDataProvider")
    public void deleteTodoTest(String todoId) {
        // GIVEN

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.deleteTodo(todoId));

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
