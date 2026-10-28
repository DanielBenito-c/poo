package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Consent;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.ConsentService;
import es.upm.poo.services.UserService;

import java.util.List;

public class CreateConsent implements Command {
    private final View view;
    private final ConsentService consentService;

    public CreateConsent(View view, ConsentService consentService) {
        this.view = view;
        this.consentService = consentService;
    }

    @Override
    public String name() {
        return "create-consent";
    }

    @Override
    public List<String> obligatoryParams() {
        return List.of("id","source","purpose");
    }

    @Override
    public List<String> optionalParams() {
        return List.of("grantedAt");
    }

    @Override
    public String helpMessage() {
        return "Se crea el consentimiento";
    }

    @Override
    public void execute(CommandParams params) {
        Consent newConsent = new Consent(params.getLong(0),params.getString(1),params.getConsentPurpose(2),params.getLocalDateTime(3));
        Consent createdConsent = this.consentService.create(newConsent);
        this.view.showItem(createdConsent);
    }
}
