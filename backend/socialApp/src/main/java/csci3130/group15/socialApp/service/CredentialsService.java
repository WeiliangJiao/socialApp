package csci3130.group15.socialApp.service;

import csci3130.group15.socialApp.model.Credentials;

import java.util.Optional;

import csci3130.group15.socialApp.model.Credentials;


public interface CredentialsService {

    Optional<String> getSecurityQuestion(String email);
    boolean checkSecurityAnswer(String email, String securityAnswer);
    boolean resetPassword(String email, String newPassword);
    Credentials authenticate(String email, String password);
    Credentials save (Credentials credentials);
}
