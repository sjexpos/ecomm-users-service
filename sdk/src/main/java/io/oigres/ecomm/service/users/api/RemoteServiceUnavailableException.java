package io.oigres.ecomm.service.users.api;

public class RemoteServiceUnavailableException extends RuntimeException {

    public RemoteServiceUnavailableException(String message) {
        super(message);
    }

}
