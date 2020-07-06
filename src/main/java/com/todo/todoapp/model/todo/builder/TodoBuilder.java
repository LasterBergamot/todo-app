package com.todo.todoapp.model.todo.builder;

import com.todo.todoapp.model.todo.Priority;
import com.todo.todoapp.model.todo.Todo;

import java.time.LocalDate;

public class TodoBuilder {

    private String id;
    private String userId;
    private String name;
    private LocalDate deadline;
    private Priority priority;

    public TodoBuilder withId(String id) {
        this.id = id;

        return this;
    }

    public TodoBuilder withUserId(String userId) {
        this.userId = userId;

        return this;
    }

    public TodoBuilder withName(String name) {
        this.name = name;

        return this;
    }

    public TodoBuilder withDeadline(LocalDate deadline) {
        this.deadline = deadline;

        return this;
    }

    public TodoBuilder withPriority(Priority priority) {
        this.priority = priority;

        return this;
    }

    public Todo build() {
        return new Todo(id, userId, name, deadline, priority);
    }
}
