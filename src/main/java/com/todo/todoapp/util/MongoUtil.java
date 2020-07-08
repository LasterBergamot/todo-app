package com.todo.todoapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

@Component
public class MongoUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoUtil.class);

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoUtil(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void createIndex(String loggerMsg, String collectionName, String indexKey, String indexName) {
        LOGGER.info(loggerMsg);

        mongoTemplate
                .indexOps(collectionName)
                .ensureIndex(
                        new Index()
                                .on(indexKey, Sort.DEFAULT_DIRECTION)
                                .named(indexName)
                                .unique()
                );
    }
}
