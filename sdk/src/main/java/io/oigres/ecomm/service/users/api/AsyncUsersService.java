package io.oigres.ecomm.service.users.api;

import java.util.concurrent.Future;

import io.oigres.ecomm.service.users.api.model.GenderResponse;
import io.oigres.ecomm.service.users.api.model.UserTypeResponse;
import io.oigres.ecomm.service.users.api.model.ValidateUserRequest;
import io.oigres.ecomm.service.users.api.model.ValidateUserResponse;
import io.oigres.ecomm.service.users.api.model.exception.UserTypeNotFoundException;

public interface AsyncUsersService {
    
    Future<ValidateUserResponse> validateUserAsync(ValidateUserRequest request);

    Future<GenderResponse> getGenderByIdAsync(long genderId) throws io.oigres.ecomm.service.users.api.model.exception.GenderNotFoundException;

    Future<UserTypeResponse> getUserTypeByIdAsync(long userTypeId) throws UserTypeNotFoundException;
}
