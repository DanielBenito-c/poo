package es.upm.poo.data.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Park {

    private Long id;
    private List<Zone> zones = new ArrayList<>();
    private String name;
    private String address;
    private LocalTime openingTime;
    private LocalTime closingTime;

    public Park (String name, String address, LocalTime openingTime, LocalTime closingTime, String zoneName, String zoneTheme) {
        this.setName(name);
        this.setAddress(address);
        this.setOpeningTime(openingTime);
        this.setClosingTime(closingTime);
        this.addZone(zoneName, zoneTheme);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Zone> getZones() {
        return Collections.unmodifiableList(this.zones);
    }

    public void addZone(String name, String theme) {
        Zone zone = new Zone(name, theme);
        this.zones.add(zone);
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

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("address es obligatorio");
        }
        this.address = address;
    }

    public LocalTime getOpeningTime() {
        return this.openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        if (openingTime == null) {
            throw new IllegalArgumentException("openingTime es obligatorio");
        }
        this.openingTime = openingTime;
    }
    public LocalTime getClosingTime() {
        return this.closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        if (closingTime == null) {
            throw new IllegalArgumentException("closingTime es obligatorio");
        }
        this.closingTime = closingTime;
    }

    @Override
    public String toString() {
        return "Park{id=" + this.id
                + ", name=" + this.name
                + ", address=" + this.address
                + ", openingTime=" + this.openingTime
                + ", closingTime=" + this.closingTime
                + ", zones=" + this.zones.stream().map(Zone::getName).toList() + '}';
    }
}
