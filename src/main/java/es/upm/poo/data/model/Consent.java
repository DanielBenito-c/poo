package es.upm.poo.data.model;

import java.time.LocalDateTime;

public class Consent {


    private Long id;
    private LocalDateTime grantedAt;
    private String source;
    private ConsentPurpose purpose;
    private LocalDateTime revokedAt;








    public enum ConsentPurpose {
        BASIC,
        MARKETING,
        ALL
    }
}
