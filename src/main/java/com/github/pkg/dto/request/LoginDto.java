package com.github.pkg.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginDto {

    @NotNull(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String emailAddress;

    @NotNull(message = "Password is required")
    @Length(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
