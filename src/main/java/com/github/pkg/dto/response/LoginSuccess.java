package com.github.pkg.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginSuccess {
    private String message;
    private String accessToken;
}
