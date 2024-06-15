package io.oigres.ecomm.service.users.usecases.users.admins.search;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;

public interface GetAdminByIdUseCase {
    AdminProfile handle(Long userId) throws NotFoundProfileException;
}
