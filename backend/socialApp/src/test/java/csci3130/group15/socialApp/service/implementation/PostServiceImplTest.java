package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.Post;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.model.Follower;
import csci3130.group15.socialApp.repository.PostRepository;
import csci3130.group15.socialApp.repository.FollowerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private FollowerRepository followerRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private User currentUser;
    private User followedUser;
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currentUser = new User();
        currentUser.setUser_id(1);
        currentUser.setUsername("currentUser");

        followedUser = new User();
        followedUser.setUser_id(2);
        followedUser.setUsername("followedUser");

        post = new Post();
        post.setId(1);
        post.setUser(followedUser);
        post.setContent("This is a post content.");
    }

    @Test
    void testSave() {
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postService.save(post);

        assertNotNull(result);
        assertEquals(post.getId(), result.getId());
        assertEquals(post.getContent(), result.getContent());
    }

    @Test
    void testGetPostsByFollowedUsers_followedUsersExist() {
        Follower follower = new Follower();
        follower.setUser(followedUser);
        follower.setFollower(currentUser);

        when(followerRepository.findByFollower(currentUser)).thenReturn(List.of(follower));
        when(postRepository.findByUserInOrderByTimePostedDesc(anyList())).thenReturn(List.of(post));

        List<Post> result = postService.getPostsByFollowedUsers(currentUser);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(post, result.get(0));
    }

    @Test
    void testGetPostsByFollowedUsers_noFollowedUsers() {
        when(followerRepository.findByFollower(currentUser)).thenReturn(List.of());
        when(postRepository.findByUserInOrderByTimePostedDesc(anyList())).thenReturn(List.of());

        List<Post> result = postService.getPostsByFollowedUsers(currentUser);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
