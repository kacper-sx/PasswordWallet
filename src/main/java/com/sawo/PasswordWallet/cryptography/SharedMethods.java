package com.sawo.PasswordWallet.cryptography;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SharedMethods {

    private final HMACService hmacService;

    private final SHAService shaService;

    public boolean passwordsAreMatching(String masterPassword, String key, String savedPassword, String algorithmName) {

        if(algorithmName.equals("HMAC")) {
            return hmacService.encode(masterPassword, key).equals(savedPassword);
        } else {
            return shaService.encode(masterPassword, key, "SHA-512").equals(savedPassword);
        }
    }

}
