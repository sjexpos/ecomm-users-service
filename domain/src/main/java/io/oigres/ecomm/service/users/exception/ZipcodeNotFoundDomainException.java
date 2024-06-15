package io.oigres.ecomm.service.users.exception;

public class ZipcodeNotFoundDomainException extends NotFoundException{
    public ZipcodeNotFoundDomainException() {
        super("Zipcode not found");
    }

    public ZipcodeNotFoundDomainException(String message) {
        super(message);
    }
}
