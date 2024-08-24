package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.Group;
import csci3130.group15.socialApp.model.GroupMembership;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.repository.GroupRepository;
import csci3130.group15.socialApp.repository.UserRepository;
import csci3130.group15.socialApp.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import csci3130.group15.socialApp.repository.GroupMembershipRepository;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMembershipRepository groupMembershipRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Group createGroup(Group group) {
        User admin = group.getAdmin();

        Group createdGroup = groupRepository.save(group);

        GroupMembership membership = new GroupMembership();
        membership.setGroup(createdGroup);
        membership.setUser(admin);
        membership.setApproved(true);
        groupMembershipRepository.save(membership);

        return createdGroup;
    }
    @Override
    public List<Group> findByMajor(String major) {
        String normalizedMajor = major.trim().toLowerCase();
        return groupRepository.findByMajorIgnoreCase(normalizedMajor);
    }

    @Override
    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public List<Group> searchByKeyword(String keyword) {
        return groupRepository.searchByKeyword(keyword);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public List<Group> findByMajorAndKeyword(String major, String keyword) {
        String normalizedMajor = major.trim().toLowerCase();
        String normalizedKeyword = keyword.trim().toLowerCase();
        return groupRepository.findByMajorAndNameContainingIgnoreCase(normalizedMajor, normalizedKeyword);
    }

}
