package com.todo.todoapp.model.todo;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class Todo {

    @Id
    private String id;

    private String name;
    private LocalDate deadline;
    private Priority priority;

    public Todo() {}

    public Todo(String name, Priority priority) {
        this(name, LocalDate.EPOCH, priority);
    }

    public Todo(String name, LocalDate deadline, Priority priority) {
        this.name = name;
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
