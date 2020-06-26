package com.todo.todoapp.model.todo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Document
public class Todo {

    @Id
    private String id;

    @NotEmpty
    @NotNull
    @Field("name")
    @Indexed(unique = true)
    @Valid
    private String name;

    @Field("deadline")
    private LocalDate deadline;

    @NotNull
    @Field("priority")
    @Valid
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

    @Override
    public String toString() {
        return "Todo{" +
                "name='" + name + '\'' +
                ", deadline=" + deadline +
                ", priority=" + priority +
                '}';
    }
}
