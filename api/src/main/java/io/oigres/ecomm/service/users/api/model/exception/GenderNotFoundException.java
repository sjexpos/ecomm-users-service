package io.oigres.ecomm.service.users.api.model.exception;

public class GenderNotFoundException extends NotFoundException {
    public GenderNotFoundException() {
    }

    public GenderNotFoundException(String message) {
        super(message);
    }
}
