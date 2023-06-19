package com.sawo.PasswordWallet.cryptography;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class SHAServiceImpl implements SHAService {

    @Override
    public String encode(String masterPassword, String salt, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] hashValueByteArray = messageDigest.digest((salt + masterPassword).getBytes());

            BigInteger bigInteger = new BigInteger(1, hashValueByteArray);
            StringBuilder hashedText = new StringBuilder(bigInteger.toString(16));

            while (hashedText.length() < 32) {
                hashedText.insert(0, "0");
            }

            return hashedText.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
