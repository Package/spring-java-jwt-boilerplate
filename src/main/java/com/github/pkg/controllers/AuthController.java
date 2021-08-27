package com.github.pkg.controllers;

import com.github.pkg.dto.request.LoginDto;
import com.github.pkg.dto.request.RegisterDto;
import com.github.pkg.dto.response.LoginSuccess;
import com.github.pkg.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginSuccess> login(@Valid @RequestBody LoginDto loginDto) {
        Authentication authResult = authService.login(loginDto);
        if (!authResult.isAuthenticated()) {
            throw new BadCredentialsException("Email Address or Password is incorrect");
        }

        authService.setAuthenticatedUser(authResult);

        String accessToken = authService.getAccessToken(loginDto.getEmailAddress());
        String greeting = "Hello " + authResult.getName();

        return ResponseEntity.ok(new LoginSuccess(greeting, accessToken));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
        authService.register(registerDto);
        return ResponseEntity.ok("Success");
    }
}
