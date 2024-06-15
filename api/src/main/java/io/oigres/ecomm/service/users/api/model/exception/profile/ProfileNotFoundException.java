package io.oigres.ecomm.service.users.api.model.exception.profile;

public class ProfileNotFoundException extends ProfileException {
    public ProfileNotFoundException() {
    }

    public ProfileNotFoundException(String message) {
        super(message);
    }
}
