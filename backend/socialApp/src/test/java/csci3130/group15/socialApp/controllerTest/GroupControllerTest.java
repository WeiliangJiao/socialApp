package csci3130.group15.socialApp.controllerTest;

import csci3130.group15.socialApp.controller.GroupController;
import csci3130.group15.socialApp.model.Group;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import csci3130.group15.socialApp.service.UserService;

class GroupControllerTest {

    @Mock
    private GroupService groupService;

    @Mock
    private UserService userService;

    @InjectMocks
    private GroupController groupController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
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
    @Test
    void testSearchGroups() throws Exception {
        Group group1 = new Group();
        group1.setMajor("Computer Science");
        Group group2 = new Group();
        group2.setMajor("Computer Science");

        when(groupService.findByMajor("Computer Science")).thenReturn(Arrays.asList(group1, group2));

        mockMvc.perform(get("/groups/search")
                        .param("major", "Computer Science"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(groupService, times(1)).findByMajor("Computer Science");
    }

    @Test
    void testGetGroupById() throws Exception {
        Group group = new Group();
        group.setId(1L);

        when(groupService.findById(1L)).thenReturn(Optional.of(group));

        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(groupService, times(1)).findById(1L);
    }
}
