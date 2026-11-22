package es.upm.poo.services;

import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.UserRepository;
import es.upm.poo.services.exceptions.ConflictException;
import es.upm.poo.services.exceptions.NotFoundException;

import java.util.List;

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

    public User readUser(Long id) {
        return this.userRepository.read(id)
                .orElseThrow(() -> new NotFoundException("user not found with id: " + id));
    }

    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user not found with email: " + email));
    }

    public List<User> findUsersByName(String name) {
        return this.userRepository.findByNameContaining(name);
    }

    public List<String> findUserEmailsByName(String name) {
        return this.userRepository.findEmailsByNameContaining(name);
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    private void assertUniqueEmail(String email) {
        if (email != null && this.userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("email already exists: " + email);
        }
    }
}

