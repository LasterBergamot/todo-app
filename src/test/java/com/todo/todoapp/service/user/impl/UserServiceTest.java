package com.todo.todoapp.service.user.impl;

import com.todo.todoapp.repository.user.UserRepository;
import com.todo.todoapp.util.MongoUtil;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private UserRepository userRepository;
    private MongoUtil mongoUtil;

    private UserService userService;

    /*
        getUsername()
     */

    @Test
    void test_getUsernameShouldThrowIllegalArgumentException_WhenThePrincipalIsNull() {

    }

    @Test
    void test_getUsernameShouldReturnTheUsernameFromTheNameAttribute_WhenTheProviderIsGoogle() {

    }

    @Test
    void test_getUsernameShouldReturnTheUsernameFromTheLoginAttribute_WhenTheProviderIsGithub() {

    }

    /*
        handleUser()
     */

    @Test
    void test_handleUserShouldThrowIllegalArgumentException_WhenThePrincipalIsNull() {

    }

    @Test
    void test_handleUserShouldReturnNull_WhenThePrincipalIsNotFromTheAvailableProviders() {

    }

    @Test
    void test_handleUserShouldReturnANewUser_WhenNoUserExistsWithTheGivenEmail_AndTheProviderIsGoogle() {

    }

    @Test
    void test_handleUserShouldReturnAnAlreadyExistingUser_WhenAUserExistsWithTheGivenEmail_AndTheProviderIsGoogle_AndTheGoogleIdIsStored() {

    }

    @Test
    void test_handleUserShouldReturnAnAlreadyExistingUser_WhenAUserAlreadyExistsWithTheGivenEmail_AndTheProviderIsGoogle_AndTheGoogleIdIsNotStored() {

    }

    @Test
    void test_handleUserShouldReturnANewUser_WhenNoUserExistsWithTheGivenEmail_AndTheProviderIsGithub() {

    }

    @Test
    void test_handleUserShouldReturnAnAlreadyExistingUser_WhenAUserExistsWithTheGivenEmail_AndTheProviderIsGithub_AndTheGithubIdIsStored() {

    }

    @Test
    void test_handleUserShouldReturnAnAlreadyExistingUser_WhenAUserAlreadyExistsWithTheGivenEmail_AndTheProviderIsGithub_AndTheGithubIdIsNotStored() {

    }
}
