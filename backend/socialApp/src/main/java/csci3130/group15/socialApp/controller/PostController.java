package csci3130.group15.socialApp.controller;

import csci3130.group15.socialApp.model.Post;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.service.PostService;
import csci3130.group15.socialApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private PostService postService;
    private UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/addpost/{userId}")
    public Post addPost(@PathVariable("userId") int userId, @RequestBody Post post) {
        User currentUser = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        post.setUser(currentUser);  // Set the user for the post
        return postService.save(post);
    }

    @GetMapping("/feed/{userId}")
    public List<Post> getFeed(@PathVariable int userId) {
        User currentUser = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return postService.getPostsByFollowedUsers(currentUser);
    }

}
