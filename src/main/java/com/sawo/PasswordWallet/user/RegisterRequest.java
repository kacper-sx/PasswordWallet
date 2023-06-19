package com.sawo.PasswordWallet.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String masterLogin;

    private String masterPassword;

    private String encodingType;
}
