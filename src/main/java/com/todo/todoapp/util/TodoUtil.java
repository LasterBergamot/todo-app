package com.todo.todoapp.util;

import com.todo.todoapp.model.todo.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TodoUtil {

    private TodoUtil() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoUtil.class);

    public static Todo updateTodo(Todo todoFromRepo, Todo todoFromJSON) {
        LOGGER.info("Updating Todo!");
        LOGGER.info("Original: {}", todoFromRepo);
        LOGGER.info("Updated: {}", todoFromJSON);

        todoFromRepo.setName(todoFromJSON.getName());
        todoFromRepo.setDeadline(todoFromJSON.getDeadline());
        todoFromRepo.setPriority(todoFromJSON.getPriority());

        return todoFromRepo;
    }
}
