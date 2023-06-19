package com.sawo.PasswordWallet.user_login_info;

import java.time.LocalDateTime;

public interface UserLoginInfoService {

    void searchForConsecutiveFailedLogins(String ipAddress, LocalDateTime loginTime);

    void saveUserServiceLoginAttempt(String ipAddress, String masterLogin, LocalDateTime loginTime, boolean successful, boolean countDuringLoginAttempt);

    UserLoginInfo findLastFailedOrSuccessfulUserLogin(String masterLogin, boolean successful);
}
