package es.upm.poo.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Zone {

    private Long id;
    private final List<Attraction> attractions = new ArrayList<>();
    private String name;
    private String theme;

    public Zone(String name, String theme) {
        this.setName(name);
        this.setTheme(theme);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Attraction> getAttractions() {
        return Collections.unmodifiableList(this.attractions);
    }

    public void addAttraction(Attraction attraction) {
        if (attraction == null) {
            throw new IllegalArgumentException("attraction es obligatoria");
        }
        if (this.attractions.contains(attraction)) {
            throw new IllegalArgumentException("la attraction ya está en la zona");
        }
        this.attractions.add(attraction);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name es obligatorio");
        }
        this.name = name;
    }

    public String getTheme() {
        return this.theme;
    }

    public void setTheme(String theme) {
        if (theme == null || theme.isBlank()) {
            throw new IllegalArgumentException("theme es obligatorio");
        }
        this.theme = theme;
    }

    @Override
    public String toString() {
        return "Zone{id=" + this.id
                + ", name=" + this.name
                + ", theme=" + this.theme
                + ", attractions=" + this.attractions.stream().map(Attraction::getName).toList() + '}';
    }
}
