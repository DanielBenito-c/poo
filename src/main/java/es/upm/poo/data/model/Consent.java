package es.upm.poo.data.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Consent {


    private Long id;
    private LocalDateTime grantedAt;
    private String source;
    private ConsentPurpose purpose;
    private LocalDateTime revokedAt;

    public Consent(String source, ConsentPurpose purpose, LocalDateTime grantedAt) {
        this.setSource(source);
        this.setPurpose(purpose);
        this.setGrantedAt(grantedAt);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getGrantedAt() {
        return this.grantedAt;
    }

    public void setGrantedAt(LocalDateTime grantedAt) {
        this.grantedAt = grantedAt;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        if (source == null) {
            throw new IllegalArgumentException("source es obligatorio " + null);
        }
        this.source = source;
    }

    public ConsentPurpose getPurpose() {
        return this.purpose;
    }

    public void setPurpose(ConsentPurpose purpose) {
        if (purpose == null) {
            throw new IllegalArgumentException("purpose es obligatorio " + null);
        }
        this.purpose = purpose;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = Objects.requireNonNullElse(revokedAt,LocalDateTime.now());
    }

    public enum ConsentPurpose {
        BASIC,
        MARKETING,
        ALL
    }
}
