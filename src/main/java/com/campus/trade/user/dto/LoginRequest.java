package com.campus.trade.user.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank private String account;
    @NotBlank private String password;
    @NotBlank private String role;
    private Boolean rememberMe = false;
}
