package csci3130.group15.socialApp.model;

import jakarta.persistence.*;

@Entity
public class Credentials {

    @Id
    private int user_id;

    private String password;
    private boolean is_admin;
    private String email;
    private String security_question;
    private String security_answer;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public Credentials() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecurityQuestion() {
        return security_question;
    }

    public String getSecurityAnswer() {
        return security_answer;
    }

    public void setSecurityQuestion(String security_question) {
        this.security_question = security_question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSecurityAnswer(String security_answer) {
        this.security_answer = security_answer;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "user_id=" + user_id +
                ", password='" + password + '\'' +
                ", is_admin=" + is_admin +
                ", email='" + email + '\'' +
                ", security_question='" + security_question + '\'' +
                ", security_answer='" + security_answer + '\'' +
                ", user=" + user +
                '}';
    }
}
