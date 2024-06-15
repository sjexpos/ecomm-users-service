package io.oigres.ecomm.service.users.api.model.exception;

public class UserTypeNotFoundException extends Exception{
    public UserTypeNotFoundException() {
        super("User Type not found");
    }

    public UserTypeNotFoundException(String message) {
        super(message);
    }
}
