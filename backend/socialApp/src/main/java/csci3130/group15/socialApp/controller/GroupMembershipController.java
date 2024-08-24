package csci3130.group15.socialApp.controller;

import csci3130.group15.socialApp.model.Group;
import csci3130.group15.socialApp.model.GroupMembership;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.service.GroupMembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memberships")
public class GroupMembershipController {
    @Autowired
    private GroupMembershipService groupMembershipService;

    @PostMapping("/requestToJoin")
    public GroupMembership requestToJoin(
            @RequestParam Long groupId,
            @RequestParam int userId) {
        Group group = new Group();
        group.setId(groupId);
        User user = new User();
        user.setUser_id(userId);

        GroupMembership groupMembership = new GroupMembership();
        groupMembership.setGroup(group);
        groupMembership.setUser(user);
        groupMembership.setApproved(false);

        return groupMembershipService.requestToJoin(groupMembership);
    }

    @GetMapping("/group/{groupId}")
    public List<GroupMembership> getGroupMembers(@PathVariable Long groupId, @RequestParam boolean isApproved) {
        return groupMembershipService.findByGroupId(groupId, isApproved);
    }

    @GetMapping("/user/{userId}")
    public List<GroupMembership> getUserMemberships(@PathVariable int userId) {
        return groupMembershipService.findByUserId(userId);
    }

    @PutMapping("/approve")
    public GroupMembership approveRequest(@RequestBody GroupMembership groupMembership) {
        return groupMembershipService.approveRequest(groupMembership);
    }

    @DeleteMapping("/{id}")
    public void denyRequest(@PathVariable Long id) {
        groupMembershipService.denyRequest(id);
    }
}
