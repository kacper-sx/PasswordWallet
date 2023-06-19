package com.sawo.PasswordWallet.cryptography;

public interface SHAService {

    String encode(String masterPassword, String salt, String algorithm);
}
