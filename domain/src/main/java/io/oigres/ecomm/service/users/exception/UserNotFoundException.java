package io.oigres.ecomm.service.users.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() { super("User not found"); }
}
