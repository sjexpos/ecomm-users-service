package io.oigres.ecomm.service.users.api.model.exception.profile;

public class ProfileExistingException extends ProfileException{
    public ProfileExistingException() {
    }

    public ProfileExistingException(String message) {
        super(message);
    }
}
