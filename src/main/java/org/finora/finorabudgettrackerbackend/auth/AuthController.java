package org.finora.finorabudgettrackerbackend.auth;

import jakarta.transaction.Transactional;
import org.finora.finorabudgettrackerbackend.account.Account;
import org.finora.finorabudgettrackerbackend.account.AccountRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import org.finora.finorabudgettrackerbackend.user.User;
import org.finora.finorabudgettrackerbackend.user.UserRepository;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Locale;
import java.util.Locale;

@RestController
@RequestMapping("/api")
public class AuthController {


    private final UserRepository users;
    private final AccountRepository accounts;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    //constructor injection provides the required dependencies
    public AuthController(
        UserRepository users,
        AccountRepository accounts,
        PasswordEncoder passwordEncoder,
        JwtEncoder jwtEncoder
    ) {
        this.users = users;
        this.accounts = accounts;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    //DTO for register request
    public record RegisterRequest(
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotBlank @Size(min = 8) String password
    ) {}

    //DTO for login request
    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ){}

    //DTO for profile request
    public record ProfileRequest(
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotBlank String preferredCurrency,
            @Min(1) @Max(100) int alertThreshold
    ) {}

    //safe user information
    public record ProfileResponse(
            Long id,
            String name,
            String email,
            String preferredCurrency,
            int alerThreshold
    ){}

    public record AuthResponse(
        String token,
        ProfileResponse profile
    ) {}


    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private User currentUser(Jwt jwt) {
        return users.findById(Long.valueOf(jwt.getSubject()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,"User not found"));
    }

    private ProfileResponse toProfile(User user) {
        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPreferredCurrency(),
                user.getAlertThreshold()
        );
    }

    //create a signed JWT that expires after 12 hours
    private AuthResponse createSession(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("finora-budget-tracker")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(12 * 60 *60))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        String token = jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
        return new AuthResponse(token, toProfile(user));
    }

    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public AuthResponse register(

    ){}


}
