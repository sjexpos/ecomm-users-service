package io.oigres.ecomm.service.users.api.model;

public class BusinessApiException extends Exception {

    public BusinessApiException() {
    }

    public BusinessApiException(String message) {
        super(message);
    }

    public BusinessApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessApiException(Throwable cause) {
        super(cause);
    }

    public BusinessApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
