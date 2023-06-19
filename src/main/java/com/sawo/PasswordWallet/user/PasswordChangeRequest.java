package com.sawo.PasswordWallet.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

    private Long userId;

    private String oldMasterPassword;

    private String newMasterPassword;
}
