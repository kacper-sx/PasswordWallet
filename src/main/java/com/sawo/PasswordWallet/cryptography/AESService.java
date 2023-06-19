package com.sawo.PasswordWallet.cryptography;

public interface AESService {

    String encode(String passwordToEncode, String key);

    String decode(String passwordToDecode, String key);
}
