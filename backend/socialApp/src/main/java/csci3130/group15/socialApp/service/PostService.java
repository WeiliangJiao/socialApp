package csci3130.group15.socialApp.service;

import csci3130.group15.socialApp.model.Post;
import csci3130.group15.socialApp.model.User;

import java.util.List;

public interface PostService {
    Post save(Post post);
    List<Post> getPostsByFollowedUsers(User currentUser);
}
