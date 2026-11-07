package es.upm.poo.services;

import es.upm.poo.data.model.Consent;
import es.upm.poo.data.model.Consent.ConsentPurpose;
import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.ConsentRepository;
import es.upm.poo.data.repositories.UserRepository;

import java.time.LocalDateTime;

public class ConsentService {

    private final UserRepository userRepository;
    private final ConsentRepository consentRepository;

    public ConsentService(UserRepository userRepository, ConsentRepository consentRepository) {
        this.userRepository = userRepository;
        this.consentRepository = consentRepository;
    }

    public Consent create(Consent consent) {
        return this.consentRepository.create(consent);
    }
}
