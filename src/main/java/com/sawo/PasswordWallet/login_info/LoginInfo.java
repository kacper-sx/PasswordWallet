package com.sawo.PasswordWallet.login_info;

import com.sawo.PasswordWallet.user.User;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "login_informations")
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    private String password;

    private String login;

    private String webAddress;

    private String description;

    private String AESKey;
}
