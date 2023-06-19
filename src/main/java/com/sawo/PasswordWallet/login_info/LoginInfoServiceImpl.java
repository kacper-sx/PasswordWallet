package com.sawo.PasswordWallet.login_info;

import com.sawo.PasswordWallet.cryptography.AESService;
import com.sawo.PasswordWallet.cryptography.HMACService;
import com.sawo.PasswordWallet.cryptography.SHAService;
import com.sawo.PasswordWallet.cryptography.SharedMethods;
import com.sawo.PasswordWallet.exception_handler.*;
import com.sawo.PasswordWallet.user.DecodePasswordRequest;
import com.sawo.PasswordWallet.user.User;
import com.sawo.PasswordWallet.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginInfoServiceImpl implements LoginInfoService {

    private final UserRepository userRepository;

    private final LoginInfoRepository loginInfoRepository;

    private final AESService aesService;

    private final SharedMethods sharedMethods;

    @Override
    public List<LoginInfoReadDTO> getAll(Long userId) {
        Optional<User> foundUserOptional = userRepository.findById(userId);

        if (foundUserOptional.isPresent()) {
            User user = foundUserOptional.get();

            List<LoginInfo> loginInfos = loginInfoRepository.findAllByUser(user);

            return loginInfos.stream().map(loginInfo ->
                    LoginInfoReadDTO.builder()
                        .id(loginInfo.getId())
                        .password(loginInfo.getPassword())
                        .login(loginInfo.getLogin())
                        .webAddress(loginInfo.getWebAddress())
                        .description(loginInfo.getDescription())
                        .build()
            ).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public LoginInfoReadDTO getDecodedById(Long userId, Long loginInfoId, DecodePasswordRequest decodePasswordRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));


        if(user.getIsPasswordEncodedWithHMAC()) {
            if (sharedMethods.passwordsAreMatching(decodePasswordRequest.getMasterPassword(), user.getHMACKey(), user.getMasterPasswordHash(), "HMAC")) {
               return this.getLoginInfoByIdAndMapToDTO(loginInfoId, user.getAESKey());
            } else {
                throw new MasterPasswordsNotMatching("Master passwords do not match!");
            }
        } else {
            if (sharedMethods.passwordsAreMatching(decodePasswordRequest.getMasterPassword(), user.getSHASalt(), user.getMasterPasswordHash(), "SHA")) {
                return this.getLoginInfoByIdAndMapToDTO(loginInfoId, user.getAESKey());
            } else {
                throw new MasterPasswordsNotMatching("Master passwords do not match!");
            }
        }
    }

    @Override
    public ServiceResponseMessage add(Long userId, LoginInfoAddDTO loginInfoAddDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        log.warn("Adding new login info for user: " + user.getMasterLogin());

        if(user.getIsPasswordEncodedWithHMAC()) {
            if (sharedMethods.passwordsAreMatching(loginInfoAddDTO.getMasterPassword(), user.getHMACKey(), user.getMasterPasswordHash(), "HMAC")) {
                this.saveNewLoginInfo(user, loginInfoAddDTO);
            } else {
                throw new MasterPasswordsNotMatching("Master passwords do not match!");
            }
        } else {
            if (sharedMethods.passwordsAreMatching(loginInfoAddDTO.getMasterPassword(), user.getSHASalt(), user.getMasterPasswordHash(), "SHA")) {
                this.saveNewLoginInfo(user, loginInfoAddDTO);
            } else {
                throw new MasterPasswordsNotMatching("Master passwords do not match!");
            }
        }

        return ServiceResponseMessage.builder().message("Your new login info for web page: " + loginInfoAddDTO.getWebAddress() + " has been added!").build();
    }

    @Override
    public ServiceResponseMessage delete(Long userId, Long loginInfoId) {
        loginInfoRepository.deleteById(loginInfoId);
        return ServiceResponseMessage.builder().message("Login info deleted").build();
    }

    private void saveNewLoginInfo(User user, LoginInfoAddDTO newLoginInfo) {
        loginInfoRepository.save(LoginInfo.builder()
                .user(user)
                .password(aesService.encode(newLoginInfo.getPassword(), user.getAESKey()))
                .login(newLoginInfo.getLogin())
                .webAddress(newLoginInfo.getWebAddress())
                .description(newLoginInfo.getDescription())
                .build());
    }

    private LoginInfoReadDTO getLoginInfoByIdAndMapToDTO(Long loginInfoId, String AESKey) {

        LoginInfo foundLoginInfo = loginInfoRepository.findById(loginInfoId).orElseThrow(() -> new LoginInfoNotFoundException("Login info not found!"));
        return LoginInfoReadDTO.builder()
                .id(foundLoginInfo.getId())
                .login(foundLoginInfo.getLogin())
                .password(aesService.decode(foundLoginInfo.getPassword(), AESKey))
                .webAddress(foundLoginInfo.getWebAddress())
                .description(foundLoginInfo.getDescription())
                .build();
    }

    @Override
    public ServiceResponseMessage update(LoginInfoEditionRequest loginInfoEditionRequest, Long userId, Long loginInfoId) {
        System.out.println(loginInfoEditionRequest + " " + loginInfoId + " " +userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        LoginInfo foundLoginInfo = loginInfoRepository.findById(loginInfoId).orElseThrow(() -> new LoginInfoNotFoundException("Login info not found!"));

        String key = user.getAESKey();
        foundLoginInfo.setPassword(aesService.encode(loginInfoEditionRequest.getNewPassword(), key));

        loginInfoRepository.save(foundLoginInfo);

        return ServiceResponseMessage.builder().message("password Successfully changed").build();
    }
}
