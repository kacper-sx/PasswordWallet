package com.sawo.PasswordWallet.cryptography;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Service
@Slf4j
public class AESServiceImpl implements AESService {

    private static final String AES_ALGORITHM_NAME = "AES";

    @Override
    public String encode(String passwordToEncode, String passwordKey) {

        String encodedPassword = "";

        try {
            Key key  = this.generateKey(passwordKey);
            Cipher AESCipher = Cipher.getInstance(AES_ALGORITHM_NAME);
            AESCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = AESCipher.doFinal(passwordToEncode.getBytes());
            encodedPassword = Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return encodedPassword;
    }

    @Override
    public String decode(String passwordToDecode, String passwordKey) {

        String decodedPassword = "";

        try {
            Key key  = this.generateKey(passwordKey);
            Cipher AESCipher = Cipher.getInstance(AES_ALGORITHM_NAME);
            AESCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedValue = Base64.getDecoder().decode(passwordToDecode);
            decodedPassword = new String(AESCipher.doFinal(decodedValue));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return decodedPassword;
    }

    private Key generateKey( String passwordKey) {
        return new SecretKeySpec(passwordKey.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM_NAME);
    }
}
