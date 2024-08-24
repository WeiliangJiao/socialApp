package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.Credentials;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.service.CredentialsService;
import csci3130.group15.socialApp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private CredentialsService credentialsService;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_successfulRegistration() throws Exception {
        String username = "testuser";
        String email = "testuser@dal.ca";
        String password = "Password@123";
        String securityQuestion = "What is your pet's name?";
        String securityAnswer = "Fluffy";

        User user = new User();
        user.setUsername(username);

        Credentials credentials = new Credentials();
        credentials.setEmail(email);
        credentials.setPassword(password);
        credentials.setSecurityQuestion(securityQuestion);
        credentials.setSecurityAnswer(securityAnswer);
        credentials.setUser(user);

        when(userService.save(any(User.class))).thenReturn(user);
        when(credentialsService.save(any(Credentials.class))).thenReturn(credentials);

        registrationService.registerUser(username, email, password, securityQuestion, securityAnswer);

        verify(userService, times(1)).save(any(User.class));
        verify(credentialsService, times(1)).save(any(Credentials.class));
    }

    @Test
    void testRegisterUser_invalidEmail() {
        String username = "testuser";
        String email = "testuser@gmail.com";
        String password = "Password@123";
        String securityQuestion = "What is your pet's name?";
        String securityAnswer = "Fluffy";

        Exception exception = assertThrows(Exception.class, () -> {
            registrationService.registerUser(username, email, password, securityQuestion, securityAnswer);
        });

        String expectedMessage = "Registration is only allowed for Dalhousie employees and students.Please register with your dal email.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(userService, never()).save(any(User.class));
        verify(credentialsService, never()).save(any(Credentials.class));
    }

    @Test
    void testRegisterUser_invalidPassword() {
        String username = "testuser";
        String email = "testuser@dal.ca";
        String password = "password";
        String securityQuestion = "What is your pet's name?";
        String securityAnswer = "Fluffy";

        Exception exception = assertThrows(Exception.class, () -> {
            registrationService.registerUser(username, email, password, securityQuestion, securityAnswer);
        });

        String expectedMessage = "Password must meet complexity requirements.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(userService, never()).save(any(User.class));
        verify(credentialsService, never()).save(any(Credentials.class));
    }

    @Test
    void testValidateEmail_validEmail() {
        String email = "testuser@dal.ca";
        assertDoesNotThrow(() -> registrationService.validateEmail(email));
    }

    @Test
    void testValidateEmail_invalidEmail() {
        String email = "testuser@gmail.com";

        Exception exception = assertThrows(Exception.class, () -> {
            registrationService.validateEmail(email);
        });

        String expectedMessage = "Registration is only allowed for Dalhousie employees and students.Please register with your dal email.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testValidatePassword_validPassword() throws Exception {
        String password = "Password@123";
        registrationService.validatePassword(password);
    }

    @Test
    void testValidatePassword_invalidPassword() {
        String password = "password";

        Exception exception = assertThrows(Exception.class, () -> {
            registrationService.validatePassword(password);
        });

        String expectedMessage = "Password must meet complexity requirements.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
