package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.Follower;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.repository.FollowerRepository;
import csci3130.group15.socialApp.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class FollowerServiceImpl  implements FollowerService {

    @Autowired
    private FollowerRepository followerRepository;


    @Override
    public Follower followUser(User user, User follower) {
        Follower newFollower = new Follower();
        newFollower.setUser(user);
        newFollower.setFollower(follower);

        List<Follower> mutualFollow = followerRepository.findByUser(follower)
                .stream()
                .filter(f -> f.getFollower().equals(user))
                .toList();

        return followerRepository.save(newFollower);
    }

    @Override
    public void unfollowUser(User user, User follower) {
        List<Follower> followRelation = followerRepository.findByUser(user)
                .stream()
                .filter(f -> f.getFollower().equals(follower))
                .toList();

        followRelation.forEach(f -> followerRepository.delete(f));
    }

    @Override
    public boolean isFollowing(User user, User follower) {
        return followerRepository.findByUser(user)
                .stream()
                .anyMatch(f -> f.getFollower().equals(follower));
    }

    @Override
    public List<Follower> getFollowers(User user) {
        return followerRepository.findByUser(user);
    }

    @Override
    public List<Follower> getFollowing(User follower) {
        return followerRepository.findByFollower(follower);
    }

}
