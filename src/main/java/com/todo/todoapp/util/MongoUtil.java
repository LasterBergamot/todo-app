package com.todo.todoapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

public class MongoUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoUtil.class);

    @Autowired
    private static MongoTemplate mongoTemplate;

    private MongoUtil() {}

    public static void createIndex(String loggerMsg, String collectionName, String indexKey, String indexName) {
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
