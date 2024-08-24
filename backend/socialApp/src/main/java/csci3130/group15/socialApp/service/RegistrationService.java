package csci3130.group15.socialApp.service;

public interface RegistrationService {
  void registerUser(String username, String email, String password, String securityQuestion, String securityAnswer) throws Exception;
}
