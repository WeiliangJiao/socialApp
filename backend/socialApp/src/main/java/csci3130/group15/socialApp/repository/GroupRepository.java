package csci3130.group15.socialApp.repository;

import csci3130.group15.socialApp.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
//    List<Group> findByMajor(String major);
    @Query("SELECT g FROM Group g WHERE g.name LIKE %:keyword% OR g.major LIKE %:keyword%")
    List<Group> searchByKeyword(@Param("keyword") String keyword);
    List<Group> findByMajorIgnoreCase(String major);
    @Query("SELECT g FROM Group g WHERE LOWER(g.major) = LOWER(:major) AND LOWER(g.name) LIKE %:keyword%")
    List<Group> findByMajorAndNameContainingIgnoreCase(@Param("major") String major, @Param("keyword") String keyword);

}
