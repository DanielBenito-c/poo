package es.upm.poo.data.model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

public class User {

    private static final int MIN_BIRTH_YEAR = 1900;
    private static final int MAX_NAME_LENGTH = 50;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name es obligatorio: " + name);
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    "name no puede superar los " + MAX_NAME_LENGTH + " caracteres: " + name);
        }
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("email es obligatorio" + null);
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("email no tiene un formato válido: " + email);
        }
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("dateOfBirth es obligatorio");
        }
        if (dateOfBirth.getYear() < MIN_BIRTH_YEAR) {
            throw new IllegalArgumentException(
                    "Indica tu año de nacimiento con un número a partir de " + MIN_BIRTH_YEAR + ": " + dateOfBirth);
        }
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(Gender gender) {
        if (gender == null) {
            throw new IllegalArgumentException("gender es obligatorio");
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
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof User user)) {
            return false;
        }
        return this.id != null && this.id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }
    @Override
    public String toString() {
        return "User{id=" + this.id
                + ", name=" + this.name
                + ", email=" + this.email
                + ", date of birth=" + this.dateOfBirth
                + ", gender=" + this.gender
                + ", active=" + this.active + '}';
    }
}
