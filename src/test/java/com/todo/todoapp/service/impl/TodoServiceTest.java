package com.todo.todoapp.service.impl;

import com.todo.todoapp.model.todo.Priority;
import com.todo.todoapp.model.todo.Todo;
import com.todo.todoapp.model.todo.builder.TodoBuilder;
import com.todo.todoapp.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
    public void test_getTodoShouldReturnAResponseEntityWithBadRequestAndTheAppropriateErrorMessage_WhenTheGivenTodoIdIsNull() {
        // GIVEN

        // WHEN
        todoService = new TodoService(todoRepository, mongoTemplate);

        // THEN
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), todoService.getTodo(null));

        // VERIFY

    }

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithBadRequest_WhenTheGivenTodoIdIsEmpty() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithNotFound_WhenNoTodoExistsWithTheGivenId() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithOk_WhenTheDesiredTodoCanBeRetrieved() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    /*
        saveTodo()
     */

    @Test
    public void test_saveTodoShouldReturnAResponseEntityWithBadRequest_WhenTheGivenTodoFromJSONIsNull() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenTheGivenTodoFromJSONIsEmpty() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenTheNameFieldIsEmptyInTheGivenTodoFromJSON() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldIsEmptyInTheGivenTodoFromJSON() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldIsNotAValidValueInTheGivenTodoFromJSON() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

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

        // WHEN

        // THEN

        // VERIFY

    }

    /*
        updateTodo()
     */

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithBadRequest_WhenTheGivenTodoIdIsNull() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithBadRequest_WhenTheGivenTodoFromJSONIsNull() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenTheGivenTodoIdIsEmpty() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenTodoFromJSONIsEmpty() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenTheNameFieldIsEmptyInTodoFromJSON() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldIsEmptyInTodoFromJSON() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldHasANonValidValueInTodoFromJSON() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithNotFound_WhenNoTodoExistsWithTheGivenId() {
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
    public void test_deleteTodoShouldReturnAResponseEntityWithBadRequest_WhenTheGivenIdIsNull() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithBadRequest_WhenTheGivenIdIsEmpty() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithNotFound_WhenNoTodoExistsWithTheGivenId() {
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
