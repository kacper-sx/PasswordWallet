package com.sawo.PasswordWallet.login_info;

import com.sawo.PasswordWallet.exception_handler.ServiceResponseMessage;
import com.sawo.PasswordWallet.user.DecodePasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginInfoController {

    private final LoginInfoService loginInfoService;

    @GetMapping(value = "/users/{userId}/logininfos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LoginInfoReadDTO>> getAllLoginInformations(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(loginInfoService.getAll(userId));
    }

    @PostMapping(value = "users/{userId}/logininfos/{loginInfoId}/decode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginInfoReadDTO> getDecodedLoginInfoById(@PathVariable(name = "userId") Long userId,
            @PathVariable(name = "loginInfoId") Long loginInfoId, @RequestBody DecodePasswordRequest decodePasswordRequest) {
        return ResponseEntity.ok(loginInfoService.getDecodedById(userId, loginInfoId, decodePasswordRequest));
    }

    @PostMapping(value = "/users/{userId}/logininfos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponseMessage> addLoginInfo(@PathVariable(name = "userId") Long userId, @RequestBody LoginInfoAddDTO loginInfoAddDTO) {
        return ResponseEntity.ok(loginInfoService.add(userId, loginInfoAddDTO));
    }

    @DeleteMapping(value = "/users/{userId}/logininfos/{loginInfoId}")
    public ResponseEntity<ServiceResponseMessage> deleteLoginInfo(@PathVariable(name = "userId") Long userId,
                                                  @PathVariable(name = "loginInfoId") Long loginInfoId) {
        return ResponseEntity.ok(loginInfoService.delete(userId, loginInfoId));
    }

    @PostMapping(value = "/users/{userId}/logininfos/{loginInfoId}/edit")
    ResponseEntity<ServiceResponseMessage> update(@RequestBody LoginInfoEditionRequest loginInfoEditionRequest, @PathVariable(name = "userId") Long userId,
                                                  @PathVariable(name = "loginInfoId") Long loginInfoId) {
        return ResponseEntity.ok(this.loginInfoService.update(loginInfoEditionRequest, userId, loginInfoId));
    }
}
