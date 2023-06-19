package com.sawo.PasswordWallet.cryptography;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AESServiceImplTest {

    private AESService aesService;

    @BeforeEach
    void setupAESService() {
        this.aesService = new AESServiceImpl();
    }

    private static Stream<Arguments> differentPasswordKeysTestData() {
        return Stream.of(Arguments.of("zaq1@WSX", List.of("SomeVeryNiceKey1", "SomeVeryNiceKey2", "SomeVeryNiceKey3")));
    }

    @ParameterizedTest
    @MethodSource("differentPasswordKeysTestData")
    void givenDifferentPasswordKeys_whenCallingEncode_thenReturnsDifferentResults(String password, List<String> keys) {

        List<String> encodedPasswords = new ArrayList<>();

        keys.forEach(key -> {
            encodedPasswords.add(this.aesService.encode(password, key));
        });

        assertThat(encodedPasswords).doesNotHaveDuplicates();
    }

    @Test
    void givenInvalidKeyLength_whenCallingEncode_thenThrowsException() {

        Throwable throwable = assertThrows(Throwable.class, () -> this.aesService.encode("password", "tooShortKey"));

        assertAll(
                () -> assertThat(throwable).isInstanceOf(RuntimeException.class),
                () -> assertThat(throwable.getMessage()).containsSequence("Invalid AES key length:")
        );
    }

    private static Stream<Arguments> encodingAndThenDecodingPasswordTestData() {
        return Stream.of(
                Arguments.of("ThisPasswordWillBeTheSame!", "1111111111111111", "ThisPasswordWillBeTheSame!"),
                Arguments.of("Another the same password", "2222222222222222", "Another the same password"),
                Arguments.of("AndTheLastPassword", "3333333333333333", "AndTheLastPassword"));
    }

    @ParameterizedTest
    @MethodSource("encodingAndThenDecodingPasswordTestData")
    void givenValidArguments_whenEncodingAndThenDecodingPassword_thenReturnsTheSamePasswords(String password, String key, String expectedDecodedPassword) {

        String encodedPassword = this.aesService.encode(password, key);

        String actualDecodedPassword = this.aesService.decode(encodedPassword, key);

        assertThat(actualDecodedPassword).isEqualTo(expectedDecodedPassword);
    }

}
