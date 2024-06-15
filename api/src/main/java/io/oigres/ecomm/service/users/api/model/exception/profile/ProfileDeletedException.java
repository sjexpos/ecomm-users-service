package io.oigres.ecomm.service.users.api.model.exception.profile;

public class ProfileDeletedException extends ProfileException{
    public ProfileDeletedException() {
    }

    public ProfileDeletedException(String message) {
        super(message);
    }
}
