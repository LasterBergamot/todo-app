package com.todo.todoapp.service.impl;

import org.junit.jupiter.api.Test;

public class TodoServiceTest {

    private TodoService todoService;

    /*
        getTodos()
     */

    @Test
    public void test_getTodosShouldReturnAnEmptyList_WhenThereAreNoRecordsInTheDatabase() {

    }

    @Test
    public void test_getTodosShouldReturnAllOfTheRecords_WhenTheDatabaseIsNotEmpty() {

    }

    /*
        getTodo()
     */

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithBadRequest_WhenTheGivenTodoIdIsNull() {

    }

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithBadRequest_WhenTheGivenTodoIdIsEmpty() {

    }

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithNotFound_WhenNoTodoExistsWithTheGivenId() {

    }

    @Test
    public void test_getTodoShouldReturnAResponseEntityWithOk_WhenTheDesiredTodoCouldHaveBeenRetrieved() {

    }

    /*
        saveTodo()
     */

    @Test
    public void test_saveTodoShouldReturnAResponseEntityWithBadRequest_WhenTheGivenTodoFromJSONIsNull() {

    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenTheGivenTodoFromJSONIsEmpty() {

    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenTheNameFieldIsEmptyInTheGivenTodoFromJSON() {

    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldIsEmptyInTheGivenTodoFromJSON() {

    }

    @Test
    public void test_saveTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldIsNotAValidValueInTheGivenTodoFromJSON() {

    }

    @Test
    public void test_saveTodoShouldThrowAMongoWriteException_WhenARecordAlreadyExistsWithTheSameName() {

    }

    @Test
    public void test_saveTodoShouldReturnAResponseEntityWithCreated_WhenTheGivenTodoFromJSONIsValid() {

    }

    /*
        updateTodo()
     */

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithBadRequest_WhenTheGivenTodoIdIsNull() {

    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithBadRequest_WhenTheGivenTodoFromJSONIsNull() {

    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenTodoFromJSONIsEmpty() {

    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenTheNameFieldIsEmptyInTodoFromJSON() {

    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldIsEmptyInTodoFromJSON() {

    }

    @Test
    public void test_updateTodoShouldThrowAConstraintViolationException_WhenThePriorityFieldHasANonValidValueInTodoFromJSON() {

    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithNotFound_WhenNoTodoExistsWithTheGivenId() {

    }

    @Test
    public void test_updateTodoShouldReturnAResponseEntityWithCreated_WhenTheDesiredTodoCanBeUpdated() {

    }

    /*
        deleteTodo()
     */

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithBadRequest_WhenTheGivenIdIsNull() {

    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithNotFound_WhenNoTodoExistsWithTheGivenId() {

    }

    @Test
    public void test_deleteTodoShouldReturnAResponseEntityWithOk_WhenTheDesiredTodoHasBeenDeleted() {

    }
}
