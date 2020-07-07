package com.todo.todoapp.util;

import com.todo.todoapp.model.todo.Todo;
import org.springframework.stereotype.Component;

@Component
public class TodoUtil {

    public static Todo updateTodo(Todo todoFromRepo, Todo todoFromJSON) {
        todoFromRepo.setName(todoFromJSON.getName());
        todoFromRepo.setDeadline(todoFromJSON.getDeadline());
        todoFromRepo.setPriority(todoFromJSON.getPriority());

        return todoFromRepo;
    }
}
