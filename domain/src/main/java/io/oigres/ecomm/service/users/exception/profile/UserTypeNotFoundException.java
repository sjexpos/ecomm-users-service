package io.oigres.ecomm.service.users.exception.profile;

import io.oigres.ecomm.service.users.exception.NotFoundException;

public class UserTypeNotFoundException extends NotFoundException {
    public UserTypeNotFoundException(String message) {
        super(message);
    }
}
