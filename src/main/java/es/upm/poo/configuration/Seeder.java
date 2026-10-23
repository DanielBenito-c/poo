package es.upm.poo.configuration;

import es.upm.poo.data.model.User;
import es.upm.poo.services.UserService;

import java.time.LocalDate;

public class Seeder {
    private final UserService userService;

    public Seeder(UserService userService) {
        this.userService = userService;
    }

    public void seed() {
        this.userService.create(new User("Alice Johnson", "alice.johnson@upm.es", LocalDate.of(2007, 5, 15), User.Gender.FEMALE,null));
        this.userService.create(new User("Marco Rossi", "marco.rossi@upm.es", LocalDate.of(2007, 1, 1), User.Gender.MALE, true));
        this.userService.create(new User("Nadia Chen", "nadia.chen@upm.es", LocalDate.of(2000, 1, 1), User.Gender.FEMALE, true));
        this.userService.create(new User("Omar Haddad", "omar.haddad@upm.es", LocalDate.of(1965, 8, 20), User.Gender.OTHER, true));
        this.userService.create(new User("Sofia Garcia", "sofia.garcia@upm.es", LocalDate.of(1992, 11, 10), User.Gender.FEMALE, true));
    }
}

