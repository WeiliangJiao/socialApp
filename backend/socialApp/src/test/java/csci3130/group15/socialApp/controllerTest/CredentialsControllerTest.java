package csci3130.group15.socialApp.controllerTest;
import csci3130.group15.socialApp.controller.CredentialsController;
import csci3130.group15.socialApp.service.CredentialsService;
import csci3130.group15.socialApp.model.Credentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CredentialsControllerTest {

    @Mock
    private CredentialsService credentialsService;

    @InjectMocks
    private CredentialsController credentialsController;

    @Test
    public void testGetSecurityQuestion_Success() {
        String email = "test@example.com";
        String securityQuestion = "What is your pet's name?";

        when(credentialsService.getSecurityQuestion(email)).thenReturn(Optional.of(securityQuestion));

        ResponseEntity<String> response = credentialsController.getSecurityQuestion(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(securityQuestion, response.getBody());
    }

    @Test
    public void testGetSecurityQuestion_NotFound() {
        String email = "unknown@example.com";

        when(credentialsService.getSecurityQuestion(email)).thenReturn(Optional.empty());

        ResponseEntity<String> response = credentialsController.getSecurityQuestion(email);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCheckSecurityAnswer_Success() {
        String email = "test@example.com";
        String securityAnswer = "Fluffy";

        when(credentialsService.checkSecurityAnswer(email, securityAnswer)).thenReturn(true);

        ResponseEntity<String> response = credentialsController.checkSecurityAnswer(Map.of("email", email, "securityAnswer", securityAnswer));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Security answer confirmed", response.getBody());
    }

    @Test
    public void testCheckSecurityAnswer_NotFound() {
        String email = "test@example.com";
        String securityAnswer = "WrongAnswer";

        when(credentialsService.checkSecurityAnswer(email, securityAnswer)).thenReturn(false);

        ResponseEntity<String> response = credentialsController.checkSecurityAnswer(Map.of("email", email, "securityAnswer", securityAnswer));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testResetPassword_Success() {
        String email = "test@dal.com";
        String newPassword = "Correct@Complexity123";

        when(credentialsService.resetPassword(email, newPassword)).thenReturn(true);

        ResponseEntity<String> response = credentialsController.resetPassword(Map.of("email", email, "password", newPassword));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset successfully", response.getBody());
    }

    @Test
    public void testResetPassword_WeakPassword() {
        String email = "test@dal.ca.com";
        String newPassword = "weak";

        ResponseEntity<String> response = credentialsController.resetPassword(Map.of("email", email, "password", newPassword));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password does not meet the required criteria.", response.getBody());
    }

    @Test
    public void testLogin_Success() {
        String email = "test@example.com";
        String password = "password";
        int userId = 1;
        Credentials credentials = new Credentials();
        credentials.setUser_id(userId);

        when(credentialsService.authenticate(email, password)).thenReturn(credentials);

        ResponseEntity<Integer> response = credentialsController.login(Map.of("email", email, "password", password));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userId, response.getBody());
    }

    @Test
    public void testLogin_Unauthorized() {
        String email = "test@example.com";
        String password = "wrongPassword";

        when(credentialsService.authenticate(email, password)).thenReturn(null);

        ResponseEntity<Integer> response = credentialsController.login(Map.of("email", email, "password", password));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
