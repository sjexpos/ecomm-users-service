package io.oigres.ecomm.service.users.api.model.exception.profile;

import io.oigres.ecomm.service.users.api.model.exception.NotFoundException;

public class ProfileTypeNotFoundException extends NotFoundException {
    public ProfileTypeNotFoundException() {
    }

    public ProfileTypeNotFoundException(String message) {
        super(message);
    }
}
