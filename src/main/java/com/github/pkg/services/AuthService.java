package com.github.pkg.services;

import com.github.pkg.domain.ApplicationUser;
import com.github.pkg.dto.request.LoginDto;
import com.github.pkg.dto.request.RegisterDto;
import com.github.pkg.exceptions.BadRequestException;
import com.github.pkg.exceptions.ForbiddenException;
import com.github.pkg.repositories.ApplicationUserRepository;
import com.github.pkg.security.JwtSecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private final ApplicationUserRepository userRepository;
    private final ApplicationUserService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtSecurityUtil jwtService;

    public ApplicationUser register(RegisterDto registrationDto) throws RuntimeException {
        Optional<ApplicationUser> emailUser = userRepository.findByEmailAddress(registrationDto.getEmailAddress());
        if (emailUser.isPresent()) {
            throw new BadRequestException("Email Address already in use.");
        }

        ApplicationUser user = new ApplicationUser();
        user.setEmailAddress(registrationDto.getEmailAddress());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRegistrationDate(LocalDate.now());

        return userRepository.save(user);
    }

    public Authentication login(LoginDto loginDto) {
        return authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getEmailAddress(), loginDto.getPassword())
        );
    }

    public String getAccessToken(String emailAddress) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(emailAddress);
        return jwtService.buildToken(userDetails.getUsername());
    }

    public void setAuthenticatedUser(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public UserDetails getAuthenticatedUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public ApplicationUser getAuthenticatedApplicationUser() {
        UserDetails userDetails = getAuthenticatedUserDetails();
        return userRepository.findByEmailAddress(userDetails.getUsername()).orElseThrow(() -> new ForbiddenException("User Not Found"));
    }
}
