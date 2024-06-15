package io.oigres.ecomm.service.users.usecases.users.admins.update;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.exception.profile.ProfileUserException;

public interface UpdateAdminUseCase {
    AdminProfile handle(Long profileId, AdminProfile adminProfile) throws ProfileUserException;
}
