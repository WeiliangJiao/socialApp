package csci3130.group15.socialApp.controllerTest;

import csci3130.group15.socialApp.controller.GroupMembershipController;
import csci3130.group15.socialApp.model.GroupMembership;
import csci3130.group15.socialApp.service.GroupMembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GroupMembershipControllerTest {

    @Mock
    private GroupMembershipService groupMembershipService;

    @InjectMocks
    private GroupMembershipController groupMembershipController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(groupMembershipController).build();
    }

    @Test
    void testRequestToJoin() throws Exception {
        GroupMembership groupMembership = new GroupMembership();

        when(groupMembershipService.requestToJoin(any(GroupMembership.class))).thenReturn(groupMembership);

        mockMvc.perform(post("/memberships/requestToJoin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("groupId", "1")
                        .param("userId", "1"))
                .andExpect(status().isOk());

        verify(groupMembershipService, times(1)).requestToJoin(any(GroupMembership.class));
    }

    @Test
    void testGetGroupMembers() throws Exception {
        GroupMembership groupMembership1 = new GroupMembership();
        GroupMembership groupMembership2 = new GroupMembership();

        when(groupMembershipService.findByGroupId(1L, true)).thenReturn(Arrays.asList(groupMembership1, groupMembership2));

        mockMvc.perform(get("/memberships/group/1")
                        .param("isApproved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(groupMembershipService, times(1)).findByGroupId(1L, true);
    }

    @Test
    void testGetUserMemberships() throws Exception {
        GroupMembership groupMembership1 = new GroupMembership();
        GroupMembership groupMembership2 = new GroupMembership();

        when(groupMembershipService.findByUserId(1)).thenReturn(Arrays.asList(groupMembership1, groupMembership2));

        mockMvc.perform(get("/memberships/user/1")
                        .param("isApproved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(groupMembershipService, times(1)).findByUserId(1);
    }

    @Test
    void testApproveRequest() throws Exception {
        GroupMembership groupMembership = new GroupMembership();
        groupMembership.setApproved(false);

        when(groupMembershipService.approveRequest(any(GroupMembership.class))).thenReturn(groupMembership);

        mockMvc.perform(put("/memberships/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"group\":{\"id\":1},\"user\":{\"userId\":1},\"isApproved\":false}"))
                .andExpect(status().isOk());

        verify(groupMembershipService, times(1)).approveRequest(any(GroupMembership.class));
    }

    @Test
    void testDenyRequest() throws Exception {
        doNothing().when(groupMembershipService).denyRequest(1L);

        mockMvc.perform(delete("/memberships/1"))
                .andExpect(status().isOk());

        verify(groupMembershipService, times(1)).denyRequest(1L);
    }
}
