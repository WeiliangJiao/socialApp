package csci3130.group15.socialApp.repository;

import csci3130.group15.socialApp.model.GroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {
    List<GroupMembership> findByGroupIdAndIsApproved(Long groupId, boolean isApproved);
    List<GroupMembership> findByUserUserIdAndIsApproved(int userId, boolean isApproved);
}
