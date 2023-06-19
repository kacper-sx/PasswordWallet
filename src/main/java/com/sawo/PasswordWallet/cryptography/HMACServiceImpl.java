package com.sawo.PasswordWallet.cryptography;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Slf4j
public class HMACServiceImpl implements HMACService {

    private static final String MAC_ALGORITHM_NAME = "HmacSHA512";

    @Override
    public String encode(String masterPassword, String key) {

        String result = "";

        try {
            final byte[] keyByteArray = key.getBytes(StandardCharsets.UTF_8);

            Mac sha512Hmac = Mac.getInstance(MAC_ALGORITHM_NAME);
            SecretKeySpec keySpec = new SecretKeySpec(keyByteArray, MAC_ALGORITHM_NAME);
            sha512Hmac.init(keySpec);

            byte[] macResult = sha512Hmac.doFinal(masterPassword.getBytes(StandardCharsets.UTF_8));
            result = Base64.getEncoder().encodeToString(macResult);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return result;
    }
}
