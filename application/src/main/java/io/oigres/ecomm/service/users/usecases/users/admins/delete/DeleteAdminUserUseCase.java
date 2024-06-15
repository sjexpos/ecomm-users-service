package io.oigres.ecomm.service.users.usecases.users.admins.delete;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;

public interface DeleteAdminUserUseCase {
    AdminProfile handle(Long userId) throws NotFoundProfileException, UserNotFoundException;
}
