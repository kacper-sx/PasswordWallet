package com.sawo.PasswordWallet.user;

import com.sawo.PasswordWallet.exception_handler.ServiceResponseMessage;

public interface UserService {

    LoginResponse login(LoginRequest loginRequest, String ipAddress);

    ServiceResponseMessage register(RegisterRequest registerRequest);

    ServiceResponseMessage changePassword(PasswordChangeRequest passwordChangeRequest);
}
