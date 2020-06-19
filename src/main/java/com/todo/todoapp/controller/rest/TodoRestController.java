package com.todo.todoapp.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoRestController {

    @GetMapping("/todos/helloworld")
    public String helloWorld() {
        return "helloWorld";
    }
}
