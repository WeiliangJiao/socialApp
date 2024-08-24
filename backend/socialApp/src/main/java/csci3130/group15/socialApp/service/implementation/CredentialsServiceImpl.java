package csci3130.group15.socialApp.service.implementation;

import csci3130.group15.socialApp.model.Credentials;
import csci3130.group15.socialApp.repository.CredentialsRepository;
import csci3130.group15.socialApp.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialsServiceImpl implements CredentialsService {

    private CredentialsRepository credentialsRepository;

    @Autowired
    public CredentialsServiceImpl(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    /**
     * This method gets the security question of the user
     *
     * @param email the email of the user
     * @return the security question of the user if the email is found
     * otherwise return empty
     */
    @Override
    public Optional<String> getSecurityQuestion(String email) {
        Optional<Credentials> credentialsOpt = credentialsRepository.findByEmail(email);
        if (credentialsOpt.isPresent()) {
            Credentials credentials = credentialsOpt.get();
            return Optional.of(credentials.getSecurityQuestion());
        }
        return Optional.empty();
    }

    @Override
    public boolean checkSecurityAnswer(String email, String securityAnswer) {
        Optional<Credentials> credentialsOpt = credentialsRepository.findByEmail(email);
        if (credentialsOpt.isPresent()) {
            Credentials credentials = credentialsOpt.get();
            String dbSecurityAnswer = credentials.getSecurityAnswer();
            return dbSecurityAnswer.equals(securityAnswer);
        }
        return false;
    }

    /**
     * This method resets the password of the user
     *
     * @param email the email of the user
     * @param newPassword the new password of the user
     * @return true if the password is reset successfully
     * return false otherwise
     */
    @Override
    public boolean resetPassword(String email, String newPassword) {
        Optional<Credentials> credentialsOpt = credentialsRepository.findByEmail(email);
        if (credentialsOpt.isPresent()) {
            Credentials credentials = credentialsOpt.get();
            credentials.setPassword(newPassword);
            credentialsRepository.save(credentials);
            return true;
        }
        return false;
    }

    @Override
    public Credentials authenticate(String email, String password) {
        return credentialsRepository.findByEmail(email)
                .filter(credential -> password.equals(credential.getPassword()))
                .orElse(null);
    }

    @Override
    public Credentials save(Credentials credentials) {
        return credentialsRepository.save(credentials);
    }
}
