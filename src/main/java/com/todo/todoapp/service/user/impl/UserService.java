package com.todo.todoapp.service.user.impl;

import com.todo.todoapp.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UserService implements IUserService {

    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void initIndexes() {
        LOGGER.info("Creating index for the 'github_id' field of User.");

        mongoTemplate
                .indexOps("User")
                .ensureIndex(
                        new Index()
                                .on("github_id", Sort.DEFAULT_DIRECTION)
                                .named("User_github_id_index")
                                .unique()
                );

        LOGGER.info("Creating index for the 'google_id' field of User.");

        mongoTemplate
                .indexOps("User")
                .ensureIndex(
                        new Index()
                                .on("google_id", Sort.DEFAULT_DIRECTION)
                                .named("User_google_id_index")
                                .unique()
                );
    }
}
