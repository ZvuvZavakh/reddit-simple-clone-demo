package zvuv.zavakh.reddit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zvuv.zavakh.reddit.dto.AuthenticationResponse;
import zvuv.zavakh.reddit.dto.LoginRequest;
import zvuv.zavakh.reddit.dto.RefreshTokenRequest;
import zvuv.zavakh.reddit.dto.SignupRequest;
import zvuv.zavakh.reddit.exception.RedditException;
import zvuv.zavakh.reddit.model.NotificationEmail;
import zvuv.zavakh.reddit.model.RefreshToken;
import zvuv.zavakh.reddit.model.User;
import zvuv.zavakh.reddit.model.VerificationToken;
import zvuv.zavakh.reddit.repository.UserRepository;
import zvuv.zavakh.reddit.repository.VerificationTokenRepository;
import zvuv.zavakh.reddit.security.JwtProvider;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthService(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            VerificationTokenRepository verificationTokenRepository,
            MailService mailService,
            AuthenticationManager authenticationManager,
            JwtProvider jwtProvider,
            RefreshTokenService refreshTokenService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public void signup(SignupRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .createdDate(Instant.now())
                .isEnabled(false)
                .build();

        userRepository.save(user);

        String token = generateVerificationToken(user);
        NotificationEmail notificationEmail = new NotificationEmail(
                "Please Activate Account!",
                user.getEmail(),
                "LINK: http://localhost:8081/api/auth/accountVerification/" + token
        );

        mailService.sendMail(notificationEmail);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .build();

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RedditException("Invalid Token"));

        enableUserByToken(verificationToken);
    }

    @Transactional
    public void enableUserByToken(VerificationToken verificationToken) {
        String email = verificationToken.getUser().getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RedditException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        return AuthenticationResponse.builder()
                .message("Login successful")
                .authenticationToken(jwtProvider.generateToken(authentication))
                .refreshToken(refreshTokenService.generate().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .email(loginRequest.getEmail())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validate(refreshTokenRequest.getToken());

        String token = jwtProvider.generateTokenWithEmail(refreshTokenRequest.getEmail());

        return AuthenticationResponse.builder()
                .message("Refresh successful")
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .email(refreshTokenRequest.getEmail())
                .build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByEmail(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.isAuthenticated();
    }
}
