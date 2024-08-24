package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.GroupMembership;
import csci3130.group15.socialApp.repository.GroupMembershipRepository;
import csci3130.group15.socialApp.service.GroupMembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMembershipServiceImpl implements GroupMembershipService {
    @Autowired
    private GroupMembershipRepository groupMembershipRepository;

    @Override
    public GroupMembership requestToJoin(GroupMembership groupMembership) {
        return groupMembershipRepository.save(groupMembership);
    }

    @Override
    public List<GroupMembership> findByGroupId(Long groupId, boolean isApproved) {
        return groupMembershipRepository.findByGroupIdAndIsApproved(groupId, isApproved);
    }

    @Override
    public List<GroupMembership> findByUserId(int userId) {
        return groupMembershipRepository.findByUserUserIdAndIsApproved(userId, true);
    }

    @Override
    public GroupMembership approveRequest(GroupMembership groupMembership) {
        groupMembership.setApproved(true);
        return groupMembershipRepository.save(groupMembership);
    }

    @Override
    public void denyRequest(Long id) {
        groupMembershipRepository.deleteById(id);
    }
}
