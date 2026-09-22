package service;

import model.User;
import model.enums.UserRole;
import repository.UserRepository;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 600_000;
    private static final int KEY_LENGTH = 256;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ==============================
    // CREATE
    // ==============================

    public User createUser(
            String firstName,
            String lastName,
            String email,
            String password,
            UserRole role
    ) {

        // 1. Validate user data
        validateUserData(firstName, lastName, email, password, role);

        // 2. Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException(
                    "A user with this email already exists."
            );
        }

        // 3. Generate salt
        byte[] saltBytes = generateSalt();

        // Convert salt to String for database storage
        String salt = Base64.getEncoder().encodeToString(saltBytes);

        // 4. Hash password
        String passwordHash = hashPassword(password, saltBytes);

        // 5. Create User object
        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setSalt(salt);
        user.setRole(role);

        // 6. Save user
        return userRepository.save(user);
    }

    // ==============================
    // FIND BY ID
    // ==============================

    public Optional<User> findById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than 0."
            );
        }

        return userRepository.findById(id);
    }

    // ==============================
    // FIND BY EMAIL
    // ==============================

    public Optional<User> findByEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email cannot be empty."
            );
        }

        return userRepository.findByEmail(email.trim());
    }

    // ==============================
    // FIND ALL
    // ==============================

    public List<User> findAll() {
        return userRepository.findAll();
    }

    // ==============================
    // UPDATE
    // ==============================

    public void update(User user) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User cannot be null."
            );
        }

        if (user.getId() == null || user.getId() <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than 0."
            );
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "Email cannot be empty."
            );
        }

        // Check whether another user already uses this email
        Optional<User> existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()
                && !existingUser.get().getId().equals(user.getId())) {

            throw new IllegalArgumentException(
                    "Another user already uses this email."
            );
        }

        userRepository.update(user);
    }

    // ==============================
    // DELETE
    // ==============================

    public void deleteById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than 0."
            );
        }

        userRepository.deleteById(id);
    }

    // ==============================
    // VALIDATION
    // ==============================

    private void validateUserData(
            String firstName,
            String lastName,
            String email,
            String password,
            UserRole role
    ) {

        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException(
                    "First name cannot be empty."
            );
        }

        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException(
                    "Last name cannot be empty."
            );
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email cannot be empty."
            );
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Password cannot be empty."
            );
        }

        if (role == null) {
            throw new IllegalArgumentException(
                    "Role cannot be null."
            );
        }
    }

    // ==============================
    // GENERATE SALT
    // ==============================

    private byte[] generateSalt() {

        byte[] salt = new byte[SALT_LENGTH];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        return salt;
    }

    // ==============================
    // HASH PASSWORD
    // ==============================

    private String hashPassword(
            String password,
            byte[] salt
    ) {

        try {

            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256"
                    );

            byte[] hash = factory
                    .generateSecret(spec)
                    .getEncoded();

            spec.clearPassword();

            return Base64.getEncoder()
                    .encodeToString(hash);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

            throw new RuntimeException(
                    "Password hashing algorithm is not available.",
                    e
            );
        }
    }
}