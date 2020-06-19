package com.todo.todoapp.model.todo;

import java.time.LocalDate;

public class Todo {

    private String name;
    private LocalDate deadLine;
    private Priority priority;

    public Todo(String name, Priority priority) {
        this(name, LocalDate.EPOCH, priority);
    }

    public Todo(String name, LocalDate deadLine, Priority priority) {
        this.name = name;
        this.deadLine = deadLine;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
