package io.oigres.ecomm.service.users.api.model.exception;

import io.oigres.ecomm.service.users.api.model.BusinessApiException;

public class InvalidRequestException extends BusinessApiException {
    public InvalidRequestException() {}

    public InvalidRequestException(String message) {super(message);}
}
