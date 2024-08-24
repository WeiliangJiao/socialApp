package csci3130.group15.socialApp.repository;

import csci3130.group15.socialApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByUserId(int userId);
    List<User> findByUsernameContainingIgnoreCase(String username);
    @Query("SELECT u FROM User u WHERE u.userId NOT IN (SELECT f.user.userId FROM Follower f WHERE f.follower.userId = :userId)")
    List<User> findUsersNotFollowedBy(@Param("userId") int userId);
    List<User> findByIsActiveFalse();

}
