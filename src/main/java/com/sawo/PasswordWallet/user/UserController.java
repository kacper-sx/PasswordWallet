package com.sawo.PasswordWallet.user;

import com.sawo.PasswordWallet.exception_handler.ServiceResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        return ResponseEntity.ok(this.userService.login(loginRequest, request.getRemoteAddr()));
    }

    @PostMapping(value = "/register")
    ResponseEntity<ServiceResponseMessage> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(this.userService.register(registerRequest));
    }

    @PatchMapping(value = "change/password")
    ResponseEntity<ServiceResponseMessage> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        return ResponseEntity.ok(this.userService.changePassword(passwordChangeRequest));
    }
}
