package csci3130.group15.socialApp.controller;

import csci3130.group15.socialApp.model.Group;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import csci3130.group15.socialApp.service.UserService;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @PostMapping
    public Group createGroup(
            @RequestParam String name,
            @RequestParam String major,
            @RequestParam boolean isPrivate,
            @RequestParam int adminId) {
        Optional<User> adminOptional = userService.findById(adminId);
        if (!adminOptional.isPresent()) {
            throw new IllegalArgumentException("Admin user not found: " + adminId);
        }
        User admin = adminOptional.get();

        Group group = new Group();
        group.setName(name);
        group.setMajor(major);
        group.setPrivate(isPrivate);
        group.setAdmin(admin);

        return groupService.createGroup(group);
    }

    @GetMapping("/search")
    public List<Group> searchGroups(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) Long id) {

        if (id != null) {
            return groupService.findById(id).map(List::of).orElseGet(List::of);
        } else if (major != null && !major.equals("All Majors")) {
            // Filter by major and keyword if both are present
            if (keyword != null && !keyword.isEmpty()) {
                return groupService.findByMajorAndKeyword(major, keyword);
            }
            // Filter by major only
            return groupService.findByMajor(major);
        } else if (keyword != null && !keyword.isEmpty()) {
            // Search by keyword only
            return groupService.searchByKeyword(keyword);
        } else {
            // Return all groups if no major or keyword is provided
            return groupService.getAllGroups();
        }
    }

    @GetMapping("/{id}")
    public Optional<Group> getGroupById(@PathVariable Long id) {
        return groupService.findById(id);
    }
}
