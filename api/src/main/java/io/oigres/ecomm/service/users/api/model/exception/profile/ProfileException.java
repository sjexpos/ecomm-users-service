package io.oigres.ecomm.service.users.api.model.exception.profile;

import io.oigres.ecomm.service.users.api.model.BusinessApiException;

public class ProfileException extends BusinessApiException {

    public ProfileException() {
    }

    public ProfileException(String message) {
        super(message);
    }
}
