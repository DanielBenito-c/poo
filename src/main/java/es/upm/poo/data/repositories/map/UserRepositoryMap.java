package es.upm.poo.data.repositories.map;

import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.UserRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class UserRepositoryMap extends GenericRepositoryMap<User> implements UserRepository {

    @Override
    public void setId(User entity, long id) {
        entity.setId(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return this.findAll().stream()
                .filter(user -> email.equalsIgnoreCase(user.getEmail()))
                .findFirst();
    }

    @Override
    public List<User> findByNameContaining(String name) {
        if (name == null || name.isBlank()) {
            return List.of();
        }
        String normalizedName = name.toLowerCase(Locale.ROOT);
        return this.findAll().stream()
                .filter(user -> user.getName().toLowerCase(Locale.ROOT).contains(normalizedName))
                .toList();
    }

    @Override
    public List<String> findEmailsByNameContaining(String name) {
        return this.findByNameContaining(name).stream()
                .map(User::getEmail)
                .toList();
    }
}

