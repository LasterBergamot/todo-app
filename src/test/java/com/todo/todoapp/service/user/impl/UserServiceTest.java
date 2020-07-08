package com.todo.todoapp.service.user.impl;

import com.todo.todoapp.model.user.User;
import com.todo.todoapp.repository.user.UserRepository;
import com.todo.todoapp.util.MongoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static com.todo.todoapp.util.Constants.ATTRIBUTE_EMAIL;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_ID;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_LOGIN;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_NAME;
import static com.todo.todoapp.util.Constants.ATTRIBUTE_SUB;
import static com.todo.todoapp.util.Constants.DP_GET_USERNAME_NAME_ATTRIBUTE_IS_NOT_NULL_DATA_PROVIDER;
import static com.todo.todoapp.util.Constants.DP_GET_USERNAME_NAME_ATTRIBUTE_IS_NULL_DATA_PROVIDER;
import static com.todo.todoapp.util.Constants.DP_HANDLE_USER_EMAIL_ATTRIBUTE_IS_NULL_DATA_PROVIDER;
import static com.todo.todoapp.util.Constants.DP_HANDLE_USER_ID_ATTRIBUTE_IS_NULL_DATA_PROVIDER;
import static com.todo.todoapp.util.Constants.DP_HANDLE_USER_SHOULD_RETURN_A_NEW_USER_DATA_PROVIDER;
import static com.todo.todoapp.util.Constants.EMAIL;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_GIVEN_PRINCIPAL_IS_NULL;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_GIVEN_USER_COULD_NOT_BE_SAVED_TO_ANY_AVAILABLE_SERVICE;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_PRINCIPAL_S_EMAIL_ATTRIBUTE_IS_NULL;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_PRINCIPAL_S_ID_ATTRIBUTE_IS_NULL;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_PRINCIPAL_S_LOGIN_ATTRIBUTE_IS_NULL;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_PRINCIPAL_S_NAME_ATTRIBUTE_IS_NULL;
import static com.todo.todoapp.util.Constants.ERR_MSG_THE_PRINCIPAL_S_SUB_ATTRIBUTE_IS_NULL;
import static com.todo.todoapp.util.Constants.GITHUB_ID;
import static com.todo.todoapp.util.Constants.GOOGLE_ID;
import static com.todo.todoapp.util.Constants.NAME_ANDREW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    public static final User USER_WITH_GITHUB_ID = new User.Builder()
            .withEmail(EMAIL)
            .withGithubId(GITHUB_ID)
            .build();

    public static final User USER_WITH_GOOGLE_ID = new User.Builder()
            .withEmail(EMAIL)
            .withGoogleId(GOOGLE_ID)
            .build();

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
        verifyNoInteractions(userRepository, mongoUtil);
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
        verifyNoInteractions(userRepository, mongoUtil);
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
        verifyNoInteractions(userRepository, mongoUtil);
    }

    /*
        handleUser()
     */

    @Test
    void test_handleUserShouldThrowIllegalArgumentException_WhenThePrincipalIsNull() {
        // GIVEN
        OAuth2User principal = null;

        // WHEN
        userService = createUserService();

        // THEN
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.handleUser(principal)
        );

        assertEquals(ERR_MSG_THE_GIVEN_PRINCIPAL_IS_NULL, exception.getMessage());

        // VERIFY
        verifyNoInteractions(userRepository, mongoUtil);
    }

    private static Object[][] handleUserIdAttributeIsNullDataProvider() {
        return new Object[][] {
                {mock(OidcUser.class), ATTRIBUTE_SUB, ERR_MSG_THE_PRINCIPAL_S_SUB_ATTRIBUTE_IS_NULL},
                {mock(DefaultOAuth2User.class), ATTRIBUTE_ID, ERR_MSG_THE_PRINCIPAL_S_ID_ATTRIBUTE_IS_NULL}
        };
    }

    @ParameterizedTest
    @MethodSource(DP_HANDLE_USER_ID_ATTRIBUTE_IS_NULL_DATA_PROVIDER)
    void test_handleUserShouldThrowIllegalArgumentException_WhenTheIdAttributeIsNull(OAuth2User principal, String idAttribute, String errorMessage) {
        // GIVEN

        // WHEN
        when(principal.getAttribute(idAttribute)).thenReturn(null);

        userService = createUserService();

        // THEN
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.handleUser(principal)
        );

        assertEquals(errorMessage, exception.getMessage());

        // VERIFY
        verify(principal, times(1)).getAttribute(anyString());
        verifyNoInteractions(userRepository, mongoUtil);
    }

    private static Object[][] handleUserEmailAttributeIsNullDataProvider() {
        return new Object[][] {
                {mock(OidcUser.class), ATTRIBUTE_SUB, GOOGLE_ID, ERR_MSG_THE_PRINCIPAL_S_EMAIL_ATTRIBUTE_IS_NULL},
                {mock(DefaultOAuth2User.class), ATTRIBUTE_ID, GITHUB_ID, ERR_MSG_THE_PRINCIPAL_S_EMAIL_ATTRIBUTE_IS_NULL}
        };
    }

    @ParameterizedTest
    @MethodSource(DP_HANDLE_USER_EMAIL_ATTRIBUTE_IS_NULL_DATA_PROVIDER)
    void test_handleUserShouldThrowAnIllegalArgumentException_WhenTheEmailAttributeIsNull(OAuth2User principal, String idAttribute, String id, String errorMessage) {
        // GIVEN

        // WHEN
        when(principal.getAttribute(idAttribute)).thenReturn(id);
        when(principal.getAttribute(ATTRIBUTE_EMAIL)).thenReturn(null);

        userService = createUserService();

        // THEN
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.handleUser(principal)
        );

        assertEquals(errorMessage, exception.getMessage());

        // VERIFY
        verify(principal, times(2)).getAttribute(anyString());
        verifyNoInteractions(userRepository, mongoUtil);
    }

    @Disabled
    @Test
    void test_handleUserShouldThrowAnIllegalArgumentException_WhenTheGivenPrincipalIsNotFromASupportedProvider() {
        // GIVEN
        OAuth2AuthenticatedPrincipal principal = mock(OAuth2AuthenticatedPrincipal.class);

        // WHEN
        when(principal.getAttribute(ATTRIBUTE_ID)).thenReturn(GITHUB_ID);
        when(principal.getAttribute(ATTRIBUTE_EMAIL)).thenReturn(EMAIL);

        userService = createUserService();

        // THEN
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.handleUser((OAuth2User) principal)
        );

        assertEquals(ERR_MSG_THE_GIVEN_USER_COULD_NOT_BE_SAVED_TO_ANY_AVAILABLE_SERVICE, exception.getMessage());

        // VERIFY
        verify(principal, times(2)).getAttribute(anyString());
        verifyNoInteractions(userRepository, mongoUtil);
    }

    private static Object[][] handleUserShouldReturnANewUserDataProvider() {
        return new Object[][] {
                {mock(OidcUser.class), USER_WITH_GOOGLE_ID, null, ATTRIBUTE_SUB, GOOGLE_ID},
                {mock(OidcUser.class), USER_WITH_GOOGLE_ID, USER_WITH_GOOGLE_ID, ATTRIBUTE_SUB, GOOGLE_ID},
                {mock(DefaultOAuth2User.class), USER_WITH_GITHUB_ID, null, ATTRIBUTE_ID, GITHUB_ID},
                {mock(DefaultOAuth2User.class), USER_WITH_GITHUB_ID, USER_WITH_GITHUB_ID, ATTRIBUTE_ID, GITHUB_ID}
        };
    }

    @ParameterizedTest
    @MethodSource(DP_HANDLE_USER_SHOULD_RETURN_A_NEW_USER_DATA_PROVIDER)
    void test_handleUserShouldReturnANewUser(OAuth2User principal, User user, User userReturnedByEmail, String idAttribute, String id) {
        // GIVEN

        // WHEN
        when(principal.getAttribute(idAttribute)).thenReturn(id);
        when(principal.getAttribute(ATTRIBUTE_EMAIL)).thenReturn(EMAIL);

        when(userRepository.findByEmail(EMAIL)).thenReturn(userReturnedByEmail);
        when(userRepository.save(user)).thenReturn(user);

        userService = createUserService();

        // THEN
        assertEquals(user, userService.handleUser(principal));

        // VERIFY
        verify(principal, times(4)).getAttribute(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoInteractions(mongoUtil);
    }

    @Test
    void test_handleUserShouldReturnAnAlreadyExistingUser_WhenAUserAlreadyExistsWithTheGivenEmail_AndTheProviderIsGoogle_AndTheGoogleIdIsNotStored() {
        // GIVEN
        OidcUser principal = mock(OidcUser.class);
        User storedUser = new User.Builder()
                .withEmail(EMAIL)
                .build();

        User returnedUser = USER_WITH_GOOGLE_ID;

        // WHEN
        when(principal.getAttribute(ATTRIBUTE_SUB)).thenReturn(GOOGLE_ID);
        when(principal.getAttribute(ATTRIBUTE_EMAIL)).thenReturn(EMAIL);

        when(userRepository.findByEmail(EMAIL)).thenReturn(storedUser);
        when(userRepository.findByGoogleId(GOOGLE_ID)).thenReturn(null);
        when(userRepository.save(returnedUser)).thenReturn(returnedUser);

        userService = createUserService();

        // THEN
        assertEquals(storedUser, userService.handleUser(principal));

        // VERIFY
        verify(principal, times(4)).getAttribute(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).findByGoogleId(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoInteractions(mongoUtil);
    }

    @Test
    void test_handleUserShouldReturnAnAlreadyExistingUser_WhenAUserAlreadyExistsWithTheGivenEmail_AndTheProviderIsGithub_AndTheGithubIdIsNotStored() {
        // GIVEN
        DefaultOAuth2User principal = mock(DefaultOAuth2User.class);
        User storedUser = new User.Builder()
                .withEmail(EMAIL)
                .build();

        User returnedUser = USER_WITH_GITHUB_ID;

        // WHEN
        when(principal.getAttribute(ATTRIBUTE_ID)).thenReturn(GITHUB_ID);
        when(principal.getAttribute(ATTRIBUTE_EMAIL)).thenReturn(EMAIL);

        when(userRepository.findByEmail(EMAIL)).thenReturn(storedUser);
        when(userRepository.findByGithubId(GITHUB_ID)).thenReturn(null);
        when(userRepository.save(returnedUser)).thenReturn(returnedUser);

        userService = createUserService();

        // THEN
        assertEquals(storedUser, userService.handleUser(principal));

        // VERIFY
        verify(principal, times(4)).getAttribute(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).findByGithubId(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoInteractions(mongoUtil);
    }

    private UserService createUserService() {
        return new UserService(userRepository, mongoUtil);
    }
}
