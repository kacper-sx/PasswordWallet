package com.sawo.PasswordWallet.login_info;

import com.sawo.PasswordWallet.exception_handler.ServiceResponseMessage;
import com.sawo.PasswordWallet.user.DecodePasswordRequest;

import java.util.List;

public interface LoginInfoService {

    List<LoginInfoReadDTO> getAll(Long userId);

    LoginInfoReadDTO getDecodedById(Long userId, Long loginInfoId, DecodePasswordRequest decodePasswordRequest);

    ServiceResponseMessage add(Long userId, LoginInfoAddDTO loginInfoAddDTO);

    ServiceResponseMessage delete(Long userId, Long loginInfoId);

    ServiceResponseMessage update(LoginInfoEditionRequest loginInfoEditionRequest, Long userId, Long loginInfoId);
}
