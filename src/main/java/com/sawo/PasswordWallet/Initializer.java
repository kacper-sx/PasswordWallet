package com.sawo.PasswordWallet;


import com.sawo.PasswordWallet.cryptography.AESService;
import com.sawo.PasswordWallet.cryptography.HMACService;
import com.sawo.PasswordWallet.login_info.LoginInfo;
import com.sawo.PasswordWallet.login_info.LoginInfoRepository;
import com.sawo.PasswordWallet.user.User;
import com.sawo.PasswordWallet.user.UserRepository;
import com.sawo.PasswordWallet.user_login_info.UserLoginInfo;
import com.sawo.PasswordWallet.user_login_info.UserLoginInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class Initializer {

    private final UserRepository userRepository;

    private final LoginInfoRepository loginInfoRepository;

    private final UserLoginInfoRepository userLoginInfoRepository;

    private final HMACService hmacService;

    private final AESService aesService;


    public Initializer(UserRepository userRepository, LoginInfoRepository loginInfoRepository, UserLoginInfoRepository userLoginInfoRepository,
                       HMACService hmacService, AESService aesService) {
        this.userRepository = userRepository;
        this.loginInfoRepository = loginInfoRepository;
        this.userLoginInfoRepository = userLoginInfoRepository;
        this.hmacService = hmacService;
        this.aesService = aesService;

        this.initialize();
    }

    private void initialize() {
        log.warn("INITIALIZATION START");

        String masterPassword = "sawo1234";
        String HMACKey = UUID.randomUUID().toString();
        String AESKey = UUID.randomUUID().toString().substring(0, 16);

        User user = User.builder()
                .masterLogin("kacpersaweczko@com.pl")
                .masterPasswordHash(this.hmacService.encode(masterPassword, HMACKey))
                .HMACKey(HMACKey)
                .AESKey(AESKey)
                .isPasswordEncodedWithHMAC(true)
                .build();
        this.userRepository.save(user);

        List<LoginInfo> loginInfoList = List.of(
                LoginInfo.builder().user(user).password(this.aesService.encode("ItsFinallyHashed1!", AESKey))
                        .login("login1").webAddress("xd.com").description("desc1").build(),
                LoginInfo.builder().user(user).password(this.aesService.encode("ItsFinallyHashed2!", AESKey))
                        .login("login2").webAddress("xd.com2").description("desc2").build()
        );
        this.loginInfoRepository.saveAll(loginInfoList);

//        List<UserLoginInfo> failedUserLoginInfos = List.of(
//                UserLoginInfo.builder().ipAddress("0:0:0:0:0:0:0:1").masterLogin("kacpersaweczko@com.pl")
//                        .loginTime(LocalDateTime.now().minusMinutes(3))
//                        .successful(false).countDuringLoginAttempt(true).build(),
//                UserLoginInfo.builder().ipAddress("0:0:0:0:0:0:0:1").masterLogin("kacpersaweczko@com.pl")
//                        .loginTime(LocalDateTime.now().minusMinutes(2))
//                        .successful(false).countDuringLoginAttempt(true).build(),
//                UserLoginInfo.builder().ipAddress("0:0:0:0:0:0:0:1").masterLogin("kacpersaweczko@com.pl")
//                        .loginTime(LocalDateTime.now().minusSeconds(1))
//                        .successful(false).countDuringLoginAttempt(true).build()
//        );
//        this.userLoginInfoRepository.saveAll(failedUserLoginInfos);


        log.warn("INITIALIZATION END");
    }
}
