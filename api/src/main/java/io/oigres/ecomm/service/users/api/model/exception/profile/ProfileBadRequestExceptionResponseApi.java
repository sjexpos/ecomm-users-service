package io.oigres.ecomm.service.users.api.model.exception.profile;

import io.oigres.ecomm.service.users.api.model.BusinessApiException;

public class ProfileBadRequestExceptionResponseApi extends BusinessApiException {
    public ProfileBadRequestExceptionResponseApi() {
    }

    public ProfileBadRequestExceptionResponseApi(String message) {
        super(message);
    }
}
