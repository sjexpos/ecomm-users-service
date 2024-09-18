package io.oigres.ecomm.service.users.api.model.exception;

import io.oigres.ecomm.service.users.api.model.BusinessApiException;

public class UnauthorizedException extends BusinessApiException {
    public UnauthorizedException() {}

    public UnauthorizedException(String message) {super(message);}
}
