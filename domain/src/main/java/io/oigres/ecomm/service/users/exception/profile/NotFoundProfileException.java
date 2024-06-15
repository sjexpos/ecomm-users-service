package io.oigres.ecomm.service.users.exception.profile;

public class NotFoundProfileException extends ProfileUserException{
    public NotFoundProfileException() {
        super("Profile not found");
    }
}
