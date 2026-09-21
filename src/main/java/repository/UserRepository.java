package repository;
import java.util.List;
import java.util.Optional;

import model.User;





public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    public User save(User user);
    public void update (User use);
    public void deleteById(Long id);
}