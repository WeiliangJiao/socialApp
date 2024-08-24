package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.Credentials;
import csci3130.group15.socialApp.model.User;
import csci3130.group15.socialApp.service.CredentialsService;
import csci3130.group15.socialApp.service.RegistrationService;
import csci3130.group15.socialApp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class RegistrationServiceImpl implements RegistrationService {

  @Autowired
  private UserService userService;

  @Autowired
  private CredentialsService credentialsService;
  @Override
  @Transactional
  public void registerUser(String username, String email, String password, String securityQuestion, String securityAnswer) throws Exception {
    validateEmail(email);
    validatePassword(password);

    User user = new User();
    user.setUsername(username);
    user = userService.save(user);

    Credentials credentials = new Credentials();
    credentials.setEmail(email);
    credentials.setPassword(password);
    credentials.setSecurityQuestion(securityQuestion);
    credentials.setSecurityAnswer(securityAnswer);
    credentials.setUser(user);

    credentialsService.save(credentials);
  }

  public void validateEmail(String email) throws Exception {
    if (!email.endsWith("@dal.ca")) {
      throw new Exception("Registration is only allowed for Dalhousie employees and students.Please register with your dal email.");
    }
  }

  public void validatePassword(String password) throws Exception {
    String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?=\\S+$).{8,}$";
    if (!Pattern.matches(pattern, password)) {
      throw new Exception("Password must meet complexity requirements.");
    }
  }
}

