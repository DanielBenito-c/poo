package es.upm.poo.data.model;

import java.time.LocalDateTime;
public class Consent {


    private Long id;
    private final LocalDateTime grantedAt;
    private String source;
    private ConsentPurpose purpose;
    private LocalDateTime revokedAt;
    private  User user;

    public Consent(User user, String source, ConsentPurpose purpose) {
        this.setUser(user);
        this.setSource(source);
        this.setPurpose(purpose);
        this.grantedAt = LocalDateTime.now();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user es obligatorio: " + null);
        }
        this.user=user;
    }

    public LocalDateTime getGrantedAt() {
        return this.grantedAt;
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

    public void setRevokedAt() {
        this.revokedAt = LocalDateTime.now();
    }

    public enum ConsentPurpose {
        BASIC,
        MARKETING,
        ALL
    }

    @Override
    public String toString() {
        return "Consent{id=" + this.id
                + ", userId=" + this.user.getId()
                + ", source=" + this.source
                + ", purpose=" + this.purpose
                + ", grantedAt=" + this.grantedAt
                + ", revokedAt=" + this.revokedAt + "}";
    }
}
