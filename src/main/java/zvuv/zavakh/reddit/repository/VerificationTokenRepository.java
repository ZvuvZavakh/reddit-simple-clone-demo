package zvuv.zavakh.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zvuv.zavakh.reddit.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
