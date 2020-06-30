package com.todo.todoapp.model.todo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Document(collection = "Todo")
public class Todo {

    @Id
    private final String id;

    @NotEmpty
    @NotNull
    @Field("name")
    @Valid
    private String name;

    @Field("deadline")
    private LocalDate deadline;

    @NotNull
    @Field("priority")
    @Valid
    private Priority priority;

    public Todo(String id, @NotEmpty @NotNull @Valid String name, LocalDate deadline, @NotNull @Valid Priority priority) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getId() {
        return id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id) &&
                Objects.equals(name, todo.name) &&
                Objects.equals(deadline, todo.deadline) &&
                priority == todo.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, deadline, priority);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", deadline=" + deadline +
                ", priority=" + priority +
                '}';
    }
}
