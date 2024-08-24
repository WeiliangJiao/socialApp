package csci3130.group15.socialApp.controllerTest;

import csci3130.group15.socialApp.controller.UserController;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.repository.UserRepository;
import csci3130.group15.socialApp.service.RegistrationService;
import csci3130.group15.socialApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private RegistrationService registrationService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    public void testSignup_Success() throws Exception {
        String username = "testUser";
        String email = "test@example.com";
        String password = "password";
        String securityQuestion = "What is your pet's name?";
        String securityAnswer = "Fluffy";

        doNothing().when(registrationService).registerUser(username, email, password, securityQuestion, securityAnswer);

        ResponseEntity<String> response = userController.signup(username, email, password, securityQuestion, securityAnswer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Thank you! User registered successfully!", response.getBody());
    }

    @Test
    public void testSignup_Failure() throws Exception {
        String username = "testUser";
        String email = "test@example.com";
        String password = "password";
        String securityQuestion = "What is your pet's name?";
        String securityAnswer = "Fluffy";

        doThrow(new Exception("Registration error")).when(registrationService).registerUser(username, email, password, securityQuestion, securityAnswer);

        ResponseEntity<String> response = userController.signup(username, email, password, securityQuestion, securityAnswer);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error during registration: Registration error", response.getBody());
    }


    @Test
    public void testGetUserStatus_Success() {
        int userId = 1;
        User user = new User();
        user.setStatus("active");

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userController.getUserStatus(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("active", response.getBody());
    }

    @Test
    public void testGetUserStatus_NotFound() {
        int userId = 1;

        when(userService.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = userController.getUserStatus(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUserStatus_Success() {
        int userId = 1;
        String status = "active";
        User user = new User();

        when(userService.updateUserStatus(userId, status)).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userController.updateUserStatus(userId, Map.of("status", status));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status updated successfully", response.getBody());
    }

    @Test
    public void testUpdateUserStatus_NotFound() {
        int userId = 1;
        String status = "active";

        when(userService.updateUserStatus(userId, status)).thenReturn(Optional.empty());

        ResponseEntity<String> response = userController.updateUserStatus(userId, Map.of("status", status));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUserProfile_Success() {
        int userId = 1;
        User updatedUser = new User();
        updatedUser.setName("New Name");
        updatedUser.setUsername("newusername");
        updatedUser.setInterests("New interests");

        User existingUser = new User();
        existingUser.setUser_id(userId);

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(existingUser));

        ResponseEntity<String> response = userController.updateUserProfile(userId, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile updated successfully", response.getBody());
    }

    @Test
    public void testUpdateUserProfile_NotFound() {
        int userId = 1;
        User updatedUser = new User();

        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = userController.updateUserProfile(userId, updatedUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUserProfile_Success() {
        int userId = 1;
        User user = new User();
        user.setUser_id(userId);

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserProfile(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserProfile_NotFound() {
        int userId = 1;

        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserProfile(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSearchUsers_Success() {
        String query = "test";

        // Create a list with active users only
        User activeUser1 = new User();
        activeUser1.setIs_active(true);  // Ensure the user is active

        User activeUser2 = new User();
        activeUser2.setIs_active(true);  // Ensure the user is active

        List<User> users = List.of(activeUser1, activeUser2);

        when(userService.searchUsers(query)).thenReturn(users);

        ResponseEntity<List<User>> response = userController.searchUsers(query);

        // Assert that the response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that all users in the response are active
        List<User> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(users.size(), responseBody.size());
        assertTrue(responseBody.stream().allMatch(User::isIs_active));
    }

    @Test
    public void testGetRecommendations_Success() {
        int userId = 1;
        List<User> recommendations = List.of(new User(), new User());

        when(userService.getRecommendations(userId)).thenReturn(recommendations);

        ResponseEntity<List<User>> response = userController.getRecommendations(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(recommendations.size(), response.getBody().size());
    }

    @Test
    public void testFindUsersNotFollowedBy_Success() {
        int followerId = 1;
        List<User> users = List.of(new User(), new User());

        when(userService.findUsersNotFollowedBy(followerId)).thenReturn(users);

        ResponseEntity<List<User>> response = userController.findUsersNotFollowedBy(followerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users.size(), response.getBody().size());
    }

    @Test
    public void testGetAllUsers_Success() {
        List<User> users = List.of(new User(), new User());

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users.size(), response.getBody().size());
        assertEquals(users, response.getBody());
    }
}
