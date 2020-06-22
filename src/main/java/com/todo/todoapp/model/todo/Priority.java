package com.todo.todoapp.model.todo;

public enum Priority {

    BIG("big"), MEDIUM("medium"), SMALL("small");

    private final String priority;

    Priority(String priority) {
        this.priority = priority;
    }
}
