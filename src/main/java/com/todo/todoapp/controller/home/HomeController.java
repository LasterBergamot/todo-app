package com.todo.todoapp.controller.home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    private static final String GET_MAPPING_ROOT = "/";

    private static final String PAGE_INDEX = "index";

    @GetMapping(GET_MAPPING_ROOT)
    public String getHomePage() {
        return PAGE_INDEX;
    }
}
