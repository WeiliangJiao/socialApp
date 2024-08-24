package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.Follower;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.repository.FollowerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FollowerServiceImplTest {

    @Mock
    private FollowerRepository followerRepository;

    @InjectMocks
    private FollowerServiceImpl followerService;

    private User user;
    private User follower;
    private Follower followerEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUser_id(1);
        user.setUsername("user");

        follower = new User();
        follower.setUser_id(2);
        follower.setUsername("follower");

        followerEntity = new Follower();
        followerEntity.setUser(user);
        followerEntity.setFollower(follower);
    }

    @Test
    void testFollowUser_success() {
        when(followerRepository.save(any(Follower.class))).thenReturn(followerEntity);
        when(followerRepository.findByUser(follower)).thenReturn(List.of());

        Follower result = followerService.followUser(user, follower);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(follower, result.getFollower());
    }

    @Test
    void testFollowUser_mutualFollow() {
        when(followerRepository.save(any(Follower.class))).thenReturn(followerEntity);
        when(followerRepository.findByUser(follower)).thenReturn(List.of(followerEntity));

        Follower result = followerService.followUser(user, follower);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(follower, result.getFollower());
    }

    @Test
    void testUnfollowUser_success() {
        when(followerRepository.findByUser(user)).thenReturn(List.of(followerEntity));

        followerService.unfollowUser(user, follower);

        verify(followerRepository, times(1)).delete(followerEntity);
    }

    @Test
    void testUnfollowUser_noRelation() {
        when(followerRepository.findByUser(user)).thenReturn(List.of());

        followerService.unfollowUser(user, follower);

        verify(followerRepository, never()).delete(any(Follower.class));
    }

    @Test
    void testIsFollowing_true() {
        when(followerRepository.findByUser(user)).thenReturn(List.of(followerEntity));

        boolean result = followerService.isFollowing(user, follower);

        assertTrue(result);
    }

    @Test
    void testIsFollowing_false() {
        when(followerRepository.findByUser(user)).thenReturn(List.of());

        boolean result = followerService.isFollowing(user, follower);

        assertFalse(result);
    }

    @Test
    void testGetFollowers() {
        when(followerRepository.findByUser(user)).thenReturn(List.of(followerEntity));

        List<Follower> result = followerService.getFollowers(user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(followerEntity, result.get(0));
    }

    @Test
    void testGetFollowers_empty() {
        when(followerRepository.findByUser(user)).thenReturn(List.of());

        List<Follower> result = followerService.getFollowers(user);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetFollowing() {
        when(followerRepository.findByFollower(follower)).thenReturn(List.of(followerEntity));

        List<Follower> result = followerService.getFollowing(follower);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(followerEntity, result.get(0));
    }

    @Test
    void testGetFollowing_empty() {
        when(followerRepository.findByFollower(follower)).thenReturn(List.of());

        List<Follower> result = followerService.getFollowing(follower);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
