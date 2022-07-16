package zvuv.zavakh.reddit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zvuv.zavakh.reddit.exception.RedditException;
import zvuv.zavakh.reddit.model.RefreshToken;
import zvuv.zavakh.reddit.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken generate() {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .createdDate(Instant.now())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public void validate(String token) {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RedditException("Invalid refresh token"));
    }

    public void delete(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
