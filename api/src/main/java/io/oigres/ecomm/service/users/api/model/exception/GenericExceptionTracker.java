package io.oigres.ecomm.service.users.api.model.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GenericExceptionTracker {
    private String exception;
    private String error;
    private String status;
    private String message;
    private String path;

    public GenericExceptionTracker(String exception, String message,
                                    String error, String status, String path) {
        this.exception = exception;
        this.error = error;
        this.message = message;
        this.status = status;
        this.path = path;
    }
}
