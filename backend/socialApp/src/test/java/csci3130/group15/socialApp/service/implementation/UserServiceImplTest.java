package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.Follower;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.repository.CredentialsRepository;
import csci3130.group15.socialApp.repository.UserRepository;
import csci3130.group15.socialApp.service.FollowerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowerService followerService;
    @Mock
    private CredentialsRepository credentialsRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUser_id(1);
        user.setUsername("testuser");
        user.setStatus("active");
        user.setName("Test User");
        user.setInterests("coding");
        user.setIs_active(true);
    }

    @Test
    void saveUser_ShouldReturnSavedUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User savedUser = userService.save(user);
        assertEquals(user, savedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveUser_ShouldCallRepositorySaveMethod() {
        userService.save(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findById_ShouldReturnUser() {
        when(userRepository.findByUserId(1)).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.findById(1);
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void findById_ShouldReturnEmpty() {
        when(userRepository.findByUserId(1)).thenReturn(Optional.empty());
        Optional<User> foundUser = userService.findById(1);
        assertFalse(foundUser.isPresent());
    }

    @Test
    void searchUsers_ShouldReturnUsers() {
        // Arrange
        User user1 = new User();
        user1.setUsername("testuser1");
        user1.setIs_active(true);

        User user2 = new User();
        user2.setUsername("testuser2");
        user2.setIs_active(true);

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findByUsernameContainingIgnoreCase("test")).thenReturn(users);

        // Act
        List<User> foundUsers = userService.searchPublicUsers("test");

        // Assert
        assertEquals(2, foundUsers.size(), "The size of the found users list should be 2");
        assertTrue(foundUsers.contains(user1), "The list should contain user1");
        assertTrue(foundUsers.contains(user2), "The list should contain user2");
    }

    @Test
    void searchUsers_ShouldReturnEmptyList() {
        when(userRepository.findByUsernameContainingIgnoreCase("nonexistent")).thenReturn(Arrays.asList());
        List<User> foundUsers = userService.searchUsers("nonexistent");
        assertTrue(foundUsers.isEmpty());
    }

    @Test
    void getRecommendations_ShouldReturnRecommendedUsers() {
        // Create the test user and recommended user
        User user = new User();
        user.setUser_id(1);

        User recommendedUser = new User();
        recommendedUser.setUser_id(2);
        recommendedUser.setIs_active(true); // Ensure the recommended user is active

        // Setup mock responses
        when(userRepository.findByUserId(1)).thenReturn(Optional.of(user));
        when(userRepository.findUsersNotFollowedBy(1)).thenReturn(Arrays.asList(recommendedUser));

        // Call the service method
        List<User> recommendations = userService.getRecommendations(1);

        // Assertions
        assertNotNull(recommendations);
        assertEquals(1, recommendations.size());
        assertEquals(recommendedUser, recommendations.get(0));
        assertTrue(recommendations.stream().allMatch(User::isIs_active)); // Ensure all recommended users are active
    }



    @Test
    void getRecommendations_UserNotFound() {
        when(userRepository.findByUserId(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getRecommendations(1);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void updateUserStatus_ShouldUpdateAndReturnUser() {
        when(userRepository.findByUserId(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Optional<User> updatedUser = userService.updateUserStatus(1, "inactive");
        assertTrue(updatedUser.isPresent());
        assertEquals("inactive", updatedUser.get().getStatus());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("inactive", userCaptor.getValue().getStatus());
    }

    @Test
    void updateUserStatus_UserNotFound() {
        when(userRepository.findByUserId(1)).thenReturn(Optional.empty());
        Optional<User> updatedUser = userService.updateUserStatus(1, "inactive");
        assertFalse(updatedUser.isPresent());
    }

    @Test
    void findUsersNotFollowedBy_ShouldReturnUsers() {
        List<User> users = Arrays.asList(user, new User());
        when(userRepository.findUsersNotFollowedBy(1)).thenReturn(users);
        List<User> foundUsers = userService.findUsersNotFollowedBy(1);
        assertEquals(2, foundUsers.size());
    }

    @Test
    void findUsersNotFollowedBy_ShouldReturnEmptyList() {
        when(userRepository.findUsersNotFollowedBy(1)).thenReturn(Arrays.asList());
        List<User> foundUsers = userService.findUsersNotFollowedBy(1);
        assertTrue(foundUsers.isEmpty());
    }

    @Test
    void findInactiveUsers_ShouldReturnInactiveUsers() {
        User inactiveUser1 = new User();
        inactiveUser1.setUser_id(2);
        inactiveUser1.setIs_active(false);

        User inactiveUser2 = new User();
        inactiveUser2.setUser_id(3);
        inactiveUser2.setIs_active(false);

        when(userRepository.findByIsActiveFalse()).thenReturn(Arrays.asList(inactiveUser1, inactiveUser2));

        List<User> inactiveUsers = userService.findInactiveUsers();

        assertEquals(2, inactiveUsers.size());
        assertFalse(inactiveUsers.get(0).isIs_active());
        assertFalse(inactiveUsers.get(1).isIs_active());
    }

    @Test
    void updateUserActiveStatus_ShouldUpdateStatus() {
        int userId = 1;
        boolean isActive = true;

        User inactiveUser = new User();
        inactiveUser.setUser_id(userId);
        inactiveUser.setIs_active(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(inactiveUser));

        boolean result = userService.updateUserActiveStatus(userId, isActive);

        assertTrue(result);
        assertTrue(inactiveUser.isIs_active());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(inactiveUser);
    }

    @Test
    void updateUserActiveStatus_UserNotFound() {
        int userId = 1;
        boolean isActive = true;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        boolean result = userService.updateUserActiveStatus(userId, isActive);

        assertFalse(result);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void rejectUser_ShouldDeleteUserAndCredentials() {
        int userId = 1;

        when(userRepository.existsById(userId)).thenReturn(true);

        boolean result = userService.rejectUser(userId);

        assertTrue(result);
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
        verify(credentialsRepository, times(1)).deleteByUser_UserId(userId);
    }

    @Test
    void rejectUser_UserNotFound() {
        int userId = 1;

        when(userRepository.existsById(userId)).thenReturn(false);

        boolean result = userService.rejectUser(userId);

        assertFalse(result);
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(0)).deleteById(userId);
        verify(credentialsRepository, times(0)).deleteByUser_UserId(userId);
    }

    @Test
    void searchPublicUsers_ShouldReturnActiveUsers() {
        User activeUser1 = new User();
        activeUser1.setUsername("testuser1");
        activeUser1.setIs_active(true);

        User inactiveUser = new User();
        inactiveUser.setUsername("testuser2");
        inactiveUser.setIs_active(false);

        List<User> users = Arrays.asList(activeUser1, inactiveUser);

        when(userRepository.findByUsernameContainingIgnoreCase("test")).thenReturn(users);

        List<User> result = userService.searchPublicUsers("test");

        assertEquals(1, result.size());
        assertEquals(activeUser1, result.get(0));
    }

    @Test
    void searchPublicUsers_ShouldReturnEmptyListForInactiveUsers() {
        User inactiveUser = new User();
        inactiveUser.setUsername("testuser2");
        inactiveUser.setIs_active(false);

        List<User> users = Arrays.asList(inactiveUser);

        when(userRepository.findByUsernameContainingIgnoreCase("test")).thenReturn(users);

        List<User> result = userService.searchPublicUsers("test");

        assertTrue(result.isEmpty());
    }
    @Test
    void searchFollowers_ShouldReturnActiveFollowerUsers() {
        int userId = 1;
        String query = "test";

        User follower1 = new User();
        follower1.setUsername("testuser1");
        follower1.setIs_active(true);
        Follower follower = new Follower();
        follower.setFollower(follower1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(followerService.getFollowers(user)).thenReturn(List.of(follower));

        List<User> result = userService.searchFollowers(userId, query);

        assertEquals(1, result.size());
        assertEquals(follower1, result.get(0));
    }

    @Test
    void searchFollowers_ShouldReturnEmptyForInactiveFollowerUsers() {
        int userId = 1;
        String query = "test";

        User follower1 = new User();
        follower1.setUsername("testuser1");
        follower1.setIs_active(false);
        Follower follower = new Follower();
        follower.setFollower(follower1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(followerService.getFollowers(user)).thenReturn(List.of(follower));

        List<User> result = userService.searchFollowers(userId, query);

        assertTrue(result.isEmpty());
    }

    @Test
    void searchFollowers_ShouldThrowExceptionWhenUserNotFound() {
        int userId = 1;
        String query = "test";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.searchFollowers(userId, query);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void searchFollowing_ShouldReturnActiveFollowingUsers() {
        int userId = 1;
        String query = "test";

        User following1 = new User();
        following1.setUsername("testuser1");
        following1.setIs_active(true);
        Follower following = new Follower();
        following.setFollower(following1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(followerService.getFollowing(user)).thenReturn(List.of(following));

        List<User> result = userService.searchFollowing(userId, query);

        assertEquals(1, result.size());
        assertEquals(following1, result.get(0));
    }

    @Test
    void searchFollowing_ShouldReturnEmptyForInactiveFollowingUsers() {
        int userId = 1;
        String query = "test";

        User following1 = new User();
        following1.setUsername("testuser1");
        following1.setIs_active(false);
        Follower following = new Follower();
        following.setFollower(following1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(followerService.getFollowing(user)).thenReturn(List.of(following));

        List<User> result = userService.searchFollowing(userId, query);

        assertTrue(result.isEmpty());
    }

    @Test
    void searchFollowing_ShouldThrowExceptionWhenUserNotFound() {
        int userId = 1;
        String query = "test";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.searchFollowing(userId, query);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getAllUsers_ShouldReturnUsers() {
        List<User> users = Arrays.asList(user, new User());
        when(userRepository.findAll()).thenReturn(users);
        List<User> allUsers = userService.getAllUsers();
        assertEquals(2, allUsers.size());
        assertEquals(users, allUsers);
    }
}
