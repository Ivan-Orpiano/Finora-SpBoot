package org.finora.finorabudgettrackerbackend.auth;

import org.finora.finorabudgettrackerbackend.account.Account;
import org.finora.finorabudgettrackerbackend.account.AccountRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

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

@RestController
@RequestMapping("/api")
public class AuthController {


    private final UserRepository users;
    private final AccountRepository accounts;
    private final PasswordEncoder encoder;
    private final JwtEncoder jwtEncoder;



}
