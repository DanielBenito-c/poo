package es.upm.poo;

import java.util.Objects;

public class User {

    private static final int MIN_AGE = 13;
    private static final Boolean DEFAULT_IS_ACTIVE = true;

    private String name;
    private String email;
    private Integer age;
    private Gender gender;
    private Boolean isActive;

    public User(String name, String email, Integer age) {
        this.setName(name);
        this.setEmail(email);
        this.setAge(age);
        this.setActive(DEFAULT_IS_ACTIVE);
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

    public void setAge(Integer age) {
        if (age == null || age < MIN_AGE) {
            throw new IllegalArgumentException("age inválido: " + age);
        }
        this.age = age;
    }

    public Integer getAge() {
        return this.age;
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

    public void setActive(Boolean isActive) {
        this.isActive = Objects.requireNonNullElse(isActive, DEFAULT_IS_ACTIVE);
    }

    public Boolean isActive() {
        return this.isActive;
    }

    public enum Gender {
        MALE,
        FEMALE,
        NON_BINARY,
        OTHER,
        PREFER_NOT_TO_SAY
    }
}
