package csci3130.group15.socialApp.service;

import csci3130.group15.socialApp.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    Group createGroup(Group group);
    List<Group> findByMajor(String major);
    Optional<Group> findById(Long id);
    List<Group> searchByKeyword(String keyword);
    List<Group> getAllGroups();
    List<Group> findByMajorAndKeyword(String major, String keyword);

}
