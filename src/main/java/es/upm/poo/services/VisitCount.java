package es.upm.poo.services;

public record VisitCount(String email, int year, long total) {

    @Override
    public String toString() {
        return "[" + this.email + ", " + this.year + ", " + this.total + "]";
    }
}
