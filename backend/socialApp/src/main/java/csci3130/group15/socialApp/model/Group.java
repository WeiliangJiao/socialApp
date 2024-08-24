package csci3130.group15.socialApp.model;

import jakarta.persistence.*;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "`group`")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "members"})
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String major;
    private boolean isPrivate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"administeredGroups", "groupMemberships"})
    private User admin;

    @OneToMany(mappedBy = "group")
    private Set<GroupMembership> members;

    public Group() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public Set<GroupMembership> getMembers() {
        return members;
    }

    public void setMembers(Set<GroupMembership> members) {
        this.members = members;
    }
}
