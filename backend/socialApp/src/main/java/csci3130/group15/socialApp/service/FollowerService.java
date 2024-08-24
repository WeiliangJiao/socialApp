package csci3130.group15.socialApp.service;

import csci3130.group15.socialApp.model.Follower;
import csci3130.group15.socialApp.model.User;

import java.util.List;

public interface FollowerService {
    Follower followUser(User user, User follower);
    void unfollowUser(User user, User follower);
     boolean isFollowing(User user, User follower);
    List<Follower> getFollowers(User user);
    List<Follower> getFollowing(User follower);
}
