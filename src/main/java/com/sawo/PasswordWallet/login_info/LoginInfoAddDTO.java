package com.sawo.PasswordWallet.login_info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfoAddDTO {

    private String login;

    private String password;

    private String webAddress;

    private String description;

    private String masterPassword;
}

