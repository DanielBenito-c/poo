package es.upm.poo.data.model;

import java.time.LocalDateTime;
public class Consent {


    private Long id;
    private Long userId;
    private LocalDateTime grantedAt;
    private String source;
    private ConsentPurpose purpose;
    private LocalDateTime revokedAt;

    public Consent(Long userId, String source, ConsentPurpose purpose, LocalDateTime grantedAt) {
        this.setUserId(userId);
        this.setSource(source);
        this.setPurpose(purpose);
        this.setGrantedAt(grantedAt);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId es obligatorio: " + userId);
        }
        this.userId = userId;
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
        return this.revokedAt;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }

    public enum ConsentPurpose {
        BASIC,
        MARKETING,
        ALL
    }

    @Override
    public String toString() {
        return "Consent{id=" + this.id
                + ", userId=" + this.userId
                + ", source=" + this.source
                + ", purpose=" + this.purpose + "}";
    }
}
