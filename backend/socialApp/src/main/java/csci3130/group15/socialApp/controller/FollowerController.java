package csci3130.group15.socialApp.controller;


import csci3130.group15.socialApp.model.Follower;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.service.FollowerService;
import csci3130.group15.socialApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/followers")
public class FollowerController {

    @Autowired
    private FollowerService followerService;

    @Autowired
    private UserService userService;

    @PostMapping("/follow")
    public Follower followUser(@RequestParam int userId, @RequestParam int followerId) {
        User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User follower = userService.findById(followerId).orElseThrow(() -> new RuntimeException("Follower not found"));
        return followerService.followUser(user, follower);
    }

    @PostMapping("/unfollow")
    public void unfollowUser(@RequestParam int userId, @RequestParam int followerId) {
        User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User follower = userService.findById(followerId).orElseThrow(() -> new RuntimeException("Follower not found"));
        followerService.unfollowUser(user, follower);
    }

    @GetMapping("/isFollowing")
    public ResponseEntity<Boolean> isFollowing(@RequestParam int userId, @RequestParam int followerId) {
        User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User follower = userService.findById(followerId).orElseThrow(() -> new RuntimeException("Follower not found"));
        boolean isFollowing = followerService.isFollowing(user, follower);
        return ResponseEntity.ok(isFollowing);
    }

    @GetMapping("/{userId}/followers")
    public List<Follower> getFollowers(@PathVariable int userId) {
        User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return followerService.getFollowers(user);
    }

    @GetMapping("/{followerId}/following")
    public List<Follower> getFollowing(@PathVariable int followerId) {
        User follower = userService.findById(followerId).orElseThrow(() -> new RuntimeException("Follower not found"));
        return followerService.getFollowing(follower);
    }
}
