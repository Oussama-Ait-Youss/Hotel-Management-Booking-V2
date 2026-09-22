package service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class PasswordService {

    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 600_000;
    private static final int KEY_LENGTH = 256;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateSalt() {

        byte[] salt = new byte[SALT_LENGTH];

        secureRandom.nextBytes(salt);

        return Base64.getEncoder()
                .encodeToString(salt);
    }

    public String hashPassword(
            String password,
            String salt
    ) {

        byte[] saltBytes =
                Base64.getDecoder().decode(salt);

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                saltBytes,
                ITERATIONS,
                KEY_LENGTH
        );

        try {

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256"
                    );

            byte[] hash =
                    factory.generateSecret(spec)
                            .getEncoded();

            return Base64.getEncoder()
                    .encodeToString(hash);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

            throw new RuntimeException(
                    "Password hashing algorithm is not available.",
                    e
            );

        } finally {

            spec.clearPassword();
        }
    }

    public boolean verifyPassword(
            String password,
            String salt,
            String expectedHash
    ) {

        String actualHash =
                hashPassword(password, salt);

        return Arrays.equals(
                Base64.getDecoder().decode(actualHash),
                Base64.getDecoder().decode(expectedHash)
        );
    }
}