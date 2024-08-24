package csci3130.group15.socialApp.repository;

import csci3130.group15.socialApp.model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Integer> {
        Optional<Credentials> findByEmail(String email);
        @Transactional
        void deleteByUser_UserId(int userId);
}
