package csci3130.group15.socialApp.controller;

import csci3130.group15.socialApp.service.RegistrationService;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import csci3130.group15.socialApp.repository.UserRepository;


import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private RegistrationService registrationService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;


  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestParam String username,
                       @RequestParam String email,
                       @RequestParam String password,
                       @RequestParam String securityQuestion,
                       @RequestParam String securityAnswer) {
    try {
      registrationService.registerUser(username, email, password, securityQuestion, securityAnswer);
      return ResponseEntity.ok("Thank you! User registered successfully!");    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during registration: " + e.getMessage());
    }
  }

  @GetMapping("/search")
  public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
    List<User> users = userService.searchUsers(query);
    // Filter out inactive users
    List<User> activeUsers = users.stream()
            .filter(User::isIs_active)
            .collect(Collectors.toList());
    System.out.println("Active users found: " + activeUsers.size());
    return ResponseEntity.ok(activeUsers);
  }

  @GetMapping("/public/search")
  public ResponseEntity<List<User>> searchPublicUsers(@RequestParam String query) {
    List<User> users = userService.searchPublicUsers(query);
    List<User> activeUsers = users.stream()
            .filter(User::isIs_active)
            .collect(Collectors.toList());
    return ResponseEntity.ok(activeUsers);
  }

  @GetMapping("/{userId}/followers/search")
  public ResponseEntity<List<User>> searchFriends(@PathVariable int userId, @RequestParam String query) {
    List<User> followers = userService.searchFollowers(userId, query);
    List<User> following = userService.searchFollowing(userId, query);

    // Combine followers and following lists
    Set<User> friends = new HashSet<>();
    friends.addAll(followers);
    friends.addAll(following);

    List<User> activeFriends = friends.stream()
            .filter(User::isIs_active)
            .collect(Collectors.toList());

    return ResponseEntity.ok(activeFriends);
  }


  @GetMapping("/{userId}/status")
  public ResponseEntity<String> getUserStatus(@PathVariable int userId) {
    Optional<User> userOpt = userService.findById(userId);
    if (userOpt.isPresent()) {
      return ResponseEntity.ok(userOpt.get().getStatus());
    }
    else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @PutMapping("/{userId}/status")
  public ResponseEntity<String> updateUserStatus(@PathVariable int userId, @RequestBody Map<String, String> statusRequest) {
    String status = statusRequest.get("status");
    Optional<User> userOpt = userService.updateUserStatus(userId, status);
    if (userOpt.isPresent()) {
      return ResponseEntity.ok("Status updated successfully");
    }
    else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{userId}/profile")
  public ResponseEntity<String> updateUserProfile(@PathVariable int userId, @RequestBody User updatedUser) {
    Optional<User> userOpt = userRepository.findByUserId(userId);
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      user.setName(updatedUser.getName());
      user.setUsername(updatedUser.getUsername());
      user.setInterests(updatedUser.getInterests());
      userRepository.save(user);
      return ResponseEntity.ok("Profile updated successfully");
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{userId}/profile")
  public ResponseEntity<User> getUserProfile(@PathVariable int userId) {
    Optional<User> userOpt = userRepository.findByUserId(userId);
    if (userOpt.isPresent()) {
      return ResponseEntity.ok(userOpt.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }


  @GetMapping("/recommendations/{userId}")
  public ResponseEntity<List<User>> getRecommendations(@PathVariable int userId) {
    List<User> recommendations = userService.getRecommendations(userId);
    return ResponseEntity.ok(recommendations);
  }

  public ResponseEntity<List<User>> findUsersNotFollowedBy(@PathVariable int followerId) {
    List<User> users = userService.findUsersNotFollowedBy(followerId);
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{userId}/is_active")
  public ResponseEntity<Boolean> isActive(@PathVariable int userId) {
    Optional<User> userOpt = userService.findById(userId);
    if (userOpt.isPresent()) {
      return ResponseEntity.ok(userOpt.get().isIs_active());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping("/inactive")
  public ResponseEntity<List<User>> getInactiveUsers() {
    List<User> inactiveUsers = userService.findInactiveUsers();
    return ResponseEntity.ok(inactiveUsers);
  }

  @PutMapping("/{userId}/active-status")
  public ResponseEntity<String> updateUserActiveStatus(@PathVariable int userId, @RequestBody Map<String, Boolean> statusRequest) {
    boolean isActive = statusRequest.get("is_active");
    boolean updated = userService.updateUserActiveStatus(userId, isActive);
    if (updated) {
      return ResponseEntity.ok("User active status updated successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<String> rejectUser(@PathVariable int userId) {
    boolean deleted = userService.rejectUser(userId);
    if (deleted) {
      return ResponseEntity.ok("User rejected successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }
  @GetMapping("/{userId}/isAdmin")
  public ResponseEntity<Boolean> isAdmin(@PathVariable int userId) {
    Optional<User> userOpt = userService.findById(userId);
    if (userOpt.isPresent() && userOpt.get().getCredentials().isIs_admin()) {
      return ResponseEntity.ok(true);
    } else {
      return ResponseEntity.ok(false);
    }
  }
}