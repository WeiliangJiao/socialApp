package csci3130.group15.socialApp.service;

import csci3130.group15.socialApp.model.GroupMembership;

import java.util.List;

public interface GroupMembershipService {
    GroupMembership requestToJoin(GroupMembership groupMembership);
    List<GroupMembership> findByGroupId(Long groupId, boolean isApproved);
    List<GroupMembership> findByUserId(int userId);
    GroupMembership approveRequest(GroupMembership groupMembership);
    void denyRequest(Long id);
}
