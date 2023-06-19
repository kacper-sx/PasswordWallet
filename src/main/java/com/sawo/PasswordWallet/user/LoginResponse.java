package com.sawo.PasswordWallet.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private Long userId;

    private String masterLogin;

    private String lastSuccessfulLoginDate;

    private String lastUnsuccessfulLoginDate;
}
