package service;

import model.User;
import model.enums.UserRole;
import repository.UserRepository;

import java.util.Optional;

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public AuthService(
            UserRepository userRepository,
            PasswordService passwordService
    ) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    // ==========================================
    // REGISTER
    // ==========================================

    public User register(
            String firstName,
            String lastName,
            String email,
            String password,
            UserRole role
    ) {

        validateRegistrationData(
                firstName,
                lastName,
                email,
                password,
                role
        );

        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {

            throw new IllegalArgumentException(
                    "An account with this email already exists."
            );
        }

        // Generate a unique salt
        String salt =
                passwordService.generateSalt();

        // Hash password using the generated salt
        String passwordHash =
                passwordService.hashPassword(
                        password,
                        salt
                );

        // Create User
        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setSalt(salt);
        user.setRole(role);

        // Save user
        return userRepository.save(user);
    }

    // ==========================================
    // LOGIN
    // ==========================================

    public User login(
            String email,
            String password
    ) {

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

        Optional<User> result =
                userRepository.findByEmail(email.trim());

        if (result.isEmpty()) {

            throw new IllegalArgumentException(
                    "Invalid email or password."
            );
        }

        User user = result.get();

        boolean passwordCorrect =
                passwordService.verifyPassword(
                        password,
                        user.getSalt(),
                        user.getPasswordHash()
                );

        if (!passwordCorrect) {

            throw new IllegalArgumentException(
                    "Invalid email or password."
            );
        }

        return user;
    }

    // ==========================================
    // VALIDATION
    // ==========================================

    private void validateRegistrationData(
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
}