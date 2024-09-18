package io.oigres.ecomm.service.users.api.model.exception.profile;

import io.oigres.ecomm.service.users.api.model.exception.NotFoundException;

public class ProfileTypeNotFoundExceptionResponseApi extends NotFoundException {
    public ProfileTypeNotFoundExceptionResponseApi() {
    }

    public ProfileTypeNotFoundExceptionResponseApi(String message) {
        super(message);
    }
}
