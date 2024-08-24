package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.Follower;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.repository.CredentialsRepository;
import csci3130.group15.socialApp.repository.UserRepository;
import csci3130.group15.socialApp.service.FollowerService;
import csci3130.group15.socialApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private FollowerService followerService;
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findByUserId(id);
    }

    @Override
    public List<User> searchUsers(String query) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(query);
        return users.stream()
                .filter(User::isIs_active)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> searchPublicUsers(String query) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(query);
        return users.stream()
                .filter(User::isIs_active)
                .collect(Collectors.toList());
    }
    @Override
    public List<User> searchFollowers(int userId, String query) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Follower> followers = followerService.getFollowers(user);
        List<User> followerUsers = followers.stream()
                .map(Follower::getFollower)
                .collect(Collectors.toList());
        return followerUsers.stream()
                .filter(u -> u.getUsername().contains(query) && u.isIs_active())
                .collect(Collectors.toList());
    }


    @Override
    public List<User> searchFollowing(int userId, String query) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Follower> following = followerService.getFollowing(user);

        System.out.println("Following size: " + following.size()); // Debugging line

        List<User> followingUsers = following.stream()
                .map(Follower::getFollower)
                .collect(Collectors.toList());

        System.out.println("Following users size: " + followingUsers.size()); // Debugging line

        return followingUsers.stream()
                .filter(u -> u.getUsername().contains(query) && u.isIs_active())
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getRecommendations(int userId) {
        // Fetch the current user to check if they exist
        User currentUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Fetch recommended users who are not followed by the current user
        List<User> recommendedUsers = userRepository.findUsersNotFollowedBy(userId);

        // Filter out inactive users
        return recommendedUsers.stream()
                .filter(User::isIs_active)
                .collect(Collectors.toList());
    }


    public List<User> findUsersNotFollowedBy(int followerId) {
        return userRepository.findUsersNotFollowedBy(followerId);
    }

    @Override
    public Optional<User> updateUserStatus(int id, String status) {
        Optional<User> userOpt = userRepository.findByUserId(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus(status);
            userRepository.save(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findInactiveUsers() {
        return userRepository.findByIsActiveFalse();
    }

    @Override
    public boolean updateUserActiveStatus(int userId, boolean isActive) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIs_active(isActive);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean rejectUser(int userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            credentialsRepository.deleteByUser_UserId(userId);
            return true;
        }
        return false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}


