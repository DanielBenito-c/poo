package es.upm.poo.services;

import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.UserRepository;
import es.upm.poo.services.exceptions.ConflictException;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        this.assertUniqueEmail(user.getEmail());
        return this.userRepository.create(user);
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public Optional<User> readUser(Long id) {
        return this.userRepository.read(id);
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    private void assertUniqueEmail(String email) {
        if (email != null && !this.userRepository.findByEmail(email).isEmpty()) {
            throw new ConflictException("email already exists: " + email);
        }
    }
}

