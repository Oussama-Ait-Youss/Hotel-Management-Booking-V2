package service;

import model.User;
import repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than 0."
            );
        }

        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email cannot be empty."
            );
        }

        return userRepository.findByEmail(
                email.trim()
        );
    }

    public List<User> findAll() {

        return userRepository.findAll();
    }

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

        if (user.getEmail() == null
                || user.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "Email cannot be empty."
            );
        }

        Optional<User> existingUser =
                userRepository.findByEmail(
                        user.getEmail().trim()
                );

        if (existingUser.isPresent()
                && !existingUser.get()
                .getId()
                .equals(user.getId())) {

            throw new IllegalArgumentException(
                    "Another user already uses this email."
            );
        }

        userRepository.update(user);
    }

    public void deleteById(Long id) {

        if (id == null || id <= 0) {

            throw new IllegalArgumentException(
                    "User ID must be greater than 0."
            );
        }

        userRepository.deleteById(id);
    }
}