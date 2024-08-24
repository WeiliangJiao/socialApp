package csci3130.group15.socialApp.repository;

import csci3130.group15.socialApp.model.Post;
import csci3130.group15.socialApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByUserInOrderByTimePostedDesc(List<User> users);

}
