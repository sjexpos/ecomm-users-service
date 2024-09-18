package io.oigres.ecomm.service.users.api.model.exception.profile;

import io.oigres.ecomm.service.users.api.model.exception.NotFoundException;

public class ProfileNotFoundException extends NotFoundException {
    public ProfileNotFoundException() {
    }

    public ProfileNotFoundException(String message) {
        super(message);
    }
}
