package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.Group;
import csci3130.group15.socialApp.repository.GroupMembershipRepository;
import csci3130.group15.socialApp.repository.GroupRepository;
import csci3130.group15.socialApp.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMembershipRepository groupMembershipRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGroup() {
        Group group = new Group();
        group.setName("Test Group");
        when(groupRepository.save(group)).thenReturn(group);

        Group createdGroup = groupService.createGroup(group);

        assertEquals("Test Group", createdGroup.getName());
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void testFindByMajor() {
        Group group1 = new Group();
        group1.setMajor("Computer Science");
        Group group2 = new Group();
        group2.setMajor("Computer Science");

        // Use ArgumentMatchers.anyString() to match any string argument
        when(groupRepository.findByMajorIgnoreCase(ArgumentMatchers.anyString())).thenReturn(Arrays.asList(group1, group2));

        // Test with different cases to ensure the method handles case insensitivity
        List<Group> groups = groupService.findByMajor("Computer Science");

        assertEquals(2, groups.size());
        verify(groupRepository, times(1)).findByMajorIgnoreCase("computer science");
    }


    @Test
    void testFindById() {
        Group group = new Group();
        group.setId(1L);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        Optional<Group> foundGroup = groupService.findById(1L);

        assertEquals(1L, foundGroup.get().getId());
        verify(groupRepository, times(1)).findById(1L);
    }
}
