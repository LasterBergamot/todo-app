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

    @NotNull
    @NotEmpty
    @Field("user_id")
    private final String userId;

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

    public Todo(String id, @NotNull @NotEmpty String userId, @NotEmpty @NotNull @Valid String name, LocalDate deadline, @NotNull @Valid Priority priority) {
        this.id = id;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id) &&
                Objects.equals(userId, todo.userId) &&
                Objects.equals(name, todo.name) &&
                Objects.equals(deadline, todo.deadline) &&
                priority == todo.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, deadline, priority);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", deadline=" + deadline +
                ", priority=" + priority +
                '}';
    }

    public static class Builder {
        private String id;
        private String userId;
        private String name;
        private LocalDate deadline;
        private Priority priority;

        public Todo.Builder withId(String id) {
            this.id = id;

            return this;
        }

        public Todo.Builder withUserId(String userId) {
            this.userId = userId;

            return this;
        }

        public Todo.Builder withName(String name) {
            this.name = name;

            return this;
        }

        public Todo.Builder withDeadline(LocalDate deadline) {
            this.deadline = deadline;

            return this;
        }

        public Todo.Builder withPriority(Priority priority) {
            this.priority = priority;

            return this;
        }

        public Todo build() {
            return new Todo(id, userId, name, deadline, priority);
        }
    }
}
