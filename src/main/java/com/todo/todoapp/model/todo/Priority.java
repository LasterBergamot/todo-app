package com.todo.todoapp.model.todo;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public enum Priority {

    BIG("big"), MEDIUM("medium"), SMALL("small");

    @NotNull
    @NotEmpty
    @Valid
    private final String priority;

    Priority(String priority) {
        this.priority = priority;
    }
}
