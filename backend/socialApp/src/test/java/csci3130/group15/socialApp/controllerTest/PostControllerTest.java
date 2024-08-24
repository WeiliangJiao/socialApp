package csci3130.group15.socialApp.controllerTest;

import csci3130.group15.socialApp.controller.PostController;
import csci3130.group15.socialApp.model.Post;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.service.PostService;
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
public class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostController postController;

    @Test
    public void testAddPost_Success() {
        int userId = 1;
        Post post = new Post();
        User user = new User();
        user.setUser_id(userId);

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(postService.save(post)).thenReturn(post);

        Post response = postController.addPost(userId, post);

        assertEquals(post, response);
    }

    @Test
    public void testAddPost_UserNotFound() {
        int userId = 1;
        Post post = new Post();

        when(userService.findById(userId)).thenReturn(Optional.empty());

        try {
            postController.addPost(userId, post);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    public void testGetFeed_Success() {
        int userId = 1;
        User user = new User();
        user.setUser_id(userId);
        List<Post> posts = List.of(new Post(), new Post());

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(postService.getPostsByFollowedUsers(user)).thenReturn(posts);

        List<Post> response = postController.getFeed(userId);

        assertEquals(posts.size(), response.size());
    }

    @Test
    public void testGetFeed_UserNotFound() {
        int userId = 1;

        when(userService.findById(userId)).thenReturn(Optional.empty());

        try {
            postController.getFeed(userId);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }
    }
}
