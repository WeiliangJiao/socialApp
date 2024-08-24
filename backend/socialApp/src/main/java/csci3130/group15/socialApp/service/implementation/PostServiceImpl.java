package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.Post;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.repository.PostRepository;
import csci3130.group15.socialApp.repository.FollowerRepository;
import csci3130.group15.socialApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private FollowerRepository followerRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, FollowerRepository followerRepository) {
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> getPostsByFollowedUsers(User currentUser) {
        List<User> followedUsers = followerRepository.findByFollower(currentUser)
                .stream()
                .map(f -> f.getUser())
                .collect(Collectors.toList());
        return postRepository.findByUserInOrderByTimePostedDesc(followedUsers);
    }

}
