package com.sawo.PasswordWallet.user_login_info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "user_service_login_informations")
@RequiredArgsConstructor
@AllArgsConstructor
public class UserLoginInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;

    private String masterLogin;

    private LocalDateTime loginTime;

    private boolean successful;

    private boolean countDuringLoginAttempt;
}
