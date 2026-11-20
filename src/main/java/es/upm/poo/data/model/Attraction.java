package es.upm.poo.data.model;

public class Attraction {

    private Long id;
    private String name;
    private Integer minHeightCm;
    private Integer capacity;

    public Attraction(String name, Integer minHeightCm, Integer capacity) {
        this.setName(name);
        this.setMinHeightCm(minHeightCm);
        this.setCapacity(capacity);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name es obligatorio");
        }
        this.name = name;
    }

    public Integer getMinHeightCm() {
        return this.minHeightCm;
    }

    public void setMinHeightCm(Integer minHeightCm) {
        if (minHeightCm != null && minHeightCm < 0) {
            throw new IllegalArgumentException("minHeightCm no puede ser negativa");
        }
        this.minHeightCm = minHeightCm;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public void setCapacity(Integer capacity) {
        if (capacity == null) {
            throw new IllegalArgumentException("capacity es obligatorio");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity no puede ser negativo");
        }
        this.capacity = capacity;
    }
}
