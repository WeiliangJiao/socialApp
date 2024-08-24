package csci3130.group15.socialApp.controller;

import csci3130.group15.socialApp.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import csci3130.group15.socialApp.model.Credentials;
import org.springframework.http.HttpStatus;


import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class CredentialsController {

    private CredentialsService credentialsService;

    @Autowired
    public CredentialsController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    /**
     * This method gets the security question of the user
     *
     * @param email the email of the user
     * @return a ResponseEntity with the security question of the user and status 200
     * if the email is found,
     * otherwise return a ResponseEntity with status 404
     */
    @GetMapping("/users/get-security-question")
    public ResponseEntity<String> getSecurityQuestion(@RequestParam String email) {
        Optional<String> securityQuestionOpt = credentialsService.getSecurityQuestion(email);
        if (securityQuestionOpt.isPresent()) {
            return ResponseEntity.ok(securityQuestionOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * This method checks the security answer of the user
     *
     * @param request the request body containing the email and security answer
     * @return a ResponseEntity with status 200 if the security answer is correct,
     * otherwise return a ResponseEntity with status 404
     */
    @PostMapping("/users/check-security-answer")
    public ResponseEntity<String> checkSecurityAnswer(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String securityAnswer = request.get("securityAnswer");
        if (credentialsService.checkSecurityAnswer(email, securityAnswer)) {
            return ResponseEntity.ok("Security answer confirmed");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * This method resets the password of the user
     *
     * @param request the request body containing the email and new password
     * @return a ResponseEntity with status 200 if the password is reset successfully,
     * otherwise return a ResponseEntity with status 404
     */
    @PutMapping("/users/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("password");
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?=\\S+$).{8,}$";
        if (!Pattern.matches(pattern, newPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password does not meet the required criteria.");
        }

        boolean isResetSuccessful = credentialsService.resetPassword(email, newPassword);
        if (isResetSuccessful) {
            return ResponseEntity.ok("Password reset successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to reset password. Please try again.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Integer> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        Credentials authenticatedCredential = credentialsService.authenticate(email, password);
        if (authenticatedCredential != null) {
            return ResponseEntity.ok(authenticatedCredential.getUser_id());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
