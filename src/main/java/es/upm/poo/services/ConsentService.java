package es.upm.poo.services;

import es.upm.poo.data.model.Consent;
import es.upm.poo.data.repositories.ConsentRepository;

public class ConsentService {
    private final ConsentRepository consentRepository;

    public ConsentService(ConsentRepository consentRepository) {
        this.consentRepository = consentRepository;
    }

    public Consent create(Consent consent) {
        return this.consentRepository.create(consent);
    }
}
