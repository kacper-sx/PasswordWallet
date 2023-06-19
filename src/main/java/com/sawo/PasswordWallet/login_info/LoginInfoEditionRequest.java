package com.sawo.PasswordWallet.login_info;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfoEditionRequest {

    private String newPassword;
}
