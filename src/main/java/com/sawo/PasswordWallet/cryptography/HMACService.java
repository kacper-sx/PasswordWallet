package com.sawo.PasswordWallet.cryptography;

public interface HMACService {

    String encode(String masterPassword, String key);
}
