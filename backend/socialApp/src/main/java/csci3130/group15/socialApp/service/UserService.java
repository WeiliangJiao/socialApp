package csci3130.group15.socialApp.service;

import csci3130.group15.socialApp.model.User;

import java.util.Optional;
import java.util.List;

public interface UserService {
  User save(User user);
  Optional<User> findById(int id);
  List<User> searchUsers(String query);
  List<User> searchPublicUsers(String query);
  List<User> searchFollowers(int userId, String query);
  List<User> searchFollowing(int userId, String query);

  List<User> getRecommendations(int userId);
  public List<User> findUsersNotFollowedBy(int followerId);
  Optional<User> updateUserStatus(int id, String status);
  List<User> findInactiveUsers();
  boolean updateUserActiveStatus(int userId, boolean isActive);
  boolean rejectUser(int userId);
  List<User> getAllUsers();
}

