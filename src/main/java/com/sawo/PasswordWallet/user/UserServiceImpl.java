package com.sawo.PasswordWallet.user;

import com.sawo.PasswordWallet.cryptography.HMACService;
import com.sawo.PasswordWallet.cryptography.SHAService;
import com.sawo.PasswordWallet.cryptography.SharedMethods;
import com.sawo.PasswordWallet.exception_handler.*;
import com.sawo.PasswordWallet.user_login_info.UserLoginInfo;
import com.sawo.PasswordWallet.user_login_info.UserLoginInfoRepository;
import com.sawo.PasswordWallet.user_login_info.UserLoginInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String INCORRECT_CREDENTIALS = "Incorrect credentials!";

    private final UserRepository userRepository;

    private final SharedMethods sharedMethods;

    private final HMACService hmacService;

    private final SHAService shaService;

    private final UserLoginInfoService userLoginInfoService;

    @Override
    public LoginResponse login(LoginRequest loginRequest, String ipAddress) {

        LocalDateTime loginTime = LocalDateTime.now();

        this.userLoginInfoService.searchForConsecutiveFailedLogins(ipAddress, loginTime);

        Optional<User> userOptional = userRepository.findByMasterLogin(loginRequest.getMasterLogin());
        if (userOptional.isEmpty()) {
            this.handleUnsuccessfulLogin(ipAddress, loginRequest.getMasterLogin(), loginTime);
            throw new IncorrectCredentialsException(INCORRECT_CREDENTIALS);
        }

        final User user = userOptional.get();

        if(user.getIsPasswordEncodedWithHMAC()) {
            if (sharedMethods.passwordsAreMatching(loginRequest.getMasterPassword(), user.getHMACKey(), user.getMasterPasswordHash(), "HMAC")) {
                return this.handleSuccessfulLogin(user, ipAddress, loginTime);
            } else {
                this.handleUnsuccessfulLogin(ipAddress, user.getMasterLogin(), loginTime);
                throw new IncorrectCredentialsException(INCORRECT_CREDENTIALS);
            }
        } else {
            if (sharedMethods.passwordsAreMatching(loginRequest.getMasterPassword(), user.getSHASalt(), user.getMasterPasswordHash(), "SHA")) {
                return this.handleSuccessfulLogin(user, ipAddress, loginTime);
            } else {
                this.handleUnsuccessfulLogin(ipAddress, user.getMasterLogin(), loginTime);
                throw new IncorrectCredentialsException(INCORRECT_CREDENTIALS);
            }
        }
    }

    private LoginResponse handleSuccessfulLogin(User user, String ipAddress, LocalDateTime loginTime ) {
        this.userLoginInfoService.saveUserServiceLoginAttempt(ipAddress, user.getMasterLogin(), loginTime, true, false);

        UserLoginInfo lastSuccessfulLogin = this.userLoginInfoService.findLastFailedOrSuccessfulUserLogin(ipAddress, true);
        UserLoginInfo lastUnsuccessfulLogin = this.userLoginInfoService.findLastFailedOrSuccessfulUserLogin(ipAddress, false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LoginResponse.builder()
                .userId(user.getId())
                .masterLogin(user.getMasterLogin())
                .lastSuccessfulLoginDate(lastSuccessfulLogin != null ? lastSuccessfulLogin.getLoginTime().format(formatter) : "")
                .lastUnsuccessfulLoginDate(lastUnsuccessfulLogin != null ? lastUnsuccessfulLogin.getLoginTime().format(formatter) : "")
                .build();
    }

    private void handleUnsuccessfulLogin(String ipAddress, String masterLogin, LocalDateTime loginTime) {
        this.userLoginInfoService.saveUserServiceLoginAttempt(ipAddress, masterLogin, loginTime, false, true);
    }

    @Override
    public ServiceResponseMessage register(RegisterRequest registerRequest) {

        if(userRepository.findByMasterLogin(registerRequest.getMasterLogin()).isPresent()) {
            throw new UsernameIsTakenException("Username is already taken!");
        }

        log.warn("Registering new account for user: " + registerRequest.getMasterLogin());

        boolean isPasswordEncodedWithHMAC = registerRequest.getEncodingType().equals("HMAC");

        User newUser = User.builder()
                .masterLogin(registerRequest.getMasterLogin())
                .AESKey(UUID.randomUUID().toString().substring(0, 16))
                .isPasswordEncodedWithHMAC(isPasswordEncodedWithHMAC)
                .build();

        if (isPasswordEncodedWithHMAC) {
            String newHMACKey = UUID.randomUUID().toString();
            newUser.setMasterPasswordHash(hmacService.encode(registerRequest.getMasterPassword(), newHMACKey));
            newUser.setHMACKey(newHMACKey);
        } else {
            String newSHASalt = UUID.randomUUID().toString();
            newUser.setMasterPasswordHash(shaService.encode(registerRequest.getMasterPassword(), newSHASalt, "SHA-512"));
            newUser.setSHASalt(newSHASalt);
        }

        userRepository.save(newUser);

        log.warn("Account for user: " + registerRequest.getMasterLogin() + " has been registered");
        return ServiceResponseMessage.builder().message("Your account has been registered!").build();
    }

    @Override
    public ServiceResponseMessage changePassword(PasswordChangeRequest passwordChangeRequest) {

        User user = userRepository.findById(passwordChangeRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        log.warn("Changing password for user: " + user.getMasterLogin());

        if(user.getIsPasswordEncodedWithHMAC()) {
            if (sharedMethods.passwordsAreMatching(passwordChangeRequest.getOldMasterPassword(), user.getHMACKey(), user.getMasterPasswordHash(), "HMAC")) {
                String newHMACKey = UUID.randomUUID().toString();
                user.setMasterPasswordHash(hmacService.encode(passwordChangeRequest.getNewMasterPassword(), newHMACKey));
                user.setHMACKey(newHMACKey);
                userRepository.save(user);
            } else {
                throw new MasterPasswordsNotMatching("Master passwords do not match!");
            }
        } else {
            if (sharedMethods.passwordsAreMatching(passwordChangeRequest.getOldMasterPassword(), user.getSHASalt(), user.getMasterPasswordHash(), "SHA")) {
                String newSHASalt = UUID.randomUUID().toString();
                user.setMasterPasswordHash(shaService.encode(passwordChangeRequest.getNewMasterPassword(), newSHASalt, "SHA-512"));
                user.setSHASalt(newSHASalt);
                userRepository.save(user);
            } else {
                throw new MasterPasswordsNotMatching("Master passwords do not match!");
            }
        }

        return ServiceResponseMessage.builder().message("Your password has been changed successfully!").build();
    }
}
