package csci3130.group15.socialApp.repository;

import csci3130.group15.socialApp.model.Follower;
import csci3130.group15.socialApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Integer> {
    List<Follower> findByUser(User user);
    List<Follower> findByFollower(User follower);
    Follower findByUserAndFollower(User user, User follower);
}
