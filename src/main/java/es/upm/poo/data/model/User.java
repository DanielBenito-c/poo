package es.upm.poo.data.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class User {

    private static final int MIN_AGE = 13;
    private static final Boolean DEFAULT_ACTIVE = true;

    private Long id;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Boolean active;

    public User(String name, String email, LocalDate dateOfBirth, Gender gender, Boolean active) {
        this.setName(name);
        this.setEmail(email);
        this.setDateOfBirth(dateOfBirth);
        this.setGender(gender);
        this.setActive(active);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name inválido: " + name);
        }
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("email inválido: " + email);
        }
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("dateOfBirth inválido: " + dateOfBirth);
        }
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        if (age < MIN_AGE) {
            throw new IllegalArgumentException("dateOfBirth inválido: " + dateOfBirth);
        }
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setGender(Gender gender) {
        if (gender == null) {
            throw new IllegalArgumentException("gender inválido: " + gender);
        }
        this.gender = gender;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setActive(Boolean active) {
        this.active = Objects.requireNonNullElse(active, DEFAULT_ACTIVE);
    }

    public Boolean isActive() {
        return this.active;
    }

    public enum Gender {
        MALE,
        FEMALE,
        NON_BINARY,
        OTHER,
        PREFER_NOT_TO_SAY
    }

    @Override
    public String toString() {
        return "User{id=" + this.id + ", name=" + this.name + ", email=" + this.email + ", date of birth=" + this.dateOfBirth + ", active=" + this.active + '}';
    }
}
