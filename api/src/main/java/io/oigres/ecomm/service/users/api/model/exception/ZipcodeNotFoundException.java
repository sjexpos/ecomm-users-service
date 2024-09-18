package io.oigres.ecomm.service.users.api.model.exception;

public class ZipcodeNotFoundException extends NotFoundException {
    public ZipcodeNotFoundException() {
    }

    public ZipcodeNotFoundException(String message) {
        super(message);
    }
}
