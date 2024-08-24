package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.Credentials;
import csci3130.group15.socialApp.repository.CredentialsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CredentialsServiceImplTest {

    @Mock
    private CredentialsRepository credentialsRepository;

    @InjectMocks
    private CredentialsServiceImpl credentialsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSecurityQuestion_emailFound() {
        String email = "user@example.com";
        String securityQuestion = "What is your pet's name?";
        Credentials credentials = new Credentials();
        credentials.setEmail(email);
        credentials.setSecurityQuestion(securityQuestion);

        when(credentialsRepository.findByEmail(email)).thenReturn(Optional.of(credentials));

        Optional<String> result = credentialsService.getSecurityQuestion(email);

        assertTrue(result.isPresent());
        assertEquals(securityQuestion, result.get());
    }

    @Test
    void testGetSecurityQuestion_emailNotFound() {
        String email = "user@example.com";

        when(credentialsRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<String> result = credentialsService.getSecurityQuestion(email);

        assertFalse(result.isPresent());
    }

    @Test
    void testCheckSecurityAnswer_correctAnswer() {
        String email = "user@example.com";
        String securityAnswer = "Fluffy";
        Credentials credentials = new Credentials();
        credentials.setEmail(email);
        credentials.setSecurityAnswer(securityAnswer);

        when(credentialsRepository.findByEmail(email)).thenReturn(Optional.of(credentials));

        boolean result = credentialsService.checkSecurityAnswer(email, securityAnswer);

        assertTrue(result);
    }

    @Test
    void testCheckSecurityAnswer_incorrectAnswer() {
        String email = "user@example.com";
        String securityAnswer = "Fluffy";
        String incorrectAnswer = "WrongAnswer";
        Credentials credentials = new Credentials();
        credentials.setEmail(email);
        credentials.setSecurityAnswer(securityAnswer);

        when(credentialsRepository.findByEmail(email)).thenReturn(Optional.of(credentials));

        boolean result = credentialsService.checkSecurityAnswer(email, incorrectAnswer);

        assertFalse(result);
    }

    @Test
    void testResetPassword_emailFound() {
        String email = "user@example.com";
        String newPassword = "newPassword";
        Credentials credentials = new Credentials();
        credentials.setEmail(email);
        credentials.setPassword("oldPassword");

        when(credentialsRepository.findByEmail(email)).thenReturn(Optional.of(credentials));
        when(credentialsRepository.save(any(Credentials.class))).thenReturn(credentials);

        boolean result = credentialsService.resetPassword(email, newPassword);

        assertTrue(result);
        assertEquals(newPassword, credentials.getPassword());
    }

    @Test
    void testResetPassword_emailNotFound() {
        String email = "user@example.com";
        String newPassword = "newPassword";

        when(credentialsRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = credentialsService.resetPassword(email, newPassword);

        assertFalse(result);
    }

    @Test
    void testAuthenticate_validCredentials() {
        String email = "user@example.com";
        String password = "password";
        Credentials credentials = new Credentials();
        credentials.setEmail(email);
        credentials.setPassword(password);

        when(credentialsRepository.findByEmail(email)).thenReturn(Optional.of(credentials));

        Credentials result = credentialsService.authenticate(email, password);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword());
    }

    @Test
    void testAuthenticate_invalidCredentials() {
        String email = "user@example.com";
        String password = "password";
        String invalidPassword = "wrongPassword";
        Credentials credentials = new Credentials();
        credentials.setEmail(email);
        credentials.setPassword(password);

        when(credentialsRepository.findByEmail(email)).thenReturn(Optional.of(credentials));

        Credentials result = credentialsService.authenticate(email, invalidPassword);

        assertNull(result);
    }

    @Test
    void testSave() {
        Credentials credentials = new Credentials();
        credentials.setEmail("user@example.com");
        credentials.setPassword("password");

        when(credentialsRepository.save(credentials)).thenReturn(credentials);

        Credentials result = credentialsService.save(credentials);

        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
        assertEquals("password", result.getPassword());
    }
}
