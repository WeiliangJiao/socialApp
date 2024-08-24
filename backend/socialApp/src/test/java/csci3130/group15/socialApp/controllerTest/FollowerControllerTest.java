package csci3130.group15.socialApp.controllerTest;

import csci3130.group15.socialApp.controller.FollowerController;
import csci3130.group15.socialApp.model.Follower;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.service.FollowerService;
import csci3130.group15.socialApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class FollowerControllerTest {

    @Mock
    private FollowerService followerService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FollowerController followerController;

    @Test
    public void testFollowUser_Success() {
        int userId = 1;
        int followerId = 2;

        User user = new User();
        user.setUser_id(userId);

        User follower = new User();
        follower.setUser_id(followerId);

        Follower newFollower = new Follower();
        newFollower.setUser(user);
        newFollower.setFollower(follower);

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(userService.findById(followerId)).thenReturn(Optional.of(follower));
        when(followerService.followUser(user, follower)).thenReturn(newFollower);

        Follower response = followerController.followUser(userId, followerId);

        assertEquals(newFollower, response);
    }

    @Test
    public void testFollowUser_UserNotFound() {
        int userId = 1;
        int followerId = 2;

        when(userService.findById(userId)).thenReturn(Optional.empty());

        try {
            followerController.followUser(userId, followerId);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    public void testFollowUser_FollowerNotFound() {
        int userId = 1;
        int followerId = 2;

        User user = new User();
        user.setUser_id(userId);

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(userService.findById(followerId)).thenReturn(Optional.empty());

        try {
            followerController.followUser(userId, followerId);
        } catch (RuntimeException e) {
            assertEquals("Follower not found", e.getMessage());
        }
    }

    @Test
    public void testUnfollowUser_Success() {
        int userId = 1;
        int followerId = 2;

        User user = new User();
        user.setUser_id(userId);

        User follower = new User();
        follower.setUser_id(followerId);

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(userService.findById(followerId)).thenReturn(Optional.of(follower));
        doNothing().when(followerService).unfollowUser(user, follower);

        followerController.unfollowUser(userId, followerId);
    }

    @Test
    public void testUnfollowUser_UserNotFound() {
        int userId = 1;
        int followerId = 2;

        when(userService.findById(userId)).thenReturn(Optional.empty());

        try {
            followerController.unfollowUser(userId, followerId);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    public void testUnfollowUser_FollowerNotFound() {
        int userId = 1;
        int followerId = 2;

        User user = new User();
        user.setUser_id(userId);

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(userService.findById(followerId)).thenReturn(Optional.empty());

        try {
            followerController.unfollowUser(userId, followerId);
        } catch (RuntimeException e) {
            assertEquals("Follower not found", e.getMessage());
        }
    }

    @Test
    public void testIsFollowing_Success() {
        int userId = 1;
        int followerId = 2;

        User user = new User();
        user.setUser_id(userId);

        User follower = new User();
        follower.setUser_id(followerId);

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(userService.findById(followerId)).thenReturn(Optional.of(follower));
        when(followerService.isFollowing(user, follower)).thenReturn(true);

        ResponseEntity<Boolean> response = followerController.isFollowing(userId, followerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testIsFollowing_UserNotFound() {
        int userId = 1;
        int followerId = 2;

        when(userService.findById(userId)).thenReturn(Optional.empty());

        try {
            followerController.isFollowing(userId, followerId);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    public void testIsFollowing_FollowerNotFound() {
        int userId = 1;
        int followerId = 2;

        User user = new User();
        user.setUser_id(userId);

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(userService.findById(followerId)).thenReturn(Optional.empty());

        try {
            followerController.isFollowing(userId, followerId);
        } catch (RuntimeException e) {
            assertEquals("Follower not found", e.getMessage());
        }
    }

    @Test
    public void testGetFollowers_Success() {
        int userId = 1;

        User user = new User();
        user.setUser_id(userId);

        List<Follower> followers = List.of(new Follower(), new Follower());

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(followerService.getFollowers(user)).thenReturn(followers);

        List<Follower> response = followerController.getFollowers(userId);

        assertEquals(followers.size(), response.size());
    }

    @Test
    public void testGetFollowers_UserNotFound() {
        int userId = 1;

        when(userService.findById(userId)).thenReturn(Optional.empty());

        try {
            followerController.getFollowers(userId);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    public void testGetFollowing_Success() {
        int followerId = 1;

        User follower = new User();
        follower.setUser_id(followerId);

        List<Follower> following = List.of(new Follower(), new Follower());

        when(userService.findById(followerId)).thenReturn(Optional.of(follower));
        when(followerService.getFollowing(follower)).thenReturn(following);

        List<Follower> response = followerController.getFollowing(followerId);

        assertEquals(following.size(), response.size());
    }

    @Test
    public void testGetFollowing_FollowerNotFound() {
        int followerId = 1;

        when(userService.findById(followerId)).thenReturn(Optional.empty());

        try {
            followerController.getFollowing(followerId);
        } catch (RuntimeException e) {
            assertEquals("Follower not found", e.getMessage());
        }
    }
}

