package es.upm.poo.data.model;

import java.time.LocalDate;

public class Consent {
    private Long id;
    private LocalDate consentDate;
    private String policyVersion;
    private Boolean DataProcessingAccepted;
    private Boolean AdvertisingAccepted;
}
