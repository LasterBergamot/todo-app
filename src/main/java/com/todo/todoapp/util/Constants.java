package com.todo.todoapp.util;

public class Constants {

    private Constants() {}

    public static final String ERR_MSG_NULL_OR_EMPTY_ID = "The given id was null or empty!";
    public static final String ERR_MSG_NULL_JSON = "The given JSON was null!";
    public static final String ERR_MSG_NO_TODO_WAS_FOUND_WITH_THE_GIVEN_ID = "No Todo was found with the given ID!";
    public static final String ERR_MSG_THE_GIVEN_PRINCIPAL_IS_NULL = "The given principal is null!";
    public static final String ERR_MSG_THE_GIVEN_USER_COULD_NOT_BE_SAVED_TO_ANY_AVAILABLE_SERVICE = "The given user could not be saved to any available service!";
    public static final String ERR_MSG_CURRENTLY_NO_USER_IS_LOGGED_IN = "Currently no user is logged in!";
    public static final String ERR_MSG_CURRENTLY_THE_GIVEN_USER_DOES_NOT_EXIST_IN_THE_DATABASE = "Currently the given user does not exist in the database!";
    public static final String ERR_MSG_THE_PRINCIPAL_S_NAME_ATTRIBUTE_IS_NULL = "The principal's name attribute is null!";
    public static final String ERR_MSG_THE_PRINCIPAL_S_LOGIN_ATTRIBUTE_IS_NULL = "The principal's login attribute is null!";
    public static final String ERR_MSG_THE_PRINCIPAL_S_SUB_ATTRIBUTE_IS_NULL = "The principal's sub attribute is null!";
    public static final String ERR_MSG_THE_PRINCIPAL_S_EMAIL_ATTRIBUTE_IS_NULL = "The principal's email attribute is null!";
    public static final String ERR_MSG_THE_PRINCIPAL_S_ID_ATTRIBUTE_IS_NULL = "The principal's id attribute is null!";

    public static final String NAME_ANDREW = "Andrew";

    public static final String COLLECTION_NAME_TODO = "Todo";
    public static final String COLLECTION_NAME_USER = "User";

    public static final String KEY_NAME = "name";
    public static final String KEY_GITHUB_ID = "github_id";
    public static final String KEY_GOOGLE_ID = "google_id";
    public static final String KEY_EMPTY_TODO = "EmptyTodo";
    public static final String KEY_TODO_WITHOUT_NAME = "TodoWithoutName";
    public static final String KEY_TODO_WITH_EMPTY_NAME = "TodoWithEmptyName";
    public static final String KEY_TODO_WITHOUT_PRIORITY = "TodoWithoutPriority";
    public static final String KEY_NON_EXISTING_TODO = "NonExistingTodo";
    public static final String KEY_TODO_FOR_UPDATING = "TodoForUpdating";

    public static final String INDEX_NAME_TODO_NAME_INDEX = "Todo_name_index";
    public static final String INDEX_NAME_USER_GITHUB_ID_INDEX = "User_github_id_index";
    public static final String INDEX_NAME_USER_GOOGLE_ID_INDEX = "User_google_id_index";

    public static final String ATTRIBUTE_SUB = "sub";
    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_LOGIN = "login";
    public static final String ATTRIBUTE_EMAIL = "email";

    public static final String TODO_ID_ONE = "1";
    public static final String TODO_ID_TWO = "2";
    public static final String TODO_ID_THREE = "3";
    public static final String TODO_ID_FOUR = "4";

    public static final String TODO_NAME_ONE = "Todo #1";
    public static final String TODO_NAME_TWO = "Todo #2";
    public static final String TODO_NAME_THREE = "Todo #3";

    public static final String EMPTY_STRING = "";

    public static final String DP_GET_TODOS_DATA_PROVIDER = "getTodosDataProvider";
    public static final String DP_GET_TODO_DATA_PROVIDER = "getTodoDataProvider";
    public static final String DP_GET_TODO_WITH_MOCKING_DATA_PROVIDER = "getTodoWithMockingDataProvider";
    public static final String DP_SAVE_TODO_CONSTRAINT_VIOLATION_EXCEPTION_DATA_PROVIDER = "saveTodoConstraintViolationExceptionDataProvider";
    public static final String DP_UPDATE_TODO_DATA_PROVIDER = "updateTodoDataProvider";
    public static final String DP_UPDATE_TODO_CONSTRAINT_VIOLATION_EXCEPTION_DATA_PROVIDER = "updateTodoConstraintViolationExceptionDataProvider";
    public static final String DP_DELETE_TODO_DATA_PROVIDER = "deleteTodoDataProvider";
    public static final String DP_GET_USERNAME_NAME_ATTRIBUTE_IS_NULL_DATA_PROVIDER = "getUsernameNameAttributeIsNullDataProvider";
    public static final String DP_GET_USERNAME_NAME_ATTRIBUTE_IS_NOT_NULL_DATA_PROVIDER = "getUsernameNameAttributeIsNotNullDataProvider";
    public static final String DP_HANDLE_USER_ID_ATTRIBUTE_IS_NULL_DATA_PROVIDER = "handleUserIdAttributeIsNullDataProvider";
    public static final String DP_HANDLE_USER_EMAIL_ATTRIBUTE_IS_NULL_DATA_PROVIDER = "handleUserEmailAttributeIsNullDataProvider";
    public static final String DP_HANDLE_USER_SHOULD_RETURN_A_NEW_USER_DATA_PROVIDER = "handleUserShouldReturnANewUserDataProvider";

    public static final String GOOGLE_ID = "156246724635752711248";
    public static final String GITHUB_ID = "56322326";
    public static final String EMAIL = "asd123@asd.com";
}
