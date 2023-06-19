package com.sawo.PasswordWallet.user;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "users")
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String masterLogin;

    private String masterPasswordHash;

    private String HMACKey;

    private String SHASalt;
    //private String SHAPepper;

    private String AESKey;

    private Boolean isPasswordEncodedWithHMAC;

//    private Boolean isLoggedIn;
//
//    private Boolean hasDecodedPasswordOnceDuringSession;

}
