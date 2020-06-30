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

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.todo.todoapp.util.TodoConstants.ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID;
import static com.todo.todoapp.util.TodoConstants.ERR_MSG_NULL_JSON;
import static com.todo.todoapp.util.TodoConstants.ERR_MSG_NULL_OR_EMPTY_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TodoServiceTest {

    public static final List<Todo> EXPECTED_TODOS = List.of(
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
        when(todoRepository.findAll()).thenReturn(EXPECTED_TODOS);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.ok(EXPECTED_TODOS), todoService.getTodos());

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
        when(todoRepository.findById("1")).thenReturn(Optional.of(EXPECTED_TODOS.get(0)));

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.ok(EXPECTED_TODOS.get(0)), todoService.getTodo("1"));

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

    @Disabled
    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenTheGivenTodoFromJSONIsEmpty() {
        // GIVEN
        Todo emptyTodo = new TodoBuilder().build();

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        Exception exception = assertThrows(ConstraintViolationException.class, () -> todoService.saveTodo(emptyTodo));

        // VERIFY
        verify(todoRepository, times(0)).save(emptyTodo);
    }

    @Disabled
    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenTheNameFieldIsEmptyInTheGivenTodoFromJSON() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Disabled
    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldIsEmptyInTheGivenTodoFromJSON() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Disabled
    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldIsNotAValidValueInTheGivenTodoFromJSON() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

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
        Todo createdTodo = EXPECTED_TODOS.get(0);

        // WHEN
        when(todoRepository.save(createdTodo)).thenReturn(createdTodo);

        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(createdTodo), todoService.saveTodo(createdTodo));

        // VERIFY
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    /*
        updateTodo()
     */

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoIdIsNull() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenTodoFromJSONIsNull() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Disabled
    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenTheGivenTodoIdIsEmpty() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Disabled
    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenTodoFromJSONIsEmpty() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Disabled
    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenTheNameFieldIsEmptyInTodoFromJSON() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Disabled
    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldIsEmptyInTodoFromJSON() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

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

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithCreated_WhenTheDesiredTodoCanBeUpdated() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    /*
        deleteTodo()
     */

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenIdIsNull() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithBadRequestAndWithTheAppropriateErrorMessage_WhenTheGivenIdIsEmpty() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithNotFoundAndWithTheAppropriateErrorMessage_WhenNoTodoExistsWithTheGivenId() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithOk_WhenTheDesiredTodoHasBeenDeleted() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }
}
