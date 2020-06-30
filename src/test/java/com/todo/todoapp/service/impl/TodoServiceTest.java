package com.todo.todoapp.service.impl;

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

    public static final List<Todo> TODOS = List.of(
            new TodoBuilder()
                    .withId("1")
                    .withName("Todo #1")
                    .withPriority(Priority.SMALL)
                    .build(),
            new TodoBuilder()
                    .withId("2")
                    .withName("Todo #2")
                    .withDeadline(LocalDate.of(2020, 6, 22))
                    .withPriority(Priority.MEDIUM).build(),
            new TodoBuilder()
                    .withId("3")
                    .withName("Todo #3")
                    .withPriority(Priority.BIG)
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
        List<Todo> expectedTodos = Collections.emptyList();

        // WHEN
        when(todoRepository.findAll()).thenReturn(expectedTodos);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.ok(expectedTodos), todoService.getTodos());

        // VERIFY
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    public void test_getTodosShouldReturnAllOfTheRecords_WhenTheDatabaseIsNotEmpty() {
        // GIVEN

        // WHEN
        when(todoRepository.findAll()).thenReturn(TODOS);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.ok(TODOS), todoService.getTodos());

        // VERIFY
        verify(todoRepository, times(1)).findAll();
    }

    /*
        getTodo()
     */

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoIdIsNull() {
        // GIVEN

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.getTodo(null));

        // VERIFY
        verify(todoRepository, times(0)).findById(null);
    }

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoIdIsEmpty() {
        // GIVEN

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.getTodo(""));

        // VERIFY
        verify(todoRepository, times(0)).findById("");
    }

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithNotFoundAndWithTheAppropriateErrorMessage_WhenNoTodoExistsWithTheGivenId() {
        // GIVEN

        // WHEN
        when(todoRepository.findById("4")).thenReturn(Optional.empty());

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID), todoService.getTodo("4"));

        // VERIFY
        verify(todoRepository, times(1)).findById(anyString());
    }

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithOk_WhenTheDesiredTodoCanBeRetrieved() {
        // GIVEN

        // WHEN
        when(todoRepository.findById("1")).thenReturn(Optional.of(TODOS.get(0)));

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.ok(TODOS.get(0)), todoService.getTodo("1"));

        // VERIFY
        verify(todoRepository, times(1)).findById("1");
    }

    /*
        saveTodo()
     */

    @Test
    public void test_saveTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoFromJSONIsNull() {
        // GIVEN

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_JSON), todoService.saveTodo(null));

        // VERIFY
        verify(todoRepository, times(0)).save(null);
    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenTheGivenTodoFromJSONIsEmpty() {
        // GIVEN
        Todo emptyTodo = new TodoBuilder().build();
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(emptyTodo);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).save(emptyTodo);
    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenTheNameFieldIsEmptyInTheGivenTodoFromJSON() {
        // GIVEN
        Todo emptyTodo = new TodoBuilder().withId("1").withName("").withPriority(Priority.BIG).build();
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(emptyTodo);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).save(emptyTodo);
    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldINullInTheGivenTodoFromJSON() {
        // GIVEN
        Todo emptyTodo = new TodoBuilder().withId("1").withName("Todo #56").build();
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(emptyTodo);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).save(emptyTodo);
    }

    /*
        TODO: when the JSON request is sent, and the priority field's value is not valid, before reaching the RestController's method's body, a 404 error is sent back
         How can this be tested?
     */
    @Disabled
    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldIsNotAValidValueInTheGivenTodoFromJSON() {
        // GIVEN
        Todo emptyTodo = new TodoBuilder().withId("1").withName("Todo #56").withPriority(Priority.valueOf("")).build();
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(emptyTodo);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).save(emptyTodo);
    }

    @Disabled
    @Test
    public void test_saveTodoShouldThrowAMongoWriteException_WhenARecordAlreadyExistsWithTheSameName() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_saveTodoShouldReturnAResponseEntityWithCreated_WhenTheGivenTodoFromJSONIsValid() {
        // GIVEN
        Todo todoFromJSON = TODOS.get(0);

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
        Todo todoForUpdating = new TodoBuilder().withId("1").withName("Todo #88").withPriority(Priority.BIG).build();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.updateTodo(null, todoForUpdating));

        // VERIFY
        verify(todoRepository, times(0)).findById(null);
        verify(todoRepository, times(0)).save(todoForUpdating);
    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoFromJSONIsNull() {
        // GIVEN

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_JSON), todoService.updateTodo("1", null));

        // VERIFY
        verify(todoRepository, times(0)).findById("1");
        verify(todoRepository, times(0)).save(null);
    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoIdIsEmpty() {
        // GIVEN
        String todoId = "";
        Todo todoFromJSON = TODOS.get(0);

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.updateTodo(todoId, todoFromJSON));

        // VERIFY
        verify(todoRepository, times(0)).findById(todoId);
        verify(todoRepository, times(0)).save(todoFromJSON);
    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenTodoFromJSONIsEmpty() {
        // GIVEN
        String todoId = "1";
        Todo todoFromJSON = new TodoBuilder().build();
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(todoFromJSON);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).findById(todoId);
        verify(todoRepository, times(0)).save(todoFromJSON);
    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenTheNameFieldIsEmptyInTodoFromJSON() {
        // GIVEN
        String todoId = "1";
        Todo todoFromJSON = new TodoBuilder().withId(todoId).withName("").withPriority(Priority.BIG).build();
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(todoFromJSON);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).findById(todoId);
        verify(todoRepository, times(0)).save(todoFromJSON);
    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldIsEmptyInTodoFromJSON() {
        // GIVEN
        String todoId = "1";
        Todo todoFromJSON = new TodoBuilder().withId(todoId).withName("Todo #23").build();
        createValidator();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Set<ConstraintViolation<Todo>> todoViolations = validator.validate(todoFromJSON);
        assertFalse(todoViolations.isEmpty());

        // VERIFY
        verify(todoRepository, times(0)).findById(todoId);
        verify(todoRepository, times(0)).save(todoFromJSON);
    }

    /*
        TODO: when the JSON request is sent, and the priority field's value is not valid, before reaching the RestController's method's body, a 404 error is sent back
         How can this be tested?
     */
    @Disabled
    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldHasANonValidValueInTodoFromJSON() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithNotFoundAndWithTheAppropriateErrorMessage_WhenNoTodoExistsWithTheGivenId() {
        // GIVEN
        String todoId = "4";
        Todo todoFromJSON = new TodoBuilder().withId("4").withName("Todo #89").withPriority(Priority.SMALL).build();

        // WHEN
        when(todoRepository.findById(todoId)).thenReturn(Optional.empty());

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID), todoService.updateTodo(todoId, todoFromJSON));

        // VERIFY
        verify(todoRepository, times(1)).findById(todoId);
        verify(todoRepository, times(0)).save(todoFromJSON);
    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithCreated_WhenTheDesiredTodoCanBeUpdated() {
        // GIVEN
        String todoId = "1";
        Todo todoFromJSON = new TodoBuilder().withId("1").withName("Todo #88").withPriority(Priority.BIG).build();
        Todo storedTodo = TODOS.get(0);

        // WHEN
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(storedTodo));
        when(todoRepository.save(todoFromJSON)).thenReturn(todoFromJSON);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(todoFromJSON), todoService.updateTodo(todoId, todoFromJSON));

        // VERIFY
        verify(todoRepository, times(1)).findById(todoId);
        verify(todoRepository, times(1)).save(todoFromJSON);
    }

    /*
        deleteTodo()
     */

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenIdIsNull() {
        // GIVEN
        String todoId = null;

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.deleteTodo(todoId));

        // VERIFY
        verify(todoRepository, times(0)).findById(todoId);
        verify(todoRepository, times(0)).deleteById(todoId);
    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenIdIsEmpty() {
        // GIVEN
        String todoId = "";

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERR_MSG_NULL_OR_EMPTY_ID), todoService.deleteTodo(todoId));

        // VERIFY
        verify(todoRepository, times(0)).findById(todoId);
        verify(todoRepository, times(0)).deleteById(todoId);
    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithNotFoundAndWithTheAppropriateErrorMessage_WhenNoTodoExistsWithTheGivenId() {
        // GIVEN
        String todoId = "4";

        // WHEN
        when(todoRepository.findById(todoId)).thenReturn(Optional.empty());

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID), todoService.deleteTodo(todoId));

        // VERIFY
        verify(todoRepository, times(1)).findById(todoId);
        verify(todoRepository, times(0)).deleteById(todoId);
    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithOk_WhenTheDesiredTodoHasBeenDeleted() {
        // GIVEN
        Todo storedTodo = TODOS.get(0);
        String todoId = storedTodo.getId();

        // WHEN
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(storedTodo));
        doNothing().when(todoRepository).deleteById(todoId);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(new ResponseEntity<>(HttpStatus.OK), todoService.deleteTodo(todoId));

        // VERIFY
        verify(todoRepository, times(1)).findById(todoId);
        verify(todoRepository, times(1)).deleteById(todoId);
    }

    private void createValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
}
