package io.oigres.ecomm.service.users.api;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RemoteMethodArgumentNotValidException extends RuntimeException {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        private String field;
        private String rejectedValue;
        private String defaultMessage;
        private String code;
    }

    private List<Error> errors = new ArrayList<Error>();

    public RemoteMethodArgumentNotValidException() {
        super();
    }
    
    public void addError(Error e) {
        this.errors.add(e);
    }

    public List<Error> getErrors() {
        return this.errors;
    }

}
