package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.GroupMembership;
import csci3130.group15.socialApp.repository.GroupMembershipRepository;
import csci3130.group15.socialApp.service.GroupMembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GroupMembershipServiceTest {

    @Mock
    private GroupMembershipRepository groupMembershipRepository;

    @InjectMocks
    private GroupMembershipServiceImpl groupMembershipService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRequestToJoin() {
        GroupMembership groupMembership = new GroupMembership();
        when(groupMembershipRepository.save(groupMembership)).thenReturn(groupMembership);

        GroupMembership createdMembership = groupMembershipService.requestToJoin(groupMembership);

        assertNotNull(createdMembership);
        verify(groupMembershipRepository, times(1)).save(groupMembership);
    }

    @Test
    void testFindByGroupId() {
        GroupMembership groupMembership1 = new GroupMembership();
        GroupMembership groupMembership2 = new GroupMembership();
        when(groupMembershipRepository.findByGroupIdAndIsApproved(1L, true)).thenReturn(Arrays.asList(groupMembership1, groupMembership2));

        List<GroupMembership> memberships = groupMembershipService.findByGroupId(1L, true);

        assertEquals(2, memberships.size());
        verify(groupMembershipRepository, times(1)).findByGroupIdAndIsApproved(1L, true);
    }

    @Test
    void testFindByUserId() {
        GroupMembership groupMembership1 = new GroupMembership();
        GroupMembership groupMembership2 = new GroupMembership();
        when(groupMembershipRepository.findByUserUserIdAndIsApproved(1, true)).thenReturn(Arrays.asList(groupMembership1, groupMembership2));

        List<GroupMembership> memberships = groupMembershipService.findByUserId(1);

        assertEquals(2, memberships.size());
        verify(groupMembershipRepository, times(1)).findByUserUserIdAndIsApproved(1, true);
    }

    @Test
    void testApproveRequest() {
        GroupMembership groupMembership = new GroupMembership();
        groupMembership.setApproved(false);
        when(groupMembershipRepository.save(groupMembership)).thenReturn(groupMembership);

        GroupMembership approvedMembership = groupMembershipService.approveRequest(groupMembership);

        assertTrue(approvedMembership.isApproved());
        verify(groupMembershipRepository, times(1)).save(groupMembership);
    }

    @Test
    void testDenyRequest() {
        doNothing().when(groupMembershipRepository).deleteById(1L);

        groupMembershipService.denyRequest(1L);

        verify(groupMembershipRepository, times(1)).deleteById(1L);
    }
}
