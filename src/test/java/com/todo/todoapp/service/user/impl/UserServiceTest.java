package com.todo.todoapp.service.user.impl;

import com.todo.todoapp.repository.user.UserRepository;
import com.todo.todoapp.util.MongoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static com.todo.todoapp.util.Constants.ATTRIBUTE_LOGIN;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_NAME;
import static com.todo.todoapp.util.Constants.DP_GET_USERNAME_NAME_ATTRIBUTE_IS_NOT_NULL_DATA_PROVIDER;
import static com.todo.todoapp.util.Constants.DP_GET_USERNAME_NAME_ATTRIBUTE_IS_NULL_DATA_PROVIDER;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_GIVEN_PRINCIPAL_IS_NULL;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_PRINCIPAL_S_LOGIN_ATTRIBUTE_IS_NULL;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_PRINCIPAL_S_NAME_ATTRIBUTE_IS_NULL;
import static com.todo.todoapp.util.Constants.NAME_ANDREW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserRepository userRepository;
    private MongoUtil mongoUtil;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        mongoUtil = mock(MongoUtil.class);
    }

    /*
        getUsername()
     */

    @Test
    void test_getUsernameShouldThrowIllegalArgumentException_WhenThePrincipalIsNull() {
        // GIVEN
        OAuth2User principal = null;

        // WHEN
        userService = createUserService();

        // THEN
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUsername(principal)
        );

        assertEquals(ERR_MSG_THE_GIVEN_PRINCIPAL_IS_NULL, exception.getMessage());

        // VERIFY
        verifyNoInteractions(userRepository);
        verifyNoInteractions(mongoUtil);
    }

    private static Object[][] getUsernameNameAttributeIsNullDataProvider() {
        return new Object[][] {
                {mock(OidcUser.class), ATTRIBUTE_NAME, ERR_MSG_THE_PRINCIPAL_S_NAME_ATTRIBUTE_IS_NULL},
                {mock(DefaultOAuth2User.class), ATTRIBUTE_LOGIN, ERR_MSG_THE_PRINCIPAL_S_LOGIN_ATTRIBUTE_IS_NULL}
        };
    }

    @ParameterizedTest
    @MethodSource(DP_GET_USERNAME_NAME_ATTRIBUTE_IS_NULL_DATA_PROVIDER)
    void test_getUserNameShouldThrowIllegalArgumentException_WhenTheNameAttributeIsNull(OAuth2User principal, String nameAttribute, String errorMessage) {
        // GIVEN

        // WHEN
        when(principal.getAttribute(nameAttribute)).thenReturn(null);

        userService = createUserService();

        // THEN
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUsername(principal)
        );

        assertEquals(errorMessage, exception.getMessage());

        // VERIFY
        verify(principal, times(2)).getAttribute(anyString());
        verifyNoInteractions(userRepository);
        verifyNoInteractions(mongoUtil);
    }

    private static Object[][] getUsernameNameAttributeIsNotNullDataProvider() {
        return new Object[][] {
                {mock(OidcUser.class), ATTRIBUTE_NAME},
                {mock(DefaultOAuth2User.class), ATTRIBUTE_LOGIN}
        };
    }

    @ParameterizedTest
    @MethodSource(DP_GET_USERNAME_NAME_ATTRIBUTE_IS_NOT_NULL_DATA_PROVIDER)
    void test_getUsernameShouldReturnAName_WhenTheNameAttributeIsNotNull(OAuth2User principal, String nameAttribute) {
        // GIVEN

        // WHEN
        when(principal.getAttribute(nameAttribute)).thenReturn(NAME_ANDREW);

        userService = createUserService();

        // THEN
        assertEquals(NAME_ANDREW, userService.getUsername(principal));

        // VERIFY
        verify(principal, times(2)).getAttribute(anyString());
        verifyNoInteractions(userRepository);
        verifyNoInteractions(mongoUtil);
    }

    /*
        handleUser()
     */

    @Test
    void test_handleUserShouldThrowIllegalArgumentException_WhenThePrincipalIsNull() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    void test_handleUserShouldThrowAnIllegalArgumentException_WhenTheGivenPrincipalIsNotFromASupportedProvider() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    void test_handleUserShouldThrowAnIllegalArgumentException_WhenTheSubAttributeIsNull_AndTheProviderIsGoogle() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    void test_handleUserShouldThrowAnIllegalArgumentException_WhenTheEmailAttributeIsNull_AndTheProviderIsGoogle() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    void test_handleUserShouldThrowAnIllegalArgumentException_WhenTheIdAttributeIsNull_AndTheProviderIsGithub() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    void test_handleUserShouldThrowAnIllegalArgumentException_WhenTheEmailAttributeIsNull_AndTheProviderIsGithub() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    void test_handleUserShouldReturnNull_WhenThePrincipalIsNotFromTheAvailableProviders() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    void test_handleUserShouldReturnANewUser_WhenNoUserExistsWithTheGivenEmail_AndTheProviderIsGoogle() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    void test_handleUserShouldReturnAnAlreadyExistingUser_WhenAUserExistsWithTheGivenEmail_AndTheProviderIsGoogle_AndTheGoogleIdIsStored() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    void test_handleUserShouldReturnAnAlreadyExistingUser_WhenAUserAlreadyExistsWithTheGivenEmail_AndTheProviderIsGoogle_AndTheGoogleIdIsNotStored() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    void test_handleUserShouldReturnANewUser_WhenNoUserExistsWithTheGivenEmail_AndTheProviderIsGithub() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    void test_handleUserShouldReturnAnAlreadyExistingUser_WhenAUserExistsWithTheGivenEmail_AndTheProviderIsGithub_AndTheGithubIdIsStored() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    @Test
    void test_handleUserShouldReturnAnAlreadyExistingUser_WhenAUserAlreadyExistsWithTheGivenEmail_AndTheProviderIsGithub_AndTheGithubIdIsNotStored() {
        // GIVEN

        // WHEN

        // THEN

        // VERIFY

    }

    private UserService createUserService() {
        return new UserService(userRepository, mongoUtil);
    }
}
