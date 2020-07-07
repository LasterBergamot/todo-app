package com.todo.todoapp.util;

public class Constants {

    private Constants() {}

    public static final String ERR_MSG_NULL_OR_EMPTY_ID = "The given id was null or empty!";
    public static final String ERR_MSG_NULL_JSON = "The given JSON was null!";
    public static final String ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID = "No Todo was found with the given ID!";
    public static final String ERR_MSG_THE_GIVEN_PRINCIPAL_WAS_NULL = "The given principal was null!";
    public static final String ERR_MSG_THE_GIVEN_USER_COULD_NOT_BE_SAVED_TO_ANY_AVAILABLE_SERVICE = "The given user could not be saved to any available service!";

    public static final String COLLECTION_NAME_TODO = "Todo";
    public static final String COLLECTION_NAME_USER = "User";

    public static final String KEY_NAME = "name";
    public static final String KEY_GITHUB_ID = "github_id";
    public static final String KEY_GOOGLE_ID = "google_id";

    public static final String INDEX_NAME_TODO_NAME_INDEX = "Todo_name_index";
    public static final String INDEX_NAME_USER_GITHUB_ID_INDEX = "User_github_id_index";
    public static final String INDEX_NAME_USER_GOOGLE_ID_INDEX = "User_google_id_index";

    public static final String ATTRIBUTE_SUB = "sub";
    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_LOGIN = "login";
    public static final String ATTRIBUTE_EMAIL = "email";
}
