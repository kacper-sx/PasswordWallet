package com.sawo.PasswordWallet.login_info;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginInfoReadDTO {

    private Long id;

    private String login;

    private String password;

    private String webAddress;

    private String description;
}
