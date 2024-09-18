package io.oigres.ecomm.service.users.api.model.exception;

import io.oigres.ecomm.service.users.api.model.BusinessApiException;

public class NotFoundException extends BusinessApiException {
    public NotFoundException() {}

    public NotFoundException(String message) {super(message);}
}
