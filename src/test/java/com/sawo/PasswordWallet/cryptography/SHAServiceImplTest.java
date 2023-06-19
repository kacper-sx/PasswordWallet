package com.sawo.PasswordWallet.cryptography;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class SHAServiceImplTest {

    private SHAService shaService;

    @BeforeEach
    void setupSHAService() {
        this.shaService = new SHAServiceImpl();
    }

    @Test
    void givenInvalidAlgorithmName_whenCallingEncode_thenThrowsException() {

        Throwable throwable = assertThrows(Throwable.class, () -> this.shaService.encode("password", "salt", "fakeAlgorithm!"));

        assertAll(
                () -> assertThat(throwable).isInstanceOf(RuntimeException.class),
                () -> assertThat(throwable.getMessage()).containsSequence("MessageDigest not available")
        );
    }

    @Test
    void givenValidAlgorithmName_whenCallingEncode_thenNoExceptionIsThrown() {

        assertDoesNotThrow(() -> this.shaService.encode("password", "salt", "SHA-256"));
    }

    private static Stream<Arguments> passwordsWithDifferentSalts() {
        return Stream.of(Arguments.of("PasswordToEncode", List.of("saltOne", "secondSalt", "thirdSalt")));
    }

    @ParameterizedTest
    @MethodSource("passwordsWithDifferentSalts")
    void givenDifferentSalts_whenCallingEncode_thenReturnsDifferentResults(String password, List<String> salts) {

        List<String> encodedPasswords = salts.stream().map(salt -> this.shaService.encode(password, salt, "SHA-512")).collect(Collectors.toList());

        assertThat(encodedPasswords).doesNotHaveDuplicates();

    }
}
